3. Domain Classes
   The domain classes represent both the input data and output data. We create Employee and Shift classes, as well as a ShiftSchedule that contains the list of employees and shifts for a particular dataset.

3.1. Employee
An employee is a person we can assign to shifts. Each employee has a name and one or more skills.

The Employee class doesn’t need any Timefold annotation because it does not change during solving:

public class Employee {

    private String name;
    private Set<String> skills;

    public Employee(String name, Set<String> skills) {
        this.name = name;
        this.skills = skills;
    }

    @Override
    public String toString() {
        return name;
    }

    // Getters and setters
}
Copy
3.2. Shift
A shift is a job assignment for exactly one employee on a specific date from a start time to an end time. There can be two shifts at the same time. Each shift has one required skill.

Shift objects change during solving: Each shift is assigned to an employee. Timefold needs to know that. Only the employee field changes during solving. Therefore, we annotate the class with @PlanningEntity and the employee field with @PlanningVariable so Timefold knows it should fill in the employee for each shift:

@PlanningEntity
public class Shift {

    private LocalDateTime start;
    private LocalDateTime end;
    private String requiredSkill;

    @PlanningVariable
    private Employee employee;

    // A no-arg constructor is required for @PlanningEntity annotated classes
    public Shift() {
    }

    public Shift(LocalDateTime start, LocalDateTime end, String requiredSkill) {
        this(start, end, requiredSkill, null);
    }

    public Shift(LocalDateTime start, LocalDateTime end, String requiredSkill, Employee employee) {
        this.start = start;
        this.end = end;
        this.requiredSkill = requiredSkill;
        this.employee = employee;
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }

    // Getters and setters
}
Copy
3.3. ShiftSchedule
A schedule represents a single dataset of employees and shifts. It is both the input and output for Timefold:

We annotate the ShiftSchedule class with @PlanningSolution so Timefold knows it represents the input and output.
We annotate the employees field with @ValueRangeProvider to tell Timefold it contains the list of employees from which it can pick instances to assign to Shift.employee.
We annotate the shifts field with @PlanningEntityCollectionProperty so Timefold finds all Shift instances to assign to an employee.
We include a score field with a @PlanningScore annotation. Timefold will fill this in for us. Let’s use a HardSoftScore so we can differentiate between hard and soft constraints later.
Now, let’s have a look at our class:

@PlanningSolution
public class ShiftSchedule {

    @ValueRangeProvider
    private List<Employee> employees;
    @PlanningEntityCollectionProperty
    private List<Shift> shifts;

    @PlanningScore
    private HardSoftScore score;

    // A no-arg constructor is required for @PlanningSolution annotated classes
    public ShiftSchedule() {
    }

    public ShiftSchedule(List<Employee> employees, List<Shift> shifts) {
        this.employees = employees;
        this.shifts = shifts;
    }

    // Getters and setters
}
Copy
4. Constraints
   Without constraints, Timefold would assign all shifts to the first employee. That’s not a feasible schedule.

To teach it how to distinguish good and bad schedules, let’s add two hard constraints:

The atMostOneShiftPerDay() constraint checks if two shifts on the same date are assigned to the same employee. If that’s the case, it penalizes the score by 1 hard point.
The requiredSkill() constraint checks if a shift is assigned to an employee for which the shift’s required skill is part of the employee’s skill set. If it’s not, it penalizes the score by 1 hard point.
A single hard constraint takes priority over all soft constraints. Typically, hard constraints are impossible to break, either physically or legally. Soft constraints, on the other hand, can be broken, but we want to minimize that. Those typically represent financial costs, service quality, or employee happiness. Hard and soft constraints are implemented with the same API.

4.1. ConstraintProvider
First, we create a ConstraintProvider for our constraint implementations:

public class ShiftScheduleConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
          atMostOneShiftPerDay(constraintFactory),
          requiredSkill(constraintFactory)
        };
    }

    // Constraint implementations

}
Copy
4.2. Unit Test the ConstraintProvider
If it isn’t tested, it doesn’t work — especially for constraints. Let’s create a test class to test each constraint of our ConstraintProvider.

The test-scoped timefold-solver-test dependency contains ConstraintVerifier, a helper to test each constraint in isolation. This improves maintenance — we can refactor a single constraint without breaking tests of other constraints:

public class ShiftScheduleConstraintProviderTest {

    private static final LocalDate MONDAY = LocalDate.of(2030, 4, 1);
    private static final LocalDate TUESDAY = LocalDate.of(2030, 4, 2);

    ConstraintVerifier<ShiftScheduleConstraintProvider, ShiftSchedule> constraintVerifier
      = ConstraintVerifier.build(new ShiftScheduleConstraintProvider(), ShiftSchedule.class, Shift.class);

    // Tests for each constraint

}
Copy
We’ve also prepared two dates to reuse in our tests below. Let’s add the actual constraints next.

4.3. Hard Constraint: at Most One Shift per Day
Following TDD (Test Driven Design), let’s write the tests for our new constraint in our test class first:

@Test
void whenTwoShiftsOnOneDay_thenPenalize() {
Employee ann = new Employee("Ann", null);
constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::atMostOneShiftPerDay)
.given(
new Shift(MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), null, ann),
new Shift(MONDAY.atTime(14, 0), MONDAY.atTime(22, 0), null, ann))
// Penalizes by 2 because both {shiftA, shiftB} and {shiftB, shiftA} match.
// To avoid that, use forEachUniquePair() in the constraint instead of forEach().join() in the implementation.
.penalizesBy(2);
}

@Test
void whenTwoShiftsOnDifferentDays_thenDoNotPenalize() {
Employee ann = new Employee("Ann", null);
constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::atMostOneShiftPerDay)
.given(
new Shift(MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), null, ann),
new Shift(TUESDAY.atTime(14, 0), TUESDAY.atTime(22, 0), null, ann))
.penalizesBy(0);
}
Copy
Then, we implement it in our ConstraintProvider:

public Constraint atMostOneShiftPerDay(ConstraintFactory constraintFactory) {
return constraintFactory.forEach(Shift.class)
.join(Shift.class,
equal(shift -> shift.getStart().toLocalDate()),
equal(Shift::getEmployee))
.filter((shift1, shift2) -> shift1 != shift2)
.penalize(HardSoftScore.ONE_HARD)
.asConstraint("At most one shift per day");
}
Copy
To implement constraints, we use the ConstraintStreams API: a Stream/SQL-like API that provides incremental score calculation (deltas) and indexed hashtable lookups under the hood. This approach scales to datasets with hundreds of thousands of shifts in a single schedule.

Let’s run the tests and verify they are green.

4.4. Hard Constraint: Required Skill
Let’s write the tests in our test class:

@Test
void whenEmployeeLacksRequiredSkill_thenPenalize() {
Employee ann = new Employee("Ann", Set.of("Waiter"));
constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::requiredSkill)
.given(
new Shift(MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), "Cook", ann))
.penalizesBy(1);
}

@Test
void whenEmployeeHasRequiredSkill_thenDoNotPenalize() {
Employee ann = new Employee("Ann", Set.of("Waiter"));
constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::requiredSkill)
.given(
new Shift(MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), "Waiter", ann))
.penalizesBy(0);
}
Copy
Then, let’s implement the new constraint in our ConstraintProvider:

public Constraint requiredSkill(ConstraintFactory constraintFactory) {
return constraintFactory.forEach(Shift.class)
.filter(shift -> !shift.getEmployee().getSkills()
.contains(shift.getRequiredSkill()))
.penalize(HardSoftScore.ONE_HARD)
.asConstraint("Required skill");
}
Copy
Let’s run the tests again. They are still green.

To make this a soft constraint, we would change penalize(HardSoftScore.ONE_HARD) into penalize(HardSoftScore.ONE_SOFT). To turn that into a dynamic decision by the input dataset, we could use penalizeConfigurable() and @ConstraintWeight instead.

5. Application
   We’re ready to put our application together.

5.1. Solve It
To solve a schedule, we create a SolverFactory from our @PlanningSolution, @PlanningEntity, and ConstraintProvider classes. A SolverFactory is a long-lived object. Typically, there’s only one instance per application.

We also need to configure how long we want a solver to run. For large datasets, with thousands of shifts and far more constraints, it’s impossible to find the optimal solution in a reasonable timeframe (due to the exponential nature of NP-hard problems). Instead, we want to find the best possible solution in the amount of time available. Let’s limit that to two seconds for now:

SolverFactory<ShiftSchedule> solverFactory = SolverFactory.create(new SolverConfig()
.withSolutionClass(ShiftSchedule.class)
.withEntityClasses(Shift.class)
.withConstraintProviderClass(ShiftScheduleConstraintProvider.class)
// The solver runs only for 2 seconds on this tiny dataset.
// It's recommended to run for at least 5 minutes ("5m") on large datasets.
.withTerminationSpentLimit(Duration.ofSeconds(2)));
Copy
We use the SolverFactory to create a Solver instance, one per dataset. Then, we call Solver.solve() to solve a dataset:

Solver<ShiftSchedule> solver = solverFactory.buildSolver();
ShiftSchedule problem = loadProblem();
ShiftSchedule solution = solver.solve(problem);
printSolution(solution);
Copy
In Spring Boot, the SolverFactory is built automatically and injected into an @Autowired field:

@Autowired
SolverFactory<ShiftSchedule> solverFactory;
Copy
And we configure the solver time in application.properties:

timefold.solver.termination.spent-limit=5s
Copy
In Quarkus, similarly, the SolverFactory is also built automatically and injected in an @Inject field. The solver time is also configured in application.properties.

To solve asynchronously, to avoid hogging the current thread when calling Solver.solve(), we would inject and use a SolverManager instead.

5.2. Test Data
Let’s generate a tiny dataset of five shifts and three employees as the input problem:

private ShiftSchedule loadProblem() {
LocalDate monday = LocalDate.of(2030, 4, 1);
LocalDate tuesday = LocalDate.of(2030, 4, 2);
return new ShiftSchedule(List.of(
new Employee("Ann", Set.of("Bartender")),
new Employee("Beth", Set.of("Waiter", "Bartender")),
new Employee("Carl", Set.of("Waiter"))
), List.of(
new Shift(monday.atTime(6, 0), monday.atTime(14, 0), "Waiter"),
new Shift(monday.atTime(9, 0), monday.atTime(17, 0), "Bartender"),
new Shift(monday.atTime(14, 0), monday.atTime(22, 0), "Bartender"),
new Shift(tuesday.atTime(6, 0), tuesday.atTime(14, 0), "Waiter"),
new Shift(tuesday.atTime(14, 0), tuesday.atTime(22, 0), "Bartender")
));
}
Copy
5.3. Result
After we run the test data through our solver, we’ll print the output solution to System.out:

private void printSolution(ShiftSchedule solution) {
logger.info("Shift assignments");
for (Shift shift : solution.getShifts()) {
logger.info("  " + shift.getStart().toLocalDate()
+ " " + shift.getStart().toLocalTime()
+ " - " + shift.getEnd().toLocalTime()
+ ": " + shift.getEmployee().getName());
}
}
Copy
Here’s the result for our dataset:

Shift assignments


2030-04-01 06:00 - 14:00: Carl
2030-04-01 09:00 - 17:00: Ann
2030-04-01 14:00 - 22:00: Beth
2030-04-02 06:00 - 14:00: Beth
2030-04-02 14:00 - 22:00: Ann

Copy
Ann wasn’t assigned to the first shift because she didn’t have the waiter skill. But why wasn’t Beth assigned to the first shift? She has the waiter skill.

If Beth had been assigned to the first shift, it would then be impossible to assign both the second and third shifts. Those both need a bartender, so Carl can’t do them. Only when Carl is assigned to the first shift is a feasible solution possible. In large, real-world datasets, these kinds of intricacies become a lot more complex. Let the Solver worry about them.
