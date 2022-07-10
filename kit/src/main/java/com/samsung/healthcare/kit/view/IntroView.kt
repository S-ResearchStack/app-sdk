package com.samsung.healthcare.kit.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.IntroModel
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.BottomBarWithGradientBackground

class IntroView(
    val bottomBarText: String? = null,
) : View<IntroModel>() {
    @Composable
    override fun Render(
        model: IntroModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val scrollState = rememberScrollState()
        val joinButtonText = bottomBarText ?: LocalContext.current.getString(R.string.intro_button_text)

        Scaffold(
            bottomBar = {
                BottomBarWithGradientBackground(
                    text = joinButtonText
                ) { callbackCollection.next() }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            model.drawableId?.let { drawableId ->
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                        .height(200.dp),
                                    painter = painterResource(drawableId),
                                    contentDescription = "This is image of drawableId",
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                model.logoDrawableId?.let {
                                    Image(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(100.dp)
                                            .shadow(
                                                elevation = 50.dp,
                                                shape = CircleShape,
                                                clip = false
                                            ),
                                        painter = painterResource(model.logoDrawableId),
                                        contentDescription = "This is image of logoDrawableId",
                                        contentScale = ContentScale.Fit
                                    )
                                }
                                Text(
                                    text = model.title,
                                    style = AppTheme.typography.appTitle,
                                    color = Color(0xff130C00),
                                )
                            }
                        }
                    }
                }

                model.summaries?.let { Summary(it) }

                Description(model.descriptions)
            }
        }
    }
}

@Composable
fun Summary(summaries: List<Pair<Int, String>>) =
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        summaries.forEach { (icon, message) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Image(
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(icon),
                    contentDescription = "This is image of summaries",
                    contentScale = ContentScale.Fit
                )
                Text(
                    modifier = Modifier.width(80.dp),
                    text = message,
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.textPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

@Composable
fun Description(descriptions: List<Pair<String, String>>) =
    descriptions.forEach { (title, description) ->
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                style = AppTheme.typography.subHeader2,
                color = AppTheme.colors.textPrimary
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = description,
                style = AppTheme.typography.body1,
                color = AppTheme.colors.textPrimary
            )
        }
    }

@Preview(showBackground = true)
@Composable
fun DescriptionPreview() =
    Description(
        listOf(
            "Description 1" to "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia",
            "Description 2" to "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia",
        )
    )

@Preview(showBackground = true)
@Composable
fun SummaryPreview() =
    Summary(
        listOf(
            R.drawable.ic_watch to "Wear your watch",
            R.drawable.ic_clock to "10 min a day",
            R.drawable.ic_alarm to "2 surveys a week"
        )
    )
