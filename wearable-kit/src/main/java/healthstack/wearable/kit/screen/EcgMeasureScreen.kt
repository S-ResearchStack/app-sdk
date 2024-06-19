package healthstack.wearable.kit.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import healthstack.common.MeasureState
import healthstack.wearable.kit.R
import healthstack.wearable.kit.component.AppButton
import healthstack.wearable.kit.theme.Blue100
import healthstack.wearable.kit.theme.ItemHomeColor
import healthstack.wearable.kit.theme.Orange
import healthstack.wearable.kit.theme.TitleGray
import healthstack.wearable.kit.theme.Typography
import kotlinx.coroutines.delay

class EcgMeasureScreen(
    private val measureState: MeasureState,
    private val screenOrientation: Int,
    private val remainPercent: Int,
) {
    @Composable
    fun Render(
        onInitial: () -> Unit,
        onMeasuring: () -> Unit,
        onCompleted: () -> Unit,
        onOverLoad: () -> Unit,
        onDispose: () -> Unit,
    ) {
        when (measureState) {
            MeasureState.Initial -> {
                InitialState(screenOrientation)
                onInitial()
            }

            MeasureState.Measuring -> {
                MeasureStateView(stringResource(id = R.string.measuring), remainPercent)
                onMeasuring()
            }

            MeasureState.Motion -> {
                MeasureStateView(stringResource(id = R.string.stay_still), remainPercent)
                onMeasuring()
            }

            MeasureState.Completed -> {
                MeasureResult(
                    stringResource(id = R.string.measuring_complete),
                    onCompleted
                )
            }

            MeasureState.Failed -> {
                MeasureResult(
                    stringResource(id = R.string.ecg_measure_fail),
                    onCompleted
                )
            }

            MeasureState.MotionOverLoad -> {
                MotionOverLoad(screenOrientation, onOverLoad)
            }
        }

        val currentView = LocalView.current
        DisposableEffect(null) {
            currentView.keepScreenOn = true
            onDispose {
                onDispose()
                currentView.keepScreenOn = false
            }
        }
    }
}

@Composable
fun MotionOverLoad(screenOrientation: Int, onOverLoad: () -> Unit) {
    val drawable = when (screenOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> listOf(
            R.drawable.ecg_help_try_again_01_r,
            R.drawable.ecg_help_try_again_02_r,
            R.drawable.ecg_help_try_again_03_r,
        )

        else -> listOf(
            R.drawable.ecg_help_try_again_01_l,
            R.drawable.ecg_help_try_again_02_l,
            R.drawable.ecg_help_try_again_03_l,
        )
    }

    var stateImage by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(stateImage) {
        delay(500)
        stateImage = ++stateImage % 3
    }

    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = drawable[stateImage]),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Text(text = stringResource(id = R.string.guide_message), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.ecg_guide_1), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.ecg_guide_2), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(Blue100, stringResource(id = R.string.ok)) {
                onOverLoad()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MeasureResult(
    message: String,
    onCompleted: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.ecg),
            color = TitleGray,
            style = Typography.body1,
            textAlign = TextAlign.Center,
        )
        Text(text = message, textAlign = TextAlign.Center)
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            onCompleted()
        }
    }
}

@Composable
fun MeasureStateView(stateMessage: String, remainPercent: Int) {
    var dots by remember {
        mutableStateOf(3)
    }
    LaunchedEffect(dots) {
        delay(500)
        dots = ++dots % 3
    }

    remainPercent.let {
        CircularProgressIndicator(
            progress = it.toFloat().times(0.01).toFloat(),
            Modifier.fillMaxSize(),
            indicatorColor = Orange,
            trackColor = Color.DarkGray,
            strokeWidth = 5.dp,
        )
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {

            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = it.toString(), style = Typography.title1)
                Text(text = "%", modifier = Modifier.padding(bottom = 3.dp))
            }
            val string = StringBuilder()
            for (i in 0..dots) {
                string.append(".")
            }
            Spacer(modifier = Modifier.height(84.dp))
            Text(text = "$stateMessage $string")
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InitialState(screenOrientation: Int) {
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.ecg), color = TitleGray, style = Typography.body1)

        val bitmap = when (screenOrientation) {

            Configuration.ORIENTATION_LANDSCAPE -> ContextCompat.getDrawable(
                LocalContext.current,
                R.drawable.arrow_left_key
            )?.toBitmap()?.asImageBitmap()

            else -> ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_right_key)?.toBitmap()
                ?.asImageBitmap()
        }
        Column(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {
                        val offset = when (screenOrientation) {
                            Configuration.ORIENTATION_LANDSCAPE -> Offset(16f, size.height / 2 + 20f)

                            else -> Offset(size.width / 2 + 144f, size.height / 2 - 144f)
                        }
                        if (bitmap != null) {
                            drawImage(
                                bitmap, topLeft = offset
                            )
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = R.string.ecg_measure), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
