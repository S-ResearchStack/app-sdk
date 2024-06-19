rootProject.name = "SamsungHealthStack"
val buildAppsWithSDKProject: String by settings

if (buildAppsWithSDKProject.toBoolean()) {
    include("starter-app")
    project(":starter-app").projectDir = file("samples/starter-app/app")
    include("starter-app-wearable")
    project(":starter-app-wearable").projectDir = file("samples/starter-app/wearable")
}
include(":kit")
include(":healthdata-link:interface")
include(":healthdata-link:healthconnect")
include(":backend-integration:interface")
include(":backend-integration:healthstack-adapter")
include(":resources:korean")
include(":app-support")
include(":common")
include(":wearable-kit")
include(":wearable-support")
