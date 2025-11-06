plugins {
  `kotlin-dsl`
}

// Apply the shared repositories script located in buildSrc/
apply(from = file("../gradle/repositories.gradle.kts"))
