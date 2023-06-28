package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.ImageChoiceQuestionModel
import healthstack.kit.theme.AppTheme

class ImageQuestionComponent : QuestionComponent<ImageChoiceQuestionModel>() {
    @Composable
    override fun Render(model: ImageChoiceQuestionModel, callbackCollection: CallbackCollection) {
        super.Render(model, callbackCollection)

        Spacer(modifier = Modifier.height(20.dp))

        if (model.isMulti) MultiChoiceImageGroup(model)
        else SingleChoiceImageGroup(model)
    }

    @Composable
    private fun SingleChoiceImageGroup(model: ImageChoiceQuestionModel) =
        Column(modifier = Modifier.fillMaxWidth()) {
            val selection = remember { mutableStateOf(model.selection) }

            LazyVerticalGrid(
                columns = Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(600.dp)
            ) {
                itemsIndexed(model.candidates) { index, candidate ->
                    val onClick = {
                        if (selection.value == index) {
                            model.selection = null
                            selection.value = model.selection
                        } else {
                            model.selection = index
                            selection.value = model.selection
                        }
                    }
                    if (model.labels.isNullOrEmpty())
                        ImageCard(
                            imagePath = candidate,
                            selected = selection.value == index,
                            onClick = onClick
                        )
                    else
                        ImageWithLabelCard(
                            label = model.labels[index],
                            imagePath = candidate,
                            selected = selection.value == index,
                            onClick = onClick
                        )
                }
            }
        }

    @Composable
    private fun MultiChoiceImageGroup(model: ImageChoiceQuestionModel) =
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyVerticalGrid(
                columns = Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(600.dp)
            ) {
                itemsIndexed(model.candidates) { index, candidate ->
                    val checkedState = remember(model.id + index) {
                        mutableStateOf(model.isSelected(index))
                    }
                    val onClick = {
                        val newState = !checkedState.value
                        checkedState.value = newState
                        if (newState) model.select(index)
                        else model.deselect(index)
                    }

                    if (model.labels.isNullOrEmpty())
                        ImageCard(
                            imagePath = candidate,
                            selected = checkedState.value,
                            onClick = onClick
                        )
                    else
                        ImageWithLabelCard(
                            label = model.labels[index],
                            imagePath = candidate,
                            selected = checkedState.value,
                            onClick = onClick
                        )
                }
            }
        }

    @Composable
    private fun ImageCard(
        imagePath: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) =
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(190.dp)
                .clickable {
                    onClick()
                }
                .testTag(imagePath),
            elevation = if (selected) 5.dp else 0.dp,
            shape = RoundedCornerShape(4.dp),
            border = if (selected) BorderStroke(2.dp, AppTheme.colors.primary) else null
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
        }

    @Composable
    private fun ImageWithLabelCard(
        label: String,
        imagePath: String,
        selected: Boolean,
        onClick: () -> Unit,
    ) =
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(190.dp)
                .clickable {
                    onClick()
                }
                .testTag(imagePath),
            elevation = if (selected) 5.dp else 0.dp,
            shape = RoundedCornerShape(4.dp),
            border = if (selected) BorderStroke(2.dp, AppTheme.colors.primary)
            else BorderStroke(1.dp, AppTheme.colors.primaryVariant)
        ) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.height(46.dp).fillMaxWidth().padding(8.dp),
                    text = label,
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.onBackground,
                    maxLines = 2
                )
                AsyncImage(
                    model = imagePath,
                    modifier = Modifier.width(150.dp).height(144.dp),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        }
}
