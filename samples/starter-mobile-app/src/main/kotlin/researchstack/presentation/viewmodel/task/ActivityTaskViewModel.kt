package researchstack.presentation.viewmodel.task

import android.app.Application
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import researchstack.BuildConfig
import researchstack.data.datasource.local.pref.dataStore
import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.domain.model.task.ActivityType
import researchstack.domain.usecase.file.UploadFileUseCase
import researchstack.domain.usecase.task.GetTodayTasksUseCase
import researchstack.domain.usecase.task.SaveAndUploadTaskResultUseCase
import researchstack.domain.usecase.wearable.SendLaunchAppMessageUseCase
import java.io.FileInputStream
import java.nio.file.Path
import javax.inject.Inject

@HiltViewModel
class ActivityTaskViewModel @Inject constructor(
    application: Application,
    getTodayTasksUseCase: GetTodayTasksUseCase,
    saveTaskResultUseCase: SaveAndUploadTaskResultUseCase,
    private val sendLaunchAppMessageUseCase: SendLaunchAppMessageUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
) : TaskViewModel(getTodayTasksUseCase, saveTaskResultUseCase) {
    var result: Map<String, Any> = emptyMap()

    private val _phase = MutableStateFlow(Phase.Instruction)
    val phase: StateFlow<Phase> = _phase

    private val measurementPreference = WearableMeasurementPref(application.dataStore)

    fun isEcgMeasurementEnabled() = measurementPreference.ecgMeasurementEnabled

    suspend fun launchWearableActivity(activityType: ActivityType) {
        val acitvityName = when (activityType) {
            ActivityType.BIA_MEASUREMENT -> ".presentation.measurement.BiaActivity"
            ActivityType.BP_MEASUREMENT -> ".presentation.measurement.BloodPressureActivity"
            ActivityType.ECG_MEASUREMENT -> ".presentation.measurement.EcgActivity"
            ActivityType.SPO2_MEASUREMENT -> ".presentation.measurement.SpO2Activity"
            ActivityType.PPG_MEASUREMENT -> ".presentation.measurement.PpgActivity"
            else -> throw Exception("not supported activity type")
        }
        sendLaunchAppMessageUseCase.invoke("${BuildConfig.APPLICATION_ID}/$acitvityName")
    }

    suspend fun uploadFile(studyId: String, localFilePath: Path, uploadObjectName: String) {
        uploadFileUseCase.invoke(studyId, uploadObjectName, FileInputStream(localFilePath.toFile()))
    }

    fun setPhase(p: Phase) {
        _phase.value = p
    }

    enum class Phase {
        Instruction,
        Measuring,
        Result,
    }
}
