package healthstack.app.pref

/**
 * An enumeration representing the different stages of the app.
 * Each value has a corresponding title.
 * @param title The title of the stage.
 */
enum class AppStage(val title: String) {
    Onboarding("Onboarding"),
    SignUp("Sign Up"),
    Home("Home"),
    Profile("Profile"),
    StudyInformation("Study Information"),
    Settings("Settings"),
    Insights("Insights"),
    Education("Education");
}
