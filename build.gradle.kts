import kotlinx.benchmark.gradle.JvmBenchmarkTarget

apply {
  from("${rootProject.rootDir}/gradle/repositories.gradle.kts")
}

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.allopen)
  alias(libs.plugins.kotlinx.benchmark)
}

dependencies {
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)
  implementation(libs.krid)
  implementation(libs.kotlinx.benchmark.runtime)

  implementation(libs.aoc.kotlin)
}

allOpen {
  annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
  configurations {
    named("main") {
      iterationTime = 5
      iterationTimeUnit = "sec"

    }
  }
  targets {
    register("main") {
      this as JvmBenchmarkTarget
      jmhVersion = "1.21"
    }
  }
}

kotlin {
  jvmToolchain(17)
}

