repositories {
  mavenLocal()
  mavenCentral()
  gradlePluginPortal()
}

configurations.all {
  with(resolutionStrategy) {
    cacheChangingModulesFor(0, TimeUnit.SECONDS)
    cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
  }
}
