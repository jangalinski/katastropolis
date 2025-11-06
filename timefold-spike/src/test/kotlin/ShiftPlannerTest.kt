package io.github.jangalinski.spike.timefold

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty
import ai.timefold.solver.core.api.domain.solution.PlanningScore
import ai.timefold.solver.core.api.domain.solution.PlanningSolution
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider
import ai.timefold.solver.core.api.domain.variable.PlanningVariable
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider
import ai.timefold.solver.core.api.score.stream.Joiners.equal
import ai.timefold.solver.core.api.solver.SolverFactory
import ai.timefold.solver.core.config.solver.SolverConfig
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier
import ai.timefold.solver.test.api.score.stream.ConstraintVerifier.build
import org.junit.jupiter.api.Test
import org.junit.platform.commons.logging.LoggerFactory
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

data class Employee(
  val name: String,
  val skills: Set<String> = emptySet()
)

@PlanningEntity
class Shift() {

  @PlanningVariable
  lateinit var employee: Employee
  lateinit var start: LocalDateTime
  lateinit var end: LocalDateTime
  lateinit var requiredSkill: String

  constructor(start: LocalDateTime, end: LocalDateTime, requiredSkill: String) : this() {
    this.start = start
    this.end = end
    this.requiredSkill = requiredSkill
  }

  constructor(employee: Employee, start: LocalDateTime, end: LocalDateTime, requiredSkill: String) : this(
    start, end, requiredSkill
  ) {
    this.employee = employee
  }

  override fun toString() = "Shift(employee=${employee.name}, skill=$requiredSkill, start=$start, end=$end)"
}

@PlanningSolution
class ShiftSchedule() {

  @ValueRangeProvider
  lateinit var employees: MutableList<Employee>

  @PlanningEntityCollectionProperty
  lateinit var shifts: MutableList<Shift>

  @PlanningScore
  lateinit var score: HardSoftScore

  constructor(employees: MutableList<Employee>, shifts: MutableList<Shift>) : this() {
    this.employees = employees
    this.shifts = shifts
  }

}

class ShiftScheduleConstraintProvider : ConstraintProvider {
  override fun defineConstraints(constraintFactory: ConstraintFactory): Array<Constraint> = arrayOf<Constraint>(
    atMostOneShiftPerDay(constraintFactory),
    requiredSkill(constraintFactory)
  )

  fun atMostOneShiftPerDay(constraintFactory: ConstraintFactory): Constraint = constraintFactory.forEach(Shift::class.java)
    .join(
      Shift::class.java,
      equal { it.start.toLocalDate() },
      equal(Shift::employee)
    )
    .filter { shift1, shift2 -> shift1 !== shift2 }
    .penalize(HardSoftScore.ONE_HARD)
    .asConstraint("At most one shift per day")

  fun requiredSkill(constraintFactory: ConstraintFactory): Constraint = constraintFactory.forEach(Shift::class.java)
    .filter { shift: Shift ->
      !shift.employee.skills.contains(shift.requiredSkill)
    }
    .penalize<HardSoftScore>(HardSoftScore.ONE_HARD)
    .asConstraint("Required skill")
}

class ShiftPlannerTest {
  val logger = LoggerFactory.getLogger(ShiftPlannerTest::class.java)
  val MONDAY: LocalDate = LocalDate.of(2030, 4, 1)
  val TUESDAY: LocalDate = LocalDate.of(2030, 4, 2)

  val constraintVerifier: ConstraintVerifier<ShiftScheduleConstraintProvider, ShiftSchedule> = build(
    ShiftScheduleConstraintProvider(),
    ShiftSchedule::class.java,
    Shift::class.java
  )

  @Test
  fun whenTwoShiftsOnOneDay_thenPenalize() {
    val ann = Employee("Ann")

    constraintVerifier
      .verifyThat(ShiftScheduleConstraintProvider::atMostOneShiftPerDay)
      .given(
        Shift(ann, MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), ""),
        Shift(ann, MONDAY.atTime(14, 0), MONDAY.atTime(22, 0), "")
      ) // Penalizes by 2 because both {shiftA, shiftB} and {shiftB, shiftA} match.
      // To avoid that, use forEachUniquePair() in the constraint instead of forEach().join() in the implementation.
      .penalizesBy(2)
  }

  @Test
  fun whenTwoShiftsOnDifferentDays_thenDoNotPenalize() {
    val ann = Employee("Ann")
    constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::atMostOneShiftPerDay)
      .given(
        Shift(ann, MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), ""),
        Shift(ann, TUESDAY.atTime(14, 0), TUESDAY.atTime(22, 0), "")
      )
      .penalizesBy(0)
  }

  @Test
  fun whenEmployeeLacksRequiredSkill_thenPenalize() {
    val ann = Employee("Ann", mutableSetOf<String>("Waiter"))
    constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::requiredSkill)
      .given(
        Shift(ann, MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), "Cook")
      )
      .penalizesBy(1)
  }

  @Test
  fun whenEmployeeHasRequiredSkill_thenDoNotPenalize() {
    val ann = Employee("Ann", setOf("Waiter"))
    constraintVerifier.verifyThat(ShiftScheduleConstraintProvider::requiredSkill)
      .given(
        Shift(ann, MONDAY.atTime(6, 0), MONDAY.atTime(14, 0), "Waiter")
      )
      .penalizesBy(0)
  }

  @Test
  fun `solve it`() {
    val solverFactory: SolverFactory<ShiftSchedule> = SolverFactory.create<ShiftSchedule>(
      SolverConfig()
        .withSolutionClass(ShiftSchedule::class.java)
        .withEntityClasses(Shift::class.java)
        .withConstraintProviderClass(ShiftScheduleConstraintProvider::class.java) // The solver runs only for 2 seconds on this tiny dataset.
        // It's recommended to run for at least 5 minutes ("5m") on large datasets.
        .withTerminationSpentLimit(2.seconds.toJavaDuration())
    )

    fun loadProblem(): ShiftSchedule {
      val monday = LocalDate.of(2030, 4, 1)
      val tuesday = LocalDate.of(2030, 4, 2)
      return ShiftSchedule(
        mutableListOf(
          Employee("Ann", setOf("Bartender")),
          Employee("Beth", setOf("Waiter", "Bartender")),
          Employee("Carl", setOf("Waiter"))
        ),
        mutableListOf(
          Shift(monday.atTime(6, 0), monday.atTime(14, 0), "Waiter"),
          Shift(monday.atTime(9, 0), monday.atTime(17, 0), "Bartender"),
          Shift(monday.atTime(14, 0), monday.atTime(22, 0), "Bartender"),
          Shift(tuesday.atTime(6, 0), tuesday.atTime(14, 0), "Waiter"),
          Shift(tuesday.atTime(14, 0), tuesday.atTime(22, 0), "Bartender")
        )
      )
    }

    val solver = solverFactory.buildSolver()
    val problem: ShiftSchedule = loadProblem()
    val solution = solver.solve(problem)

    logger.info { "Shift assignments" }
    solution.shifts.forEach{
      logger.info { it.toString() }
    }
  }
}
