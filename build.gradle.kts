import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.testing.Test

group = "io.github.jangalinski.kata"
version = "0.0.1-SNAPSHOT"

plugins {
  base
  idea
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.allopen)
  alias(libs.plugins.kotlinx.benchmark)
}

allprojects {
  apply(from = file("${rootProject.rootDir}/gradle/repositories.gradle.kts"))

  // Provide Byte Buddy agent for Mockito inline mock-maker to avoid JDK warnings and future breakage
  val byteBuddyAgent by configurations.creating
  dependencies {
    add("byteBuddyAgent", "net.bytebuddy:byte-buddy-agent:1.17.8")
  }

  tasks.withType<Test>().configureEach {
    // Add Byte Buddy as a Java agent so Mockito doesn't need to self-attach
    jvmArgs("-javaagent:${configurations.getByName("byteBuddyAgent").singleFile}")
  }

  plugins.apply {

    withType<JavaPlugin> {
      extensions.configure<JavaPluginExtension>("java") {
        toolchain {
          languageVersion.set(JavaLanguageVersion.of(21))
        }
      }

      // Copy shared logback.xml into each project's resources during processing
      tasks.withType<ProcessResources>().configureEach {
        from(rootProject.layout.projectDirectory.dir("gradle/shared")) {
          include("logback.xml")
          into("")
        }
        // In case some projects also provide their own logback.xml, keep both
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
      }

      // Provide SLF4J backend (Logback) by default for all projects with Java/Kotlin
      // Adds provider on main and test runtime classpath
      val catalogs = project.extensions.getByType<VersionCatalogsExtension>()
      val libsCatalog = catalogs.named("libs")
      project.dependencies.apply {
        add("runtimeOnly", libsCatalog.findLibrary("logback-classic").get())
        add("testRuntimeOnly", libsCatalog.findLibrary("logback-classic").get())
      }
    }

    withType<KotlinPluginWrapper> {
      extensions.configure<KotlinJvmProjectExtension>("kotlin") {
        jvmToolchain(21)
        compilerOptions {
          freeCompilerArgs.addAll("-Xjsr305=strict")
        }
      }
    }
  }
}

dependencies {
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)

  implementation(libs.flexmark.html2md.converter)

  implementation(libs.krid)
  implementation(libs.kotlinx.benchmark.runtime)

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

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}
