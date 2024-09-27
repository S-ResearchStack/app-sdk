package researchstack.presentation.screen.insight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.BuildConfig
import researchstack.R.string
import researchstack.presentation.component.AppSwitch
import researchstack.presentation.component.TopBar
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.viewmodel.UISettingViewModel
import researchstack.presentation.viewmodel.WearableMeasurementPrefViewModel

@Composable
fun SettingScreen(
    uiSettingViewModel: UISettingViewModel = hiltViewModel(),
    WearableMeasurementPrefViewModel: WearableMeasurementPrefViewModel = hiltViewModel(),
) {
    val scrollState = rememberScrollState()
    val mobileSyncEnabled = uiSettingViewModel.mobileDataSyncEnable.collectAsState().value
    val inClinicModeUntil = uiSettingViewModel.inClinicModeUntil.collectAsState().value
    val ecgMeasurementEnabled = uiSettingViewModel.ecgMeasurementEnabled.collectAsState().value

    Scaffold(
        topBar = {
            TopBar(title = stringResource(id = string.settings))
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
                .background(AppTheme.colors.background)
                .verticalScroll(scrollState),
        ) {
            if (BuildConfig.SUPPORT_IN_CLINIC_MODE) {
                Text(
                    text = "Select Mode",
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface,
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = string.in_clinic),
                        style = AppTheme.typography.caption,
                        color = AppTheme.colors.onSurface,
                    )
                    AppSwitch(
                        uiSettingViewModel.isInClinicMode(),
                    ) {
                        uiSettingViewModel.updateInClinicModeUntil(!it)
                    }
                }

                Spacer(Modifier.height(20.dp))
            }

            Text(
                text = "Data Sync",
                style = AppTheme.typography.body2,
                color = AppTheme.colors.onSurface,
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = string.mobile_data_enable),
                    style = AppTheme.typography.caption,
                    color = AppTheme.colors.onSurface,
                )
                AppSwitch(
                    mobileSyncEnabled,
                ) {
                    uiSettingViewModel.setMobileDataEnable(it)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "ECG / BIA",
                style = AppTheme.typography.body2,
                color = AppTheme.colors.onSurface,
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = string.enable_ecg_bia),
                    style = AppTheme.typography.caption,
                    color = AppTheme.colors.onSurface,
                )
                AppSwitch(
                    ecgMeasurementEnabled,
                ) {
                    uiSettingViewModel.setEcgMeasurementEnabled(it)
                    WearableMeasurementPrefViewModel.setEcgMeasurementEnabled(it)
                }
            }
        }
    }
}
