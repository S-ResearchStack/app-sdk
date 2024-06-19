package healthstack.wearable.support

import android.content.res.Configuration.ORIENTATION_UNDEFINED
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import healthstack.common.MeasureState
import healthstack.common.model.PrivDataType
import healthstack.common.model.PrivDataType.ECG
import healthstack.wearable.kit.screen.EcgMainScreen
import healthstack.wearable.kit.screen.EcgMeasureScreen
import healthstack.wearable.kit.screen.HomeScreen
import healthstack.wearable.support.helper.getOrientation
import healthstack.wearable.support.viewmodel.EcgMainViewModel
import healthstack.wearable.support.viewmodel.EcgMeasureViewModel

@Composable
fun Router(
    healthDataList: List<PrivDataType>,
    ecgMainViewModel: EcgMainViewModel = hiltViewModel(),
    ecgMeasureViewModel: EcgMeasureViewModel = hiltViewModel(),
) {
    val startDest = Route.Home.name
    val navController = rememberNavController()

    var lastMeasurementTime by remember {
        mutableStateOf("")
    }
    var measureState by remember {
        mutableStateOf(MeasureState.Initial)
    }
    var orientation by remember {
        mutableStateOf(ORIENTATION_UNDEFINED)
    }
    var remainPercent by remember {
        mutableStateOf(0)
    }
    var measurementType by remember {
        mutableStateOf(PrivDataType.ECG)
    }

    ecgMainViewModel.lastMeasurementTime.observeAsState().value?.let {
        lastMeasurementTime = it
    }
    ecgMeasureViewModel.measureState.observeAsState(MeasureState.Initial).value.let {
        measureState = it
    }
    ecgMeasureViewModel.remainPercent.observeAsState().value?.let {
        remainPercent = it
    }
    orientation = getOrientation(LocalContext.current) ?: ORIENTATION_UNDEFINED

    val homeScreen = HomeScreen(lastMeasurementTime, healthDataList)
    val ecgMainScreen = EcgMainScreen(lastMeasurementTime)
    val ecgMeasureScreen = EcgMeasureScreen(
        measureState,
        orientation,
        remainPercent,
    )

    NavHost(
        navController = navController,
        startDestination = startDest,
    ) {
        composable(Route.Home.name) {
            homeScreen.Render {
                type ->
                measurementType = type
                navController.navigate(Route.Main.name)
            }
        }
        composable(Route.Main.name) {
            when (measurementType) {
                ECG -> ecgMainScreen.Render { navController.navigate(Route.Measure.name) }
            }
        }
        composable(Route.Measure.name) {
            when (measurementType) {
                ECG -> ecgMeasureScreen.Render(
                    onInitial = {
                        ecgMeasureViewModel.startTrackingEcg()
                    },
                    onMeasuring = {},
                    onCompleted = {
                        navController.navigate(Route.Main.name)
                    },
                    onOverLoad = {
                        ecgMeasureViewModel.pushState(MeasureState.Initial)
                    },
                    onDispose = {
                        ecgMeasureViewModel.stopTracking()
                    },
                )
            }
        }
    }
}
