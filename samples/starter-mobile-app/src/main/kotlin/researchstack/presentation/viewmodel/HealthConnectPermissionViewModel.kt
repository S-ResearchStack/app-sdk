package researchstack.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthConnectPermissionViewModel @Inject constructor(
    application: Application,
    @ApplicationContext private val context: Context
) : AndroidViewModel(application) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
    private val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(HeartRateRecord::class),
        HealthPermission.getReadPermission(BloodPressureRecord::class),
        HealthPermission.getReadPermission(BloodGlucoseRecord::class),
        HealthPermission.getReadPermission(OxygenSaturationRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
    )
    private val _showToast = MutableStateFlow(false)
    val showToast: StateFlow<Boolean> = _showToast

    fun requestPermission(healthConnectPermissionsLauncher: ManagedActivityResultLauncher<Set<String>, Set<String>>) {
        viewModelScope.launch {
            if (hasAllPermissionsEnabled()) {
                showToast()
            } else {
                healthConnectPermissionsLauncher.launch(permissions)
            }
        }
    }
    fun requestPermissionActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }
    private suspend fun hasAllPermissionsEnabled(): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions()
            .containsAll(permissions)
    }
    private fun showToast() {
        _showToast.value = true
    }
    fun onToastShown() {
        _showToast.value = false
    }
}
