repositories {
  mavenLocal()
  mavenCentral()
  maven {
    url = uri("https://central.sonatype.com/repository/maven-snapshots/")
    mavenContent {
      snapshotsOnly()
    }
  }
  gradlePluginPortal()
}

configurations.all {
  with(resolutionStrategy) {
    cacheChangingModulesFor(0, TimeUnit.SECONDS)
    cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
  }
}
