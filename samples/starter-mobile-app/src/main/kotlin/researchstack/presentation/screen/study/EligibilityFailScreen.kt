package researchstack.presentation.screen.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import researchstack.R
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.AppTextButton
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route.Main
import researchstack.presentation.theme.AppTheme

@Composable
fun EligibilityFailScreen() {
    val scrollState = rememberScrollState()
    val navController = LocalNavController.current
    Scaffold(
        bottomBar = {
            Row(modifier = Modifier.padding(20.dp)) {
                AppTextButton(text = stringResource(id = R.string.back_to_home)) {
                    navController.navigate("${Main.name}/${MainPage.Study.ordinal}") { popUpTo(0) }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(innerPadding),
        ) {
            Image(
                painter = painterResource(R.drawable.study_image),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "참여 불가능",
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "자격 없음",
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.onSurface
                )
            }
        }
    }
}
