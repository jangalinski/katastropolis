import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

group = "io.github.jangalinski.kata"
version = "0.0.1-SNAPSHOT"

allprojects {
  apply {
    from("${rootProject.rootDir}/gradle/repositories.gradle.kts")
  }

  plugins.withType<JavaPlugin> {
    extensions.configure<JavaPluginExtension>("java") {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
      }
    }
  }
  plugins.withType<KotlinPluginWrapper> {
    extensions.configure<KotlinJvmProjectExtension>("kotlin") {
      jvmToolchain(21)
      compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
      }
    }
  }
}

plugins {
  base
  idea
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.allopen)
  alias(libs.plugins.kotlinx.benchmark)
}

dependencies {
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)

  implementation(libs.flexmark.html2md.converter)

  implementation(libs.krid)
  implementation(libs.kotlinx.benchmark.runtime)

  implementation(libs.aoc.kotlin)

  testImplementation(platform("org.junit:junit-bom:6.0.0"))
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

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
