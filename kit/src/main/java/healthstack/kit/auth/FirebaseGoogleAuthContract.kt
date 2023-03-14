package healthstack.kit.auth

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import healthstack.kit.R

class FirebaseGoogleAuthContract(
    private val authCallback: AuthCallback,
) : ActivityResultContract<Unit, FirebaseUser?>() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun getClient(context: Context): GoogleSignInClient =
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authCallback.onSuccess()
                } else {
                    authCallback.onFailure()
                }
            }
    }

    override fun createIntent(context: Context, input: Unit): Intent =
        getClient(context).signInIntent

    override fun parseResult(resultCode: Int, intent: Intent?): FirebaseUser? {
        if (resultCode != RESULT_OK)
            return null

        val googleAccount = GoogleSignIn
            .getSignedInAccountFromIntent(intent)
            .getResult(ApiException::class.java)

        firebaseAuthWithGoogle(googleAccount.idToken!!)

        return auth.currentUser
    }
}
