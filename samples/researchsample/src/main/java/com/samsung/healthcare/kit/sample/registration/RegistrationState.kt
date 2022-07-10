package com.samsung.healthcare.kit.sample.registration

sealed class RegistrationState {
    object Init : RegistrationState()
    object Success : RegistrationState()
    object Failed : RegistrationState()
    object Loading : RegistrationState()
}
