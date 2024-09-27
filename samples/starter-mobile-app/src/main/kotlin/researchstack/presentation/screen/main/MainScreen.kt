package researchstack.presentation.screen.main

import android.Manifest
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.presentation.PermissionChecker
import researchstack.presentation.screen.insight.InsightScreen
import researchstack.presentation.screen.log.LogScreen
import researchstack.presentation.screen.study.StudyListScreen
import researchstack.presentation.screen.task.TaskListScreen
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.util.ignoreBatteryOptimization
import researchstack.presentation.viewmodel.UISettingViewModel

enum class BottomPager(@StringRes val titleId: Int, val imageVectorId: Int) {
    StudyList(R.string.home_study_list, R.drawable.home_study_list),
    TaskList(R.string.home_task_list, R.drawable.home_task_list),
    Data(R.string.home_data, R.drawable.home_data),
    Log(R.string.log_page, R.drawable.ic_walk)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    page: Int = 0,
    hiddenPageViewModel: UISettingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val permissions = mutableListOf(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.RECORD_AUDIO,
        // NOTE: Not Supported Yet
        // Manifest.permission.ACCESS_COARSE_LOCATION,
        // Manifest.permission.ACCESS_FINE_LOCATION
    )
    if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) permissions.add(Manifest.permission.POST_NOTIFICATIONS)
    PermissionChecker(permissions = permissions) {}
    LaunchedEffect(key1 = "pm") {
        ignoreBatteryOptimization(context)
    }

    val coroutineScope = rememberCoroutineScope()
    val logPageEnabled = hiddenPageViewModel.logPageEnabled.collectAsState().value

    val pageSize = if (logPageEnabled) BottomPager.values().size else BottomPager.values().size - 1
    val pagerState = rememberPagerState(page) { pageSize }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                backgroundColor = AppTheme.colors.background,
            ) {
                BottomPager.values().forEach { page ->
                    if (page.ordinal < pageSize) {
                        BottomPageItem(page, pagerState.currentPage) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page.ordinal)
                            }
                        }
                    }
                }
            }
        }
    ) { pv ->
        HorizontalPager(
            modifier = Modifier.padding(pv),
            verticalAlignment = Alignment.Top,
            state = pagerState
        ) { page ->
            when (page) {
                BottomPager.TaskList.ordinal -> TaskListScreen()

                BottomPager.StudyList.ordinal -> StudyListScreen()

                BottomPager.Data.ordinal -> InsightScreen(currentPager = pagerState.currentPage)

                BottomPager.Log.ordinal -> LogScreen()
            }
        }
    }
}

@Composable
private fun RowScope.BottomPageItem(
    page: BottomPager,
    currentPage: Int,
    onClick: () -> Unit,
) {
    BottomNavigationItem(
        icon = {
            Icon(
                imageVector = ImageVector.vectorResource(page.imageVectorId),
                contentDescription = "",
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(
                text = LocalContext.current.getString(page.titleId),
                style = AppTheme.typography.body3,
            )
        },
        selectedContentColor = AppTheme.colors.primary,
        unselectedContentColor = AppTheme.colors.onBackground.copy(@Suppress("MagicNumber") 0.6F),
        selected = currentPage == page.ordinal,
        alwaysShowLabel = true,
        onClick = onClick
    )
}
