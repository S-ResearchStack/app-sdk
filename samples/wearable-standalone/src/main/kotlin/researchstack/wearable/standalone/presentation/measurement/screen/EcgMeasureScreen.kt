package researchstack.wearable.standalone.presentation.measurement.screen

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.AppButton
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.viewmodel.EcgMeasureViewModel
import researchstack.wearable.standalone.presentation.measurement.viewmodel.EcgMeasureViewModel.MeasureState
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.getOrientation
import researchstack.wearable.standalone.presentation.theme.Blue100
import researchstack.wearable.standalone.presentation.theme.ItemHomeColor
import researchstack.wearable.standalone.presentation.theme.Orange
import researchstack.wearable.standalone.presentation.theme.TitleGray
import researchstack.wearable.standalone.presentation.theme.Typography

@Composable
fun EcgMeasureScreen(
    navController: NavHostController,
    ecgViewModel: EcgMeasureViewModel = hiltViewModel()
) {
    when (ecgViewModel.measureState.observeAsState(MeasureState.Initial).value) {
        MeasureState.Initial -> {
            InitialState()
            ecgViewModel.startTrackingEcg()
        }

        MeasureState.Measuring -> {
            MeasureStateView(stringResource(id = R.string.measuring), ecgViewModel)
        }

        MeasureState.Motion -> {
            MeasureStateView(stringResource(id = R.string.stay_still), ecgViewModel)
        }

        MeasureState.Completed -> {
            MeasureResult(
                stringResource(id = R.string.measuring_complete),
                ecgViewModel,
                navController
            )
        }

        MeasureState.Failed -> {
            MeasureResult(
                stringResource(id = R.string.ecg_measure_fail),
                ecgViewModel,
                navController
            )
        }

        MeasureState.MotionOverLoad -> {
            MotionOverLoad(ecgViewModel)
        }
    }

    val currentView = LocalView.current
    DisposableEffect(null) {
        currentView.keepScreenOn = true
        onDispose {
            ecgViewModel.stopTracking()
            currentView.keepScreenOn = false
        }
    }
}

@Composable
fun MotionOverLoad(ecgViewModel: EcgMeasureViewModel) {
    val drawable = when (getOrientation(LocalContext.current)) {
        Configuration.ORIENTATION_LANDSCAPE -> listOf(
            R.drawable.ecg_help_try_again_01_r,
            R.drawable.ecg_help_try_again_02_r,
            R.drawable.ecg_help_try_again_03_r
        )

        else -> listOf(
            R.drawable.ecg_help_try_again_01_l,
            R.drawable.ecg_help_try_again_02_l,
            R.drawable.ecg_help_try_again_03_l
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
                contentScale = ContentScale.FillWidth
            )
            Text(text = stringResource(id = R.string.guide_message), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.ecg_guide_1), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.ecg_guide_2), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(Blue100, stringResource(id = R.string.ok)) {
                ecgViewModel.pushState(MeasureState.Initial)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MeasureResult(
    message: String,
    ecgViewModel: EcgMeasureViewModel,
    navController: NavHostController,
) {
    ecgViewModel.stopTracking()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.ecg),
            color = TitleGray,
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Text(text = message, textAlign = TextAlign.Center)
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            navController.navigate(Route.Main.name) {
                popUpTo(Route.Main.name) { inclusive = true }
            }
        }
    }
}

@Composable
fun MeasureStateView(stateMessage: String, ecgViewModel: EcgMeasureViewModel) {
    ecgViewModel.remainPercent.observeAsState().value?.let {
        CircularProgressIndicator(
            progress = it.toFloat() / 100,
            Modifier.fillMaxSize(),
            indicatorColor = Orange
        )
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = it.toString(), style = Typography.title1)

                Text(text = "%", modifier = Modifier.padding(bottom = 3.dp))
            }
            Box(Modifier.height(120.dp), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.health_ecg), contentDescription = null, modifier = Modifier.size(60.dp))
            }
            Text(text = stateMessage)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InitialState() {
    val screenOrientation = getOrientation(LocalContext.current)
    Log.i("InitialState", "InitialState: $screenOrientation")
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.ecg), color = TitleGray, style = Typography.body1)

        val bitmap = when (screenOrientation) {
            Configuration.ORIENTATION_LANDSCAPE ->
                ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_left_key)
                    ?.toBitmap()
                    ?.asImageBitmap()

            else -> ContextCompat.getDrawable(LocalContext.current, R.drawable.arrow_right_key)
                ?.toBitmap()
                ?.asImageBitmap()
        }
        Column(
            Modifier
                .fillMaxSize()
                .drawWithCache {
                    onDrawBehind {
                        val offset = when (screenOrientation) {
                            Configuration.ORIENTATION_LANDSCAPE ->
                                Offset(16f, size.height / 2 + 20f)

                            else ->
                                Offset(size.width / 2 + 144f, size.height / 2 - 144f)
                        }
                        if (bitmap != null) {
                            drawImage(
                                bitmap,
                                topLeft = offset
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
