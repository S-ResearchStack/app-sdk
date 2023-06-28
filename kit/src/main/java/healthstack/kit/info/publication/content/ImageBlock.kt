package healthstack.kit.info.publication.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import healthstack.kit.R

class ImageBlock(
    private val images: List<String>
) : ContentBlock() {
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun Render() {
        val context = LocalContext.current

        if (images.size > 1) {
            val pagerState = rememberPagerState()
            HorizontalPager(
                count = images.size,
                modifier = Modifier
                    .aspectRatio(3 / 4f)
                    .fillMaxWidth(),
                state = pagerState
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val model = ImageRequest.Builder(context)
                        .data(images[page])
                        .size(Size.ORIGINAL)
                        .build()
                    Image(
                        painter = rememberAsyncImagePainter(model),
                        contentDescription = "Image $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            PageIndicator(pages = images.lastIndex, current = pagerState.currentPage)
        } else {
            val model = ImageRequest.Builder(context)
                .data(images[0])
                .size(Size.ORIGINAL)
                .build()
            Image(
                painter = rememberAsyncImagePainter(model),
                contentDescription = "Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
    }

    @Composable
    fun PageIndicator(pages: Int, current: Int) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (page in 0..pages) {
                val isCurrent = page == current
                Spacer(modifier = Modifier.width(if (isCurrent) 1.dp else 2.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(
                            if (isCurrent) R.drawable.tab_icon_selected else R.drawable.tab_icon_unselected
                        ),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(if (isCurrent) 1.dp else 2.dp))
            }
        }
    }
}
