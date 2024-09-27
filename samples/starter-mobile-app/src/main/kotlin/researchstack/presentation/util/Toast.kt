package researchstack.presentation.util

import android.content.Context
import android.util.Log
import android.widget.Toast

fun showErrorToast(context: Context, tag: String, message: String, throwable: Throwable? = null) {
    throwable?.let {
        Log.e(tag, it.stackTraceToString())
        Log.e(tag, message + throwable.message)
    }
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
