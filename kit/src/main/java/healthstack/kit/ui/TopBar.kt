package healthstack.kit.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun TopBar(
    title: String = "",
    color: Color = AppTheme.colors.onSurface,
    onClickBack: (() -> Unit)?,
    onClickAction: () -> Unit,
    actionIcon: ImageVector = Icons.Default.MoreVert,
) {
    TopAppBar(
        navigationIcon = {
            if (onClickBack != null) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    modifier = Modifier
                        .padding(start = 32.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .clickable { onClickBack() },
                    contentDescription = "back button icon",
                    tint = color,
                )
            }
        },
        actions = {
            Icon(
                imageVector = actionIcon,
                modifier = Modifier
                    .padding(12.dp)
                    .clickable { onClickAction() },
                contentDescription = "action button icon",
                tint = color,
            )
        },
        title = {
            Text(
                text = title,
                color = color
            )
        },
        backgroundColor = MaterialTheme.colors.background.copy(alpha = 0f),
        elevation = 0.dp
    )
}

@Composable
fun TopBar(
    title: String = "",
    color: Color = AppTheme.colors.onSurface,
    onClickBack: (() -> Unit)? = null,
) {
    TopAppBar(
        navigationIcon = onClickBack?.let {
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    modifier = Modifier
                        .padding(start = 32.dp, top = 12.dp, bottom = 12.dp, end = 12.dp)
                        .clickable { onClickBack() },
                    contentDescription = "back button icon",
                    tint = color
                )
            }
        },
        title = {
            Text(
                text = title,
                style = AppTheme.typography.topBar,
                color = color
            )
        },
        backgroundColor = AppTheme.colors.background,
        elevation = 0.dp
    )
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ConsentTopBarPreview() {
    TopBar(onClickBack = {})
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ConsentTopBarPreview2() {
    TopBar(onClickBack = {}, onClickAction = {})
}
