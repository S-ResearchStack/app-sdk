package healthstack.kit.task.onboarding.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.IntroModel
import healthstack.kit.task.onboarding.model.IntroModel.IntroSection
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomSquareButton

class IntroView(
    val bottomBarText: String,
) : View<IntroModel>() {
    @Composable
    override fun Render(
        model: IntroModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val scrollState = rememberScrollState()
        val joinButtonText = bottomBarText

        Scaffold(
            bottomBar = {
                BottomSquareButton(
                    text = joinButtonText
                ) { callbackCollection.next() }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppTheme.colors.background)
                    .verticalScroll(scrollState),
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
                                .height(300.dp)
                        ) {
                            model.drawableId?.let { drawableId ->
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.TopCenter)
                                        .height(214.dp),
                                    painter = painterResource(drawableId),
                                    contentDescription = "This is image of drawableId",
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 20.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                model.logoDrawableId?.let {
                                    Image(
                                        modifier = Modifier
                                            .height(100.dp)
                                            .width(100.dp)
                                            .padding(top = 20.dp)
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
                                    color = AppTheme.colors.textSecondary,
                                )

                                Spacer(Modifier.size(20.dp))

                                model.summaries?.let { Summary(it) }
                            }
                        }
                    }
                }

                IntroSections(model.sections)
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
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textPrimary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

@Composable
fun IntroSections(sections: List<IntroModel.IntroSection>) =
    sections.forEach { (title, description) ->
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 24.dp)
                .wrapContentHeight()
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
                style = AppTheme.typography.body2,
                color = AppTheme.colors.textPrimary
            )
        }
    }

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun SummaryPreview() =
    Summary(
        listOf(
            R.drawable.ic_watch to "Wear your\nwatch",
            R.drawable.ic_alert to "10 min\na day",
            R.drawable.ic_home_task to "2 surveys\na week"
        )
    )

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun IntroViewPreview() {
    val introView = IntroView("Get Started")
    return introView.Render(
        IntroModel(
            "intro",
            "CardioFlow",
            R.drawable.sample_image_alpha3,
            R.drawable.ic_launcher,
            summaries = listOf(
                R.drawable.ic_watch to "Wear your\nwatch",
                R.drawable.ic_alert to "10 min\na day",
                R.drawable.ic_home_task to "2 surveys\na week"
            ),
            sections = listOf(
                IntroSection(
                    "Overview",
                    "CardioFlow is a study developed by the University of California, San Francisco.\n" +
                        "\n" +
                        "Through this study, we identify and " +
                        "measure the data of your vital signs and symptom reports.\n" +
                        "\n" +
                        "With your help, we could test our algorithms and develop technology that contributes " +
                        "to preventing cardiovascular diseases in the U.S.",
                ),
                IntroSection(
                    "How to participate",
                    "Wear the watch as much as possible and " +
                        "take active measurements 3 times a day when notified."
                )
            )
        ),
        CallbackCollection(),
        null
    )
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun DescriptionPreview() =
    IntroSections(
        listOf(
            IntroModel.IntroSection(
                "Description 1",
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                    "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                    "in voluptate velit esse cillum dolore eu fugiat nulla pariatur. " +
                    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia"
            )
        )
    )
