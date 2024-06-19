package healthstack.wearable.kit.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import healthstack.wearable.kit.R
import healthstack.wearable.kit.component.MeasurementButton
import healthstack.wearable.kit.theme.TextColor

class HomeScreen(private val lastMeasureTime: String, private val healthDataList: List<String>) {
    @Composable
    fun Render(onClick: (String) -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            dimensionResource(id = R.dimen.cardview_compat_inset_shadow)
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.app_name_wearable),
                color = TextColor,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.app_message),
                textAlign = TextAlign.Center,
                color = TextColor,
                fontSize = 16.sp,
            )
            healthDataList.forEach {
                MeasurementButton(it, lastMeasureTime, onClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(45.dp))
        }
    }
}
