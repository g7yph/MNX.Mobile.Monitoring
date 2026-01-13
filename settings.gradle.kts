pluginManagement {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MinuxMonitoring"

includeBuild("build-logic")

include(":app")
include(":injector")
include(":injector-compose")
include(":core:network")
include(":core:designsystem")
include(":core:ui")
include(":feature:auth:api")
include(":feature:auth:impl")
include(":feature:cryptos:api")
include(":feature:cryptos:impl")
include(":core:base")
include(":feature:flightsheets:api")
include(":feature:flightsheets:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:rigs:impl")
include(":feature:rigs:api")