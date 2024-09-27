package researchstack.wearable.standalone.presentation.measurement.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.AppButton
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.main.screen.getItemIcon
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.viewmodel.PpgMeasureViewModel
import researchstack.wearable.standalone.presentation.theme.ItemHomeColor
import researchstack.wearable.standalone.presentation.theme.Typography

@Composable
fun PpgMeasureScreen(
    navController: NavController,
    ppgType: String,
    ppgMeasureViewModel: PpgMeasureViewModel = hiltViewModel()
) {
    when (ppgMeasureViewModel.measureState.collectAsState().value) {
        PpgMeasureViewModel.MeasureState.None -> {
            ppgMeasureViewModel.measurePpgData(ppgType)
        }

        PpgMeasureViewModel.MeasureState.Measuring -> {
            MeasureState(ppgMeasureViewModel, ppgType)
        }

        PpgMeasureViewModel.MeasureState.Completed -> {
            MeasureResult(ppgMeasureViewModel, navController, ppgType)
        }
    }
}

@Composable
private fun MeasureResult(
    ppgMeasureViewModel: PpgMeasureViewModel,
    navController: NavController,
    ppgType: String
) {
    LaunchedEffect(null) {
        ppgMeasureViewModel.stopTracking()
    }
    val text = if (ppgType == HomeScreenItem.PPG_IR.name) {
        stringResource(id = R.string.ppg_ir)
    } else stringResource(id = R.string.ppg_red)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, color = Color.White, style = Typography.body1)
        Text(
            text = stringResource(id = R.string.measuring_complete),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        AppButton(bgColor = ItemHomeColor, stringResource(id = R.string.ok)) {
            navController.navigate(Route.Main.name) {
                popUpTo(Route.Main.name) { inclusive = true }
            }
        }
    }
}

@Composable
private fun MeasureState(
    ppgMeasureViewModel: PpgMeasureViewModel,
    ppgType: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            ppgMeasureViewModel.timer.observeAsState(0).value.let {
                Text(
                    text = it.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
            }

            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = "s",
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(
                id = if (ppgType == HomeScreenItem.PPG_IR.name) {
                    HomeScreenItem.PPG_IR.getItemIcon()
                } else HomeScreenItem.PPG_RED.getItemIcon()
            ),
            contentDescription = null,
            modifier = Modifier.size(
                64.dp
            )
        )
        Spacer(modifier = Modifier.height(28.dp))
        AppButton(bgColor = ItemHomeColor, title = stringResource(id = R.string.stop)) {
            ppgMeasureViewModel.updateState(PpgMeasureViewModel.MeasureState.Completed)
            ppgMeasureViewModel.sendPpgDataToMobile()
        }
    }
}
