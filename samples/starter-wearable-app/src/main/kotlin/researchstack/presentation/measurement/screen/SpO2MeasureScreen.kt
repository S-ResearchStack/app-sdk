package researchstack.presentation.measurement.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import researchstack.R
import researchstack.presentation.component.AppButton
import researchstack.presentation.main.screen.getItemIcon
import researchstack.presentation.measurement.Route
import researchstack.presentation.measurement.viewmodel.SpO2MeasureViewModel
import researchstack.presentation.theme.Blue100
import researchstack.presentation.theme.ItemHomeColor
import researchstack.presentation.theme.TextColor
import researchstack.presentation.theme.Typography

@Composable
fun SpO2MeasureScreen(
    navController: NavHostController,
    spO2MeasureViewModel: SpO2MeasureViewModel = hiltViewModel()
) {
    when (spO2MeasureViewModel.measureState.observeAsState(SpO2MeasureViewModel.SpO2MeasureState.Measuring).value) {
        SpO2MeasureViewModel.SpO2MeasureState.Measuring -> {
            MeasureStateView(stringResource(id = R.string.measuring), spO2MeasureViewModel)
        }

        SpO2MeasureViewModel.SpO2MeasureState.Motion -> {
            MeasureStateView(stringResource(id = R.string.stay_still), spO2MeasureViewModel)
        }

        SpO2MeasureViewModel.SpO2MeasureState.GuideAgain -> {
            SpO2GuidesDuringMeasurement(spO2MeasureViewModel)
        }

        SpO2MeasureViewModel.SpO2MeasureState.Completed -> {
            MeasureSuccessResult(
                spO2MeasureViewModel,
                navController
            )
        }
        SpO2MeasureViewModel.SpO2MeasureState.Initial -> {
        }
        SpO2MeasureViewModel.SpO2MeasureState.Failed -> {
            MeasureFailedResult(
                spO2MeasureViewModel,
                navController
            )
        }
    }

    val currentView = LocalView.current
    DisposableEffect(null) {
        currentView.keepScreenOn = true
        onDispose {
            spO2MeasureViewModel.stopTracking()
            currentView.keepScreenOn = false
        }
    }
}

@Composable
fun MeasureSuccessResult(
    spO2MeasureViewModel: SpO2MeasureViewModel,
    navController: NavHostController,
) {
    spO2MeasureViewModel.stopTracking()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.blood_oxygen),
            color = TextColor,
            style = Typography.title3,
            textAlign = TextAlign.Center
        )
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = researchstack.presentation.main.screen.HomeScreenItem.BLOOD_OXYGEN.getItemIcon()),
                contentDescription = null,
                modifier = Modifier.size(
                    24.dp
                )
            )
        }
        Row(
            modifier = Modifier.height(72.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            spO2MeasureViewModel.spO2Value.observeAsState().value.let { it ->
                Text(text = it.toString(), style = Typography.display2, textAlign = TextAlign.Center)
            }
            Text(text = "%", style = Typography.title1, modifier = Modifier.padding(top = 9.dp))
        }
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            navController.navigate(Route.Main.name) {
                popUpTo(Route.Main.name) { inclusive = true }
            }
        }
    }
}

@Composable
fun MeasureFailedResult(
    spO2MeasureViewModel: SpO2MeasureViewModel,
    navController: NavHostController,
) {
    spO2MeasureViewModel.stopTracking()
    Column(
        Modifier
            .fillMaxSize()
            .padding(0.dp, 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = R.string.blood_oxygen),
            color = TextColor,
            style = Typography.title3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(id = R.string.spo2_measure_fail), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            navController.navigate(Route.Main.name) {
                popUpTo(Route.Main.name) { inclusive = true }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MeasureStateView(stateMessage: String, spO2MeasureViewModel: SpO2MeasureViewModel) {
    val color = Color.Blue
    CircularProgressIndicator(
        progress = spO2MeasureViewModel.progress.observeAsState(initial = 0f).value,
        modifier = Modifier.fillMaxSize(),
        startAngle = 270f,
        endAngle = 270f,
        indicatorColor = color,
        trackColor = color.copy(alpha = 0.3f),
        strokeWidth = 4.dp,
    )
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.height(72.dp)) {
            Image(
                painter = painterResource(id = researchstack.presentation.main.screen.HomeScreenItem.BLOOD_OXYGEN.getItemIcon()),
                contentDescription = null,
                modifier = Modifier.size(
                    64.dp
                )
            )
        }
        Text(text = stateMessage)
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SpO2GuidesDuringMeasurement(spO2MeasureViewModel: SpO2MeasureViewModel) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.spo2_higher_on_wrist),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.spo2_guide_again_move_watch_higher_on_wrist), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.spo2_guide_again_watch_on_top_of_wrist), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.spo2_wrist_near_heart),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.spo2_guide_again_wrist_near_heart), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.spo2_guide_again_not_move_or_talking), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.spo2_guide_again_warm_up_your_skin), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(Blue100, stringResource(id = R.string.ok)) {
            spO2MeasureViewModel.reMeasuring()
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
