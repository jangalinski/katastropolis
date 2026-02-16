import kotlinx.benchmark.gradle.JvmBenchmarkTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper


plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.allopen)
  alias(libs.plugins.kotlinx.benchmark)
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


dependencies {
  implementation(libs.arrow.core)
  implementation(libs.arrow.fx.coroutines)
  implementation(libs.arrow.functions)

  implementation(libs.timefold.solver.core)
  implementation(libs.kotlin.logging)
  implementation(libs.flexmark.html2md.converter)

  implementation(libs.krid)
  implementation(libs.kotlinx.benchmark.runtime)
  implementation(libs.kaliningraph)
  implementation(libs.kotlin.graphs)
  implementation(libs.jgrapht.core)

  implementation(libs.aoc.kotlin)


  val byteBuddyAgent by configurations.creating
  add("byteBuddyAgent", "net.bytebuddy:byte-buddy-agent:1.18.5")


  testImplementation(libs.timefold.solver.test)
  testImplementation(platform("org.junit:junit-bom:6.0.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.assertj:assertj-core:3.27.7")
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
  // Add Byte Buddy as a Java agent so Mockito doesn't need to self-attach
  jvmArgs("-javaagent:${configurations.getByName("byteBuddyAgent").singleFile}")
  testLogging {
    events("passed", "skipped", "failed")
  }
}
