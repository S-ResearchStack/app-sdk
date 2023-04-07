package healthstack.kit.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.HyperLinkAction.DIAL
import healthstack.kit.ui.HyperLinkAction.EMAIL
import healthstack.kit.ui.HyperLinkAction.WEB

enum class HyperLinkAction {
    WEB, EMAIL, DIAL
}

/**
 * When clicked, it performs a specific action.
 *
 * @param text a text to display
 * @param meta property that needs to perform an action
 *             if the action is WEB, the meta is url.
 *             if the action is DIAL, the meta is phone number.
 *             if the action is EMAIL, the meta is receiver's email address.
 * @param action one of WEB / DIAL / EMAIL
 */
@Composable
fun HyperLinkText(
    text: String,
    meta: String,
    action: HyperLinkAction = WEB,
) {

    val composeContext = LocalContext.current
    val httpPrefix = "https://"

    ClickableText(
        text = AnnotatedString(text),
        style = AppTheme.typography.title3.copy(color = AppTheme.colors.primary),
        onClick = {
            when (action) {
                WEB -> {
                    val url: String = if (!meta.startsWith(httpPrefix))
                        "$httpPrefix$meta"
                    else
                        meta

                    composeContext.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                        )
                    )
                }

                DIAL -> {
                    composeContext.startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:$meta")
                        )
                    )
                }

                EMAIL -> {
                    composeContext.startActivity(
                        Intent(
                            Intent.ACTION_SENDTO,
                            Uri.parse("mailto:$meta")
                        )
                    )
                }
            }
        }
    )
}
