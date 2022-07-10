package com.samsung.healthcare.kit.auth

import androidx.activity.result.contract.ActivityResultContract
import com.google.firebase.auth.FirebaseUser
import com.samsung.healthcare.kit.view.auth.AuthCallback

object AuthContractFactory {
    fun createAuthContract(
        provider: SignInProvider,
        authCallback: AuthCallback,
    ): ActivityResultContract<Unit, FirebaseUser?> =
        when (provider) {
            SignInProvider.Google -> FirebaseGoogleAuthContract(authCallback)
            else -> TODO()
        }
}
