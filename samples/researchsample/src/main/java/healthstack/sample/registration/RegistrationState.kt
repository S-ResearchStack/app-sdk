package healthstack.sample.registration

sealed class RegistrationState {
    object Init : RegistrationState()
    object Success : RegistrationState()
    object Failed : RegistrationState()
    object Loading : RegistrationState()
}
