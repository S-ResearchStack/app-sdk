package healthstack.kit.task.onboarding.view.layout

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.RoundButton
import healthstack.kit.ui.SdkCard
import healthstack.kit.ui.TopBar
import se.warting.signaturepad.SignaturePadAdapter
import se.warting.signaturepad.SignaturePadView

@Composable
fun SignatureLayout(
    onClickDone: (String) -> Unit = {},
    onClickCancel: () -> Unit = {},
    lockLandscapeOrientation: Boolean = true,
) {
    if (lockLandscapeOrientation) LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val mutableSvg = remember { mutableStateOf("") }
    var signaturePadAdapter: SignaturePadAdapter? = null

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
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            SdkCard(
                modifier = Modifier
                    .height(189.dp)
                    .testTag("signatureView"),
                color = AppTheme.colors.disabled.copy(0.2F)
            ) {
                SignaturePadView(
                    onReady = { signaturePadAdapter = it },
                    penColor = AppTheme.colors.onSurface
                )
            }

            Text(
                "By signing this document with an electronic signature, " +
                    "I agree that such signature\nwill be as valid as handwritten signatures " +
                    "to the exxtent allowed by local law",
                color = AppTheme.colors.onSurface.copy(0.6F),
                style = AppTheme.typography.body3.copy(lineHeight = 18.sp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RoundButton(
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

                RoundButton(
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

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun SignatureViewPreview() {
    SignatureLayout()
}
