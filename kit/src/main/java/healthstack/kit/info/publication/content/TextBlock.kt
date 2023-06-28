package healthstack.kit.info.publication.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import healthstack.kit.theme.AppTheme

class TextBlock(
    val text: String
) : ContentBlock() {
    @Composable
    override fun Render() {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            color = AppTheme.colors.onBackground,
            textAlign = TextAlign.Left,
            style = AppTheme.typography.body2
        )
    }
}
