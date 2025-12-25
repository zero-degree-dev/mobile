pluginManagement {
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.yandex.ru")
        }
    }
}

rootProject.name = "zero-degree"
include(":app")
include(":lib:injector")

// Libs modules
include(":libs:ImageLoader:api")
include(":libs:ImageLoader:impl")
include(":libs:User:api")
include(":libs:User:impl")

// Core modules
include(":core:api")
include(":core:storage")
include(":core-ui")

// Feature modules with api and impl
include(":features:auth:api")
include(":features:auth:impl")
include(":features:home:api")
include(":features:home:impl")
include(":features:bars:api")
include(":features:bars:impl")
include(":features:drinks:api")
include(":features:drinks:impl")
include(":features:events:api")
include(":features:events:impl")
include(":features:bookings:api")
include(":features:bookings:impl")
include(":features:profile:api")
include(":features:profile:impl")