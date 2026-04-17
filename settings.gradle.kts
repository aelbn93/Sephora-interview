pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Sephora"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")


include(":core:common")
include(":core:network")
include(":core:data")
include(":core:model")
include(":core:database")
include(":core:navigation")
include(":core:designsystem")
include(":core:ui")
include(":core:domain")
include(":core:testing")
include(":feature:product:api")
include(":feature:product:impl")
