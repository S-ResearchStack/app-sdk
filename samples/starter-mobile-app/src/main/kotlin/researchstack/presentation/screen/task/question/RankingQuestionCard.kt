package researchstack.presentation.screen.task.question

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import researchstack.domain.model.task.question.RankQuestion
import researchstack.presentation.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RankingQuestionCard(question: RankQuestion, onChangedResult: (String) -> Unit) {
    var candidates by remember { mutableStateOf(question.options.map { it.value }) }

    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            candidates = candidates.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
            onChangedResult("")
            TODO("handle onChangedResult")
        }
    )

    LazyColumn(
        state = state.listState,
        modifier = modifier(state),
    ) {
        items(candidates, { it }) { value ->
            ReorderableItemRow(state, value)
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LazyItemScope.ReorderableItemRow(
    state: ReorderableLazyListState,
    value: String,
) {
    ReorderableItem(
        state,
        key = value,
    ) { selected ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (selected) {
                        return@let it.background(
                            AppTheme.colors.primaryVariant,
                            RoundedCornerShape(4.dp)
                        )
                    }
                    it
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .animateItemPlacement(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically,
            ) {
                Text(
                    text = value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(@Suppress("MagicNumber") 0.8F),
                    color = if (selected) AppTheme.colors.primary else AppTheme.colors.onSurface,
                    style = if (selected) AppTheme.typography.title2 else AppTheme.typography.body1
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .let {
                            if (selected) return@let it.background(AppTheme.colors.primary)
                            it.background(AppTheme.colors.primaryVariant)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        tint = if (selected) AppTheme.colors.onPrimary else AppTheme.colors.primary,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun modifier(state: ReorderableLazyListState) = Modifier
    // TODO calculate height size
    .height(100.dp)
    .reorderable(state)
    .detectReorderAfterLongPress(state)
    .background(AppTheme.colors.background)
    .testTag("ranking")
