package researchstack.wearable.standalone.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.theme.Blue100

@Composable
fun GuideComponent(drawable: Int, title: String, navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Text(text = stringResource(id = R.string.guide_message), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            AppButton(Blue100, stringResource(id = R.string.ok)) {
                navController.navigate(Route.Measure.name)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
