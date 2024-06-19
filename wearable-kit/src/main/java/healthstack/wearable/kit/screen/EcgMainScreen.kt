package healthstack.wearable.kit.screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import healthstack.wearable.kit.R
import healthstack.wearable.kit.theme.ItemHomeColor
import healthstack.wearable.kit.theme.TitleGray
import healthstack.wearable.kit.theme.Typography

class EcgMainScreen(private val lastMeasureTime: String) {
    @Composable
    fun Render(onClick: () -> Unit) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = stringResource(id = R.string.ecg), color = Color.White, style = Typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.health_ecg),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
            Text(
                stringResource(id = R.string.ecg_message),
                color = Color.White,
                textAlign = TextAlign.Center,
                style = Typography.caption1,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .background(ItemHomeColor, RoundedCornerShape(50))
                    .width(100.dp)
                    .height(32.dp)
                    .clickable {
                        onClick()
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(id = R.string.measure), color = Color.White,
                )
            }
            lastMeasureTime.let { value ->
                if (value != null && value.isNotBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(id = R.string.last_measure), color = TitleGray)
                    Text(
                        text = value,
                        color = TitleGray,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
