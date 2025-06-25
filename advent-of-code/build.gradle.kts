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
  implementation(libs.arrow.functions)

  implementation(libs.flexmark.html2md.converter)

  implementation(libs.krid)
  implementation(libs.kotlinx.benchmark.runtime)
  implementation(libs.kaliningraph)
  implementation(libs.jgrapht.core)

  implementation(libs.aoc.kotlin)

  testImplementation(platform("org.junit:junit-bom:5.13.2"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.assertj:assertj-core:3.27.3")
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
  jvmToolchain(17)
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
