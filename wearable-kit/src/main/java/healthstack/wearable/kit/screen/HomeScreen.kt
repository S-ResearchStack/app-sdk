package healthstack.wearable.kit.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import healthstack.wearable.kit.R
import healthstack.wearable.kit.theme.HomeScreenItemBackground
import healthstack.wearable.kit.theme.SubTextColor
import healthstack.wearable.kit.theme.TextColor

class HomeScreen(private val lastMeasureTime: String) {
    @Composable
    fun Render(onClick: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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
            Row(
                Modifier
                    .fillMaxWidth(0.9f)
                    .height(57.dp)
                    .padding(top = 8.dp)
                    .background(HomeScreenItemBackground, RoundedCornerShape(26))
                    .clickable {
                        onClick()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Image(
                    painter = painterResource(id = R.drawable.health_ecg),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.ecg),
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
            Spacer(modifier = Modifier.height(53.dp))
        }
    }
}
