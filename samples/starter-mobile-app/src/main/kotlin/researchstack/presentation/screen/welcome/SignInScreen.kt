package researchstack.presentation.screen.welcome

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import researchstack.BuildConfig
import researchstack.auth.data.repository.auth.samsung.SAStringResource.DATA_FIELD_ERROR_CODE
import researchstack.auth.data.repository.auth.samsung.SAStringResource.DATA_FIELD_ERROR_MESSAGE
import researchstack.presentation.util.showErrorToast

private const val SIGN_IN_INTENT = "com.osp.app.signin.action.ADD_SAMSUNG_ACCOUNT"
private const val TAG = "SignInScreen"

@Composable
fun SignInScreen(onSuccess: () -> Unit) {
    val context = LocalContext.current
    val onFailure = {
        showErrorToast(context, TAG, "Samsung Account Login Failed")
    }

    val signInLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) onResult@{
            val data = it.data
            if (data == null) {
                onFailure()
                return@onResult
            }

            if (it.resultCode == Activity.RESULT_OK || isAlreadyLogged(data)) {
                Log.i(TAG, "Login Success")
                onSuccess()
            } else {
                val errorCode = data.getStringExtra(DATA_FIELD_ERROR_CODE)
                val errorMessage = data.getStringExtra(DATA_FIELD_ERROR_MESSAGE)
                Log.e(TAG, "Error occurred: $errorCode - $errorMessage")
                onFailure()
            }
        }

    LaunchedEffect(null) {
        val intent = Intent(SIGN_IN_INTENT)
        intent.putExtra("client_id", BuildConfig.SA_CLIENT_ID)
        signInLauncher.launch(intent)
    }
}

private fun isAlreadyLogged(data: Intent) =
    data.getStringExtra(DATA_FIELD_ERROR_MESSAGE) == "Samsung account already exists"
