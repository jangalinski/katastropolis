plugins {
  alias(libs.plugins.kotlin.jvm)
}

repositories {
  maven {
    url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    mavenContent {
      snapshotsOnly()
    }
  }
}


dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation(libs.kotlin.logging)

  implementation(platform("dev.tamboui:tamboui-bom:0.2.0-SNAPSHOTS"))

  // For Toolkit DSL (recommended)
  implementation("dev.tamboui:tamboui-toolkit")

  // JLine backend (required)
  implementation("dev.tamboui:tamboui-jline3-backend")

  // Use JUnit 5 (Jupiter) BOM and engine
  testImplementation(platform("org.junit:junit-bom:6.1.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(libs.assertj.core)

  // Mockito Kotlin bindings for idiomatic Kotlin mocks
  testImplementation("org.mockito.kotlin:mockito-kotlin:6.4.0")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
}
