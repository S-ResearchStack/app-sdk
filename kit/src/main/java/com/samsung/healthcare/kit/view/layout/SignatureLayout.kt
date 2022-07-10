package com.samsung.healthcare.kit.view.layout

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.SdkCard
import com.samsung.healthcare.kit.view.common.SquareButton
import com.samsung.healthcare.kit.view.common.TopBar
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

@Composable
fun SignatureLayout(
    onClickDone: (String) -> Unit = {},
    onClickCancel: () -> Unit = {},
    lockLandscapeOrientation: Boolean = false,
) {
    if (lockLandscapeOrientation) LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val mutableSvg = remember { mutableStateOf("") }
    var signaturePadAdapter: SignaturePadAdapter? = null
    val penColor = remember { mutableStateOf(Color.Black) }

    Scaffold(
        topBar = {
            TopBar(
                onClickBack = null,
                onClickAction = { onClickCancel() },
                actionIcon = ImageVector.vectorResource(R.drawable.ic_cancel)
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            SdkCard(
                modifier = Modifier
                    .height(200.dp)
                    .padding(horizontal = 50.dp, vertical = 10.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF3F4F4)
            ) {
                SignaturePadView(
                    onReady = { signaturePadAdapter = it },
                    penColor = penColor.value
                )
            }

            Text(
                "By signing this document with an electronic signature, I agree that such signature will be as valid as handwritten signatures to the exxtent allowed by local law"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp),
                horizontalArrangement = Arrangement.End
            ) {
                SquareButton(
                    modifier = Modifier
                        .height(44.dp)
                        .width(144.dp),
                    text = "Rewrite",
                    border = BorderStroke(width = 1.dp, color = AppTheme.colors.primary),
                    buttonColor = AppTheme.colors.background,
                    textColor = AppTheme.colors.primary,
                    onClick = {
                        mutableSvg.value = ""
                        signaturePadAdapter?.clear()
                    }
                )

                Spacer(modifier = Modifier.width(24.dp))

                SquareButton(
                    modifier = Modifier
                        .height(44.dp)
                        .width(144.dp)
                        .testTag("signatureDoneButton"),
                    text = "Sign",
                    border = BorderStroke(width = 1.dp, color = AppTheme.colors.primary),
                    onClick = {
                        mutableSvg.value = signaturePadAdapter?.getSignatureSvg() ?: ""
                        onClickDone(mutableSvg.value)
                    }
                )
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation

        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? =
    when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }

@Preview(showBackground = true)
@Composable
fun SignatureViewPreview() {
    SignatureLayout()
}
