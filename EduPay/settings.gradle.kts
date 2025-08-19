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
        maven { url = uri("https://jitpack.io") } // ✅ Needed for libraries like ImagePicker, MPAndroidChart (if from GitHub), etc.

    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()                // ✅ Required for AndroidX, AGP, Google Play Services, etc.
        mavenCentral()          // ✅ Required for most libraries like Retrofit, OkHttp, etc.
        maven { url = uri("https://jitpack.io") } // ✅ Needed for libraries like ImagePicker, MPAndroidChart (if from GitHub), etc.
    }
}

rootProject.name = "EduPay"
include(":app")
