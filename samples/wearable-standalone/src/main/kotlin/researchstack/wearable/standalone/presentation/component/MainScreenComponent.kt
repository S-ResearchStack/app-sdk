package researchstack.wearable.standalone.presentation.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.theme.BackgroundButtonGray
import researchstack.wearable.standalone.presentation.theme.LastMeasureText
import researchstack.wearable.standalone.presentation.theme.TextColor
import researchstack.wearable.standalone.presentation.theme.Typography

@Composable
fun MainScreenComponent(
    title: String,
    @DrawableRes icon: Int,
    @StringRes content: Int,
    showMeasureBtn: Boolean = true,
    lastMeasurementTime: String?,
    showCalibrateBtn: Boolean = false,
    onClickMeasure: () -> Unit,
    onClickCalibrate: () -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = title,
            color = TextColor,
            style = Typography.body1
        )
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            stringResource(id = content),
            color = TextColor,
            textAlign = TextAlign.Center,
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (showMeasureBtn) {
            Box(
                Modifier
                    .background(BackgroundButtonGray, RoundedCornerShape(50))
                    .width(100.dp)
                    .height(32.dp)
                    .clickable {
                        onClickMeasure()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.measure),
                    color = TextColor,
                    style = Typography.body1.copy(fontSize = 15.sp)
                )
            }
        }

        lastMeasurementTime?.let { value ->
            if (value.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(id = R.string.last_measure),
                    color = LastMeasureText,
                    fontSize = 12.sp
                )
                Text(
                    text = value,
                    color = LastMeasureText,
                    fontSize = 12.sp
                )
                if (!showCalibrateBtn) {
                    Spacer(modifier = Modifier.height(53.dp))
                }
            }
        }
        if (showCalibrateBtn) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                Modifier
                    .background(BackgroundButtonGray, RoundedCornerShape(50))
                    .width(100.dp)
                    .height(32.dp)
                    .clickable {
                        onClickCalibrate()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.calibrate),
                    color = TextColor,
                    style = Typography.body1.copy(fontSize = 15.sp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}
