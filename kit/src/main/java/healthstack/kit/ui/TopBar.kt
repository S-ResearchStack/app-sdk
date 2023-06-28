package healthstack.kit.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

@Composable
fun TopBar(
    title: String = "",
    color: Color = AppTheme.colors.onSurface,
    onClickBack: (() -> Unit)?,
    actionIcon: ImageVector = Icons.Default.MoreVert,
    onClickAction: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            if (onClickBack != null) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 12.dp, bottom = 12.dp)
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
                style = AppTheme.typography.subtitle1,
                color = color,
                maxLines = 1
            )
        },
        backgroundColor = AppTheme.colors.background,
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
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = onClickBack?.let {
            {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 12.dp, bottom = 12.dp)
                        .wrapContentWidth()
                        .clickable { onClickBack() },
                    contentDescription = "back button icon",
                    tint = color
                )
            }
        },
        title = {
            Text(
                text = title,
                style = AppTheme.typography.subtitle1,
                color = color,
                maxLines = 1
            )
        },
        backgroundColor = AppTheme.colors.background,
        elevation = 0.dp
    )
}

@Composable
fun TopBarWithDropDown(
    title: String = "",
    style: TextStyle = AppTheme.typography.subtitle1,
    color: Color = AppTheme.colors.onSurface,
    items: List<DropdownMenuItemData>,
) {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(
                text = title,
                style = style,
                color = color,
                modifier = Modifier.padding(start = 10.dp),
                maxLines = 1
            )
        },
        elevation = 0.dp,
        actions = {
            IconButton(
                modifier = Modifier.testTag("dropDownIcon"),
                onClick = {
                    expanded.value = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "open dropdown",
                    tint = color,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .width(width = 200.dp)
                    .wrapContentHeight(),
                expanded = expanded.value,
                onDismissRequest = {
                    expanded.value = false
                },
            ) {
                items.forEach {
                    DropdownMenuItem(
                        onClick = {
                            it.onClick()
                            expanded.value = false
                        },
                        enabled = true,
                        modifier = Modifier.height(48.dp)
                    ) {

                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.text,
                            tint = color
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = it.text,
                            style = AppTheme.typography.body3,
                            color = color
                        )
                    }
                }
            }
        },
        backgroundColor = AppTheme.colors.background,
    )
}

data class DropdownMenuItemData(val text: String, val icon: ImageVector, val onClick: () -> Unit)

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ConsentTopBarPreview() {
    TopBar("Hello")
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ConsentTopBarPreview2() {
    TopBar("Hello", onClickBack = null, onClickAction = nothing)
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ConsentTopBarPreview3() {
    TopBar("Hello", onClickBack = nothing)
}

private val nothing: () -> Unit = { }
