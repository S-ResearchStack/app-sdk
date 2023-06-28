package healthstack.kit.info.publication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import healthstack.kit.info.publication.content.ContentBlock
import healthstack.kit.R
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar

class TextPublication(
    coverContent: String?,
    category: String,
    title: String,
    description: String,
    contents: List<ContentBlock>,
) : Publication(coverContent, category, title, description, contents) {
    @Composable
    override fun Render(
        onClick: (Publication?) -> Unit
    ) {
        val scrollState = rememberScrollState()
        val context = LocalContext.current

        Scaffold(
            topBar = {
                TopBar("Education") {
                    onClick(null)
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                if (coverContent != null) {
                    val model = ImageRequest.Builder(context)
                        .data(coverContent)
                        .size(Size.ORIGINAL)
                        .build()
                    Image(
                        painter = rememberAsyncImagePainter(model),
                        contentDescription = "Cover Image",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }
                Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = category,
                            color = AppTheme.colors.onBackground.copy(0.6f),
                            textAlign = TextAlign.Left,
                            style = AppTheme.typography.body3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = title,
                            color = AppTheme.colors.onBackground,
                            textAlign = TextAlign.Left,
                            style = AppTheme.typography.headline3
                        )
                        for (block in contents) {
                            Spacer(modifier = Modifier.height(24.dp))
                            block.Render()
                        }
                        this@TextPublication.RelatedContent()
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    @Composable
    override fun PreviewImage() {
        if (coverContent != null) {
            Image(
                painter = rememberAsyncImagePainter(coverContent),
                contentDescription = "Preview Image",
                modifier = Modifier
                    .height(98.dp)
                    .width(116.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(R.drawable.ic_education_default),
                contentDescription = "Default Image",
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
