package healthstack.wearable.support.helper

import android.app.Activity
import android.content.Context
import android.os.Build

fun getOrientation(context: Context): Int? =
    if (Build.VERSION.SDK_INT >= 30)
        context.display?.rotation
    else
        (context as Activity).windowManager.defaultDisplay.rotation
