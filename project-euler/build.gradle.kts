import kotlinx.benchmark.gradle.JvmBenchmarkTarget

plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.allopen)
  alias(libs.plugins.kotlinx.benchmark)
}

dependencies {
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)

  implementation(libs.aoc.kotlin)

  testImplementation(platform("org.junit:junit-bom:6.0.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.assertj:assertj-core:3.27.6")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
  jvmToolchain(21)
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
