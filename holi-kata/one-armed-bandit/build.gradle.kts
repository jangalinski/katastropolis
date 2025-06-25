plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spring.boot)

    alias(libs.plugins.spring.dependency.management)
}

dependencies {
	implementation(libs.spring.boot.starter.web)
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation(libs.spring.boot.starter.test)
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}
