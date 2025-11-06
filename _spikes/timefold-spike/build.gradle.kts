plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation(libs.timefold.solver.core)
  implementation(libs.kotlin.logging)

  testImplementation(libs.timefold.solver.test)

  // Use JUnit 5 (Jupiter) BOM and engine
  testImplementation(platform("org.junit:junit-bom:6.0.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(libs.assertj.core)

  // Mockito Kotlin bindings for idiomatic Kotlin mocks
  testImplementation("org.mockito.kotlin:mockito-kotlin:6.1.0")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
}
