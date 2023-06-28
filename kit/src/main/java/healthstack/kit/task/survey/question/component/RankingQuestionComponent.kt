package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.RankingQuestionModel
import healthstack.kit.theme.AppTheme
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

class RankingQuestionComponent : QuestionComponent<RankingQuestionModel>() {

    private val modifier = Modifier.height(56.dp)

    @Composable
    override fun Render(model: RankingQuestionModel, callbackCollection: CallbackCollection) {
        Column(
            Modifier.background(AppTheme.colors.background),
        ) {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))
            RankingGroup(model, modifier)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun RankingGroup(model: RankingQuestionModel, modifier: Modifier) {
        var candidates by remember { mutableStateOf(model.candidates) }
        candidates = model.candidates

        val state = rememberReorderableLazyListState(onMove = { from, to ->
            candidates = candidates.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        })

        model.selection = candidates.toString()

        LazyColumn(
            state = state.listState,
            modifier = Modifier
                .height(600.dp)
                .reorderable(state)
                .detectReorderAfterLongPress(state)
                .background(AppTheme.colors.background)
                .testTag("ranking"),
        ) {
            items(candidates, { it }) { value ->
                ReorderableItem(
                    state,
                    key = value,
                    modifier = modifier,
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
                                .padding(horizontal = 16.dp)
                                .animateItemPlacement(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = CenterVertically,
                        ) {
                            Text(
                                text = value,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(0.8F),
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
        }
    }
}
