package researchstack.presentation.measurement.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import researchstack.R
import researchstack.presentation.component.AppButton
import researchstack.presentation.component.PaginationDot
import researchstack.presentation.measurement.Route
import researchstack.presentation.theme.Blue100

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpO2GuideScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page: Int ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                SPO2GuidePage(page, navController = navController)
            }
        }
    }
    PaginationDot(pagerState = pagerState)
}

@Composable
fun SPO2GuidePage(page: Int, navController: NavHostController) {
    var drawable by remember { mutableStateOf(R.drawable.spo2_higher_on_wrist) }
    var string by remember { mutableStateOf(R.string.spo2_guide_move_watch_higher_on_wrist) }
    when (page) {
        GuidePage.MOVE_WATCH_HIGHER_ON_WRIST.ordinal -> {
            drawable = R.drawable.spo2_higher_on_wrist
            string = R.string.spo2_guide_move_watch_higher_on_wrist
        }
        GuidePage.ELBOW_ON_TABLE.ordinal -> {
            drawable = R.drawable.spo2_elbow_on_table
            string = R.string.spo2_guide_elbow_on_table
        }
        GuidePage.WRIST_NEAR_HEART.ordinal -> {
            drawable = R.drawable.spo2_wrist_near_heart
            string = R.string.spo2_guide_wrist_near_heart
        }
        GuidePage.START_MEASURE.ordinal -> {
            string = R.string.spo2_guide_start_measuring
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (page != 3) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Row(
                modifier = Modifier.fillMaxHeight()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = string), textAlign = TextAlign.Center)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.height(102.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(id = string), textAlign = TextAlign.Center)
                }
                AppButton(Blue100, stringResource(id = R.string.ok)) {
                    navController.navigate(Route.Measure.name)
                }
            }
        }
    }
}

enum class GuidePage {
    MOVE_WATCH_HIGHER_ON_WRIST,
    ELBOW_ON_TABLE,
    WRIST_NEAR_HEART,
    START_MEASURE
}
