plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.boot)
}

dependencies {
	implementation(platform(libs.spring.boot.dependencies))

	implementation(libs.spring.boot.starter.web)
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation(libs.springdoc.starter.ui)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
