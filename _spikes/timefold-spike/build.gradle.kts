plugins {
  alias(libs.plugins.kotlin.jvm)
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation(libs.timefold.solver.core)

  testImplementation(libs.timefold.solver.test)

  // Use JUnit 5 (Jupiter) BOM and engine
  testImplementation(platform("org.junit:junit-bom:6.0.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation(libs.assertj.core)
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
  useJUnitPlatform()
}
