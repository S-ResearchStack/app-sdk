package healthstack.kit.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.theme.AppTheme

data class TabContent(val title: String, val Render: @Composable () -> Unit = { Text(title) })

@Composable
fun HorizontalTab(
    tabContents: List<TabContent>,
) {
    val cur = remember { mutableStateOf(0) }

    Column(
        Modifier.fillMaxWidth()
            .background(AppTheme.colors.background),
    ) {
        TabRow(
            selectedTabIndex = cur.value,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            backgroundColor = AppTheme.colors.background,
            contentColor = AppTheme.colors.primary,
        ) {
            tabContents.forEachIndexed { index, content ->
                Tab(
                    text = {
                        Text(
                            text = content.title,
                            style = AppTheme.typography.title3,
                            color = if (cur.value == index) AppTheme.colors.primary
                            else AppTheme.colors.onSurface.copy(0.6F)
                        )
                    },
                    selected = cur.value == index,
                    onClick = { cur.value = index }
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        tabContents[cur.value].Render()
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun HorizontalTabPreview() =
    HorizontalTab(
        listOf(
            TabContent("Tab 1"),
            TabContent("Tab 2"),
            TabContent("Tab 3")
        )
    )
