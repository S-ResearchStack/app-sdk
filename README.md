## Samsung Health Stack App SDK

The app SDK provides developers with what’s needed to create mobile apps that collect data from participants. You can create a mobile app that solicits survey responses from the participant, receives data from the wearable device, displays information to the participant, and transmits the data to a backend system (yours or ours) for further analysis. Building blocks cover:

- Participant onboarding and consent
- Survey presentation
- Creation of participant tasks
- Visual reporting to keep participants engaged
- Data management and transmission to the portal

The stack also includes:

-   A backend system consisting of services and a data engine available through application programming interface (API) endpoints
-   A web portal for survey creation, participant management, and data analysis

Refer to <a href="https://s-healthstack.io" target="_blank">https://s-healthstack.io</a> for documentation, or jump directly to:
- The <a href="https://s-healthstack.io/install-backend.html" target="_blank">backend system installation instructions</a>
- The <a href="https://s-healthstack.io/install-sdk.html" target="_blank">app SDK installation instructions</a>
- The <a href="https://s-healthstack.io/install-portal.html" target="_blank">web portal installation instructions</a>
- The <a href="https://s-healthstack.io/tutorial.html" target="_blank">getting started tutorial</a>



Follow these instructions to install, build, and verify the app SDK.

> If you are installing the full stack, this installation requires successful prior completion of the [backend system installation](install-backend.md).

# Prerequisites

## I. Install OpenJDK 17

1. Set up and install OpenJDK 17 using the instructions at [https://openjdk.org/install/](https://openjdk.org/install/){:target="_blank"}.

## II. Install Android Studio 

1. Set up and install Android Studio on Windows, macOS, or Linux using the instructions at [https://developer.android.com/studio](https://developer.android.com/studio){:target="_blank"}.

## III. Clone the Repository

1. Clone the app SDK repository. 

   ```
   git clone https://github.com/S-HealthStack/app-sdk
   ```

# Build

<!-- Zain to check if detekt is supported -->

## IV. Prepare the Modules

1. Clean all modules.

   ```
   ./gradlew clean 
   ```

2. Build either all modules or just the kit.

   - To build all modules, including sample modules:

     ```
     ./gradlew build -x detekt
     ```

   - To build just the kit module and generate an android archive package (aar):

     ```
     ./gradlew :kit:build -x detekt
     ```

## V. Unit Test

1. Test either all modules or just the kit.

   - To test all modules, including samples:

     ```
     ./gradlew test
     ```

   - To test just the kit:

     ```
     ./gradlew :kit:test
     ```


## VI. Check the Coding Style

1. Use the [Compose API guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md){:target="_blank"} to check your coding style. The guidelines outline the patterns, best practices, and prescriptive style guidelines for writing idiomatic Jetpack Compose APIs. 

## VII. Create a Firebase Project

1. Follow the instructions at [https://firebase.google.com/docs/android/setup](https://firebase.google.com/docs/android/setup){:target="_blank"} to add a Firebase project to the Firebase account you created during backend system installation.

## VIII. Per App Configuration

After app creation, for each app you need to:

1. Register your app with your Firebase project by updating the ***\<repository\>*/app/google-service.json** configuration file.

2. For full-stack implementations only, associate your app with the backend system and portal study.

Refer to [Configuring the App Environment](../app-creation/configure-app.md) for details.

## IX. Reference Documentation

Refer to our [API reference](../../api-reference/api-overview.md) and [SDK reference](../../sdk-reference/kit.md) documentation for details on all the backend API endpoints and SDK packages.
