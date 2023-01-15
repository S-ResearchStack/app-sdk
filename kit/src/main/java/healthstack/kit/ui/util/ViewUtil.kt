package healthstack.kit.ui.util

import android.content.Context
import android.widget.Toast

object ViewUtil {
    fun showToastMessage(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_LONG)
            .show()
}
