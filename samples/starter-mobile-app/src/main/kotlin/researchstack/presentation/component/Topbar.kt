package researchstack.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import researchstack.presentation.theme.AppTheme

@Composable
fun TopBar(
    title: String,
    color: Color = AppTheme.colors.onSurface,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = Modifier
            .height(74.dp)
            .padding(start = 8.dp),
        title = {
            Text(
                text = title,
                style = AppTheme.typography.title1,
                color = color,
                maxLines = 1
            )
        },
        backgroundColor = AppTheme.colors.background,
        elevation = 0.dp,
        actions = actions,
    )
}
