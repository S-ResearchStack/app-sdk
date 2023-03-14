package healthstack.kit.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomBar
import healthstack.kit.ui.ButtonShape
import healthstack.kit.ui.TopBar

@Composable
fun ImageArticleLayout(
    topBarTitle: String,
    message: ImageArticleModel,
    buttonText: String,
    onClickBack: () -> Unit = {},
    onComplete: () -> Unit = {},
    buttonHidden: Boolean = false,
    buttonShape: ButtonShape = ButtonShape.ROUND,
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopBar(title = topBarTitle) {
                onClickBack()
            }
        },
        bottomBar = {
            if (!buttonHidden) {
                BottomBar(
                    text = buttonText,
                    buttonEnabled = true,
                    shape = buttonShape
                ) { onComplete() }
            }
        },
        backgroundColor = AppTheme.colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(innerPadding),
        ) {
            message.drawableId?.let { drawableId ->
                Image(
                    painter = painterResource(drawableId),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    message.title,
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    message.description,
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.onSurface
                )
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ImageArticleLayoutSquarePreview() =
    ImageArticleLayout(
        "Top Bar Title",
        ImageArticleModel(
            "id",
            "Title",
            "Description",
            R.drawable.sample_image_alpha1
        ),
        "Button Text"
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun ImageArticleLayoutRoundPreview() =
    ImageArticleLayout(
        "Top Bar Title",
        ImageArticleModel(
            "id",
            "Title",
            "Description",
            R.drawable.sample_image_alpha1
        ),
        "Button Text",
        buttonShape = ButtonShape.ROUND
    )
