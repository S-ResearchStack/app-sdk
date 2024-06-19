package healthstack.wearable.kit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import healthstack.wearable.kit.R
import healthstack.wearable.kit.theme.HomeScreenItemBackground
import healthstack.wearable.kit.theme.SubTextColor
import healthstack.wearable.kit.theme.TextColor

@Composable
fun MeasurementButton(dataType: String, lastMeasureTime: String, onClick: (String) -> Unit) {
    val iconId = when (dataType) {
        "ECG" -> R.drawable.health_ecg
        else -> TODO()
    }

    val titleId = when (dataType) {
        "ECG" -> R.string.ecg
        else -> TODO()
    }

    Row(
        Modifier
            .fillMaxWidth(0.9f)
            .height(57.dp)
            .padding(top = 8.dp)
            .background(HomeScreenItemBackground, RoundedCornerShape(26))
            .clickable { onClick(dataType) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = stringResource(id = titleId),
                color = TextColor,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.width(4.dp))
            lastMeasureTime.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        color = SubTextColor,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}
