plugins {
  id("org.jetbrains.kotlin.jvm") version "2.1.0"
}

apply {
  from("${rootProject.rootDir}/gradle/repositories.gradle.kts")
}

dependencies {
  implementation(libs.krid)

  //implementation(libs.aocKotlin)
}
