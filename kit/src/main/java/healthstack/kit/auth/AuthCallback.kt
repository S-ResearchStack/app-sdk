package healthstack.kit.auth

class AuthCallback(
    val onSuccess: () -> Unit,
    val onFailure: () -> Unit,
)
