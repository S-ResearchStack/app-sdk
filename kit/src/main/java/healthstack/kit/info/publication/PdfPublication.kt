package healthstack.kit.info.publication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState
import healthstack.kit.R
import healthstack.kit.info.publication.content.ContentBlock
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar

class PdfPublication(
    coverContent: String?,
    category: String,
    title: String,
    description: String,
    contents: List<ContentBlock>,
) : Publication(coverContent, category, title, description, contents) {
    @Composable
    override fun Render(
        onClick: (Publication?) -> Unit,
    ) {
        val pdfState = rememberVerticalPdfReaderState(
            resource = ResourceType.Remote(coverContent!!),
            isZoomEnable = true
        )
        var pdfView by remember { mutableStateOf(false) }

        if (!pdfView) {
            val scrollState = rememberScrollState()

            Scaffold(
                topBar = {
                    TopBar("Education") {
                        onClick(null)
                    }
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .verticalScroll(scrollState),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(172.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(122.dp)
                                .background(AppTheme.colors.primary.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_education_pdf),
                                contentDescription = "Default Image",
                                modifier = Modifier.size(56.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text(
                                    text = category,
                                    color = AppTheme.colors.onBackground.copy(0.6f),
                                    textAlign = TextAlign.Left,
                                    style = AppTheme.typography.title3
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = title,
                                    color = AppTheme.colors.onBackground,
                                    textAlign = TextAlign.Left,
                                    style = AppTheme.typography.headline3
                                )
                            }
                            OutlinedButton(
                                onClick = { pdfView = true },
                                modifier = Modifier
                                    .width(164.dp)
                                    .height(40.dp)
                                    .align(Alignment.BottomStart),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, AppTheme.colors.primary)
                            ) {
                                Text(
                                    text = "View PDF",
                                    color = AppTheme.colors.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    style = AppTheme.typography.title2
                                )
                            }
                        }
                    }
                    for (block in contents) {
                        Spacer(modifier = Modifier.height(24.dp))
                        block.Render()
                    }
                    this@PdfPublication.RelatedContent()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else {
            Scaffold(
                topBar = {
                    TopBar("PDF") {
                        pdfView = false
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    VerticalPDFReader(
                        state = pdfState,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(AppTheme.colors.onBackground)
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        if (!pdfState.isLoaded) {
                            LinearProgressIndicator(
                                progress = pdfState.loadPercent / 100f,
                                color = AppTheme.colors.primary,
                                backgroundColor = AppTheme.colors.disabled,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("PDF Progress Indicator")
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .height(30.dp)
                                    .padding(horizontal = 24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AppTheme.colors.onBackground.copy(0.8f)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${pdfState.currentPage} of ${pdfState.pdfPageCount}",
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .testTag("PDF Page Count"),
                                    color = AppTheme.colors.surface,
                                    style = AppTheme.typography.body3
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun PreviewImage() {
        Image(
            painter = painterResource(R.drawable.ic_education_pdf),
            contentDescription = "Default Image",
            modifier = Modifier.size(56.dp),
            contentScale = ContentScale.Fit
        )
    }
}
