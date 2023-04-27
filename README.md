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

# Build

## III. Prepare the Modules

1. Open your project's build.gradle file in a text editor or an IDE that supports Kotlin DSL.

2. Locate the `dependencies` block in the build.gradle file. If the block doesn't exist, you can add it at the end of the file:

   ```
   dependencies {
       // dependencies go here
   }
   ```

3. Add the S-HealthStack SDK and related module dependencies inside the `dependencies` block, like so:

   ```
   dependencies {
       implementation("io.s-healthstack:kit:0.9.1")
       implementation("io.s-healthstack:app-support:0.9c")
       implementation("io.s-healthstack:healthdata-link:0.9c")
       implementation("io.s-healthstack:healthconnect:0.9c")
       implementation("io.s-healthstack:backend-integration:0.9c")
       implementation("io.s-healthstack:healthstack-adapter:0.9c")
   }
   ```

   These dependencies will add the necessary modules of the S-HealthStack SDK to your project. You can find a list of all the modules here: [Maven Central - s-healthstack](https://central.sonatype.com/search?smo=true&q=s-healthstack). 

   > Version 0.9c refers to the beta version. Please refer to the Maven Center to see the latest versions.

4. Save the build.gradle file.

5. Build your project with Gradle, either using the command line or from within your IDE. Gradle will automatically download the SDK and its dependencies from the appropriate repositories and include them in your project.

## IV. Unit Test

1. Test either all modules or just the kit.

   - To test all modules, including samples:

     ```
     ./gradlew test
     ```

   - To test just the kit:

     ```
     ./gradlew :kit:test
     ```


## V. Check the Coding Style

1. Use the [Compose API guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md){:target="_blank"} to check your coding style. The guidelines outline the patterns, best practices, and prescriptive style guidelines for writing idiomatic Jetpack Compose APIs. 

## VI. Create a Firebase Project

1. Follow the instructions at [https://firebase.google.com/docs/android/setup](https://firebase.google.com/docs/android/setup){:target="_blank"} to add a Firebase project to the Firebase account you created during backend system installation.

## VII. Per App Configuration

After app creation, for each app you need to:

1. Register your app with your Firebase project by updating the ***\<repository\>*/app/google-service.json** configuration file.

2. For full-stack implementations only, associate your app with the backend system and portal study.

Refer to [Configuring the App Environment](../app-creation/configure-app.md) for details.

## VIII. Reference Documentation

Refer to our [API reference](../../api-reference/api-overview.md) and [SDK reference](../../sdk-reference/kit.md) documentation for details on all the backend API endpoints and SDK packages.
