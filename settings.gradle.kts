rootProject.name = "katastropolis"

include(":advent-of-code")
include(":project-euler")

include(":one-armed-bandit")
project(":one-armed-bandit").projectDir = file("holi-kata/one-armed-bandit")

include(":timefold-spike")
project(":timefold-spike").projectDir = file("_spikes/timefold-spike")
