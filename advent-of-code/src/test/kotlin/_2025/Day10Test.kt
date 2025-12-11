package io.github.jangalinski.kata.aoc._2025

import ai.timefold.solver.core.api.domain.entity.PlanningEntity
import ai.timefold.solver.core.api.domain.lookup.PlanningId
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty
import ai.timefold.solver.core.api.domain.solution.PlanningEntityProperty
import ai.timefold.solver.core.api.domain.solution.PlanningScore
import ai.timefold.solver.core.api.domain.solution.PlanningSolution
import ai.timefold.solver.core.api.domain.solution.ProblemFactProperty
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable
import ai.timefold.solver.core.api.domain.variable.PlanningVariable
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore
import ai.timefold.solver.core.api.score.stream.Constraint
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors
import ai.timefold.solver.core.api.score.stream.ConstraintFactory
import ai.timefold.solver.core.api.score.stream.ConstraintProvider
import ai.timefold.solver.core.api.solver.SolverFactory
import ai.timefold.solver.core.config.constructionheuristic.ConstructionHeuristicPhaseConfig
import ai.timefold.solver.core.config.solver.SolverConfig
import ai.timefold.solver.core.config.solver.termination.TerminationConfig
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@PlanningEntity
data class ButtonPress(
  @PlanningId
  val id: Int,
  @PlanningVariable(valueRangeProviderRefs = ["allButtons"])
  val button: Day10.Button
) {
  companion object {
    fun invoke(size:Int, all: List<ButtonPress>) =   all.fold(Day10.LightsSilver(size)) { acc, bp -> acc + bp.button }
  }
}



data class Target(val lights: Day10.LightsSilver)

@PlanningSolution
class Silver(
  @ProblemFactProperty
  val target : Target = Target(Day10.LightsSilver(0)),

  @ValueRangeProvider(id = "allButtons")
  val allButtons: List<Day10.Button> = emptyList()
) {

  constructor(target: Day10.LightsSilver, allButtons: List<Day10.Button>) : this(
    Target(target),
    allButtons
  )

  @PlanningEntityCollectionProperty
  var presses: List<ButtonPress> = emptyList()

  @PlanningScore
  var score: HardSoftScore = HardSoftScore.ZERO

}

class Constraints : ConstraintProvider {
  override fun defineConstraints(cf: ConstraintFactory): Array<Constraint> = arrayOf(
    cf.forEach(ButtonPress::class.java)
      .groupBy(ConstraintCollectors.toList())
      .join(cf.forEach(Target::class.java))
      .filter { buttons, target ->
        println(buttons)
        println(target)
        target.lights != ButtonPress.invoke(target.lights.size, buttons) }
      .penalize(HardSoftScore.ONE_HARD)
      .asConstraint("Target not reached")
//    ,
//    cf.forEach(Silver::class.java)
//      .penalize(HardSoftScore.ONE_SOFT) { it.presses.size }
//      .asConstraint("Number of buttons pressed")
  )
}

class Day10Test {
  @Test
  fun name() {
    val input = Day10.silverInput(true)

    val solverConfig = SolverConfig()
      .withSolutionClass(Silver::class.java)
      .withEntityClasses(ButtonPress::class.java)
      .withConstraintProviderClass(Constraints::class.java)
      .withPhases(
        ConstructionHeuristicPhaseConfig()
      )
      .withTerminationConfig(
        TerminationConfig().apply {
          bestScoreLimit = "0hard/*soft"
          spentLimit = 5.seconds.toJavaDuration()
        }
      )

    val (target, buttons) = input.first()
    val res = SolverFactory.create<Silver>(solverConfig).buildSolver()
      .solve(Silver(
        target = target,
        allButtons = buttons
      ))

    println(res.score)
  }
}
