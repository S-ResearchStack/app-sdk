package researchstack.auth.data.datasource.auth.samsung

import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import researchstack.auth.data.repository.auth.samsung.SAStringResource.DATA_FIELD_ERROR_CODE
import researchstack.auth.data.repository.auth.samsung.SAStringResource.DATA_FIELD_ERROR_MESSAGE

class SACallbackHandler : ISACallbackStubAdapter() {

    private var idTokenListener: IdTokenListener? = null

    override fun asBinder(): IBinder {
        return this
    }

    override fun onReceiveAuthCode(requestID: Int, isSuccess: Boolean, data: Bundle?) {
        if (!isSuccess) {
            Log.e(
                TAG,
                "${data?.getString(DATA_FIELD_ERROR_CODE)}: " +
                    "${data?.getString(DATA_FIELD_ERROR_MESSAGE)}"
            )
            idTokenListener?.onReceiveTokenResult(
                Result.failure(IllegalStateException(data?.getString(DATA_FIELD_ERROR_MESSAGE)))
            )
            return
        }

        getIdToken(data)
            .onFailure {
                Log.e(TAG, it.message ?: "no id-token in response")
                idTokenListener?.onReceiveTokenResult(Result.failure(it))
            }
            .onSuccess { idToken ->
                Log.d(TAG, "Id Token - $idToken")
                idTokenListener?.onReceiveTokenResult(Result.success(idToken))
            }
    }

    private fun getIdToken(data: Bundle?): Result<String> =
        data?.getString(ID_TOKEN_KEY)
            ?.let { Result.success(it) }
            ?: Result.failure(RemoteException("idToken is null"))

    fun registerIdTokenListener(listener: IdTokenListener) {
        idTokenListener = listener
    }

    companion object {
        private val TAG = SACallbackHandler::class.simpleName
        internal const val ID_TOKEN_KEY = "id_token"
    }
}
