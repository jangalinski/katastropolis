group = "io.github.jangalinski.kata"
version = "0.0.1-SNAPSHOT"

plugins {
  base
  idea
}

allprojects {
  apply(from = file("${rootProject.rootDir}/gradle/repositories.gradle.kts"))
}
