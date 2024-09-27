package researchstack.presentation.screen.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.ActivityType.ANSWER_VERBALLY
import researchstack.domain.model.task.ActivityType.ANSWER_WRITTEN
import researchstack.domain.model.task.ActivityType.BIA_MEASUREMENT
import researchstack.domain.model.task.ActivityType.BP_AND_BIA_MEASUREMENT
import researchstack.domain.model.task.ActivityType.BP_MEASUREMENT
import researchstack.domain.model.task.ActivityType.CATCH_LADYBUG
import researchstack.domain.model.task.ActivityType.DESCRIBE_IMAGE
import researchstack.domain.model.task.ActivityType.ECG_MEASUREMENT
import researchstack.domain.model.task.ActivityType.FIVE_METER_WALK_TEST
import researchstack.domain.model.task.ActivityType.GAIT_AND_BALANCE
import researchstack.domain.model.task.ActivityType.GUIDED_BREATHING
import researchstack.domain.model.task.ActivityType.MEMORIZE
import researchstack.domain.model.task.ActivityType.MEMORIZE_WORDS_END
import researchstack.domain.model.task.ActivityType.MEMORIZE_WORDS_START
import researchstack.domain.model.task.ActivityType.MOBILE_SPIROMETRY
import researchstack.domain.model.task.ActivityType.ORTHOSTATIC_BP
import researchstack.domain.model.task.ActivityType.PPG_MEASUREMENT
import researchstack.domain.model.task.ActivityType.RANGE_OF_MOTION
import researchstack.domain.model.task.ActivityType.REACTION_TIME
import researchstack.domain.model.task.ActivityType.READ_TEXT_ALOUD
import researchstack.domain.model.task.ActivityType.ROMBERG_TEST
import researchstack.domain.model.task.ActivityType.SHAPE_PAINTING
import researchstack.domain.model.task.ActivityType.SIT_TO_STAND
import researchstack.domain.model.task.ActivityType.SPEECH_RECOGNITION
import researchstack.domain.model.task.ActivityType.SPO2_MEASUREMENT
import researchstack.domain.model.task.ActivityType.STATE_BALANCE_TEST
import researchstack.domain.model.task.ActivityType.STROOP_TEST
import researchstack.domain.model.task.ActivityType.SUSTAINED_PHONATION
import researchstack.domain.model.task.ActivityType.TAPPING_SPEED
import researchstack.domain.model.task.ActivityType.UNSPECIFIED
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.LoadingIndicator
import researchstack.presentation.initiate.route.MainPage
import researchstack.presentation.initiate.route.Route.Main
import researchstack.presentation.screen.task.activity.LinkedWearableActivity
import researchstack.presentation.screen.task.activity.MobileSpirometryActivity
import researchstack.presentation.screen.task.activity.SimpleActivity
import researchstack.presentation.screen.task.activity.WearableActivity
import researchstack.presentation.viewmodel.task.ActivityTaskViewModel
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Complete
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Saving
import java.time.LocalDateTime

@Composable
fun ActivityTaskScreen(
    task: ActivityTask,
    activityTaskViewModel: ActivityTaskViewModel = hiltViewModel()
) {
    val taskState = activityTaskViewModel.taskState.collectAsState().value
    when (task.activityType) {
        UNSPECIFIED -> TODO()
        TAPPING_SPEED -> TODO()
        REACTION_TIME -> TODO()
        GUIDED_BREATHING -> TODO()
        RANGE_OF_MOTION -> TODO()
        GAIT_AND_BALANCE -> TODO()
        STROOP_TEST -> TODO()
        SPEECH_RECOGNITION -> TODO()
        SHAPE_PAINTING -> TODO()
        CATCH_LADYBUG -> TODO()
        MEMORIZE -> TODO()
        MEMORIZE_WORDS_START -> TODO()
        MEMORIZE_WORDS_END -> TODO()
        DESCRIBE_IMAGE -> TODO()
        READ_TEXT_ALOUD -> TODO()
        ANSWER_VERBALLY -> TODO()
        ANSWER_WRITTEN -> TODO()
        MOBILE_SPIROMETRY ->
            MobileSpirometryActivity(
                task,
                R.drawable.ic_activity_mobile_spirometry,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        SUSTAINED_PHONATION -> TODO()
        FIVE_METER_WALK_TEST ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_five_meter_walk_test,
                300,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        STATE_BALANCE_TEST ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_stage_balance_test,
                300,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        ROMBERG_TEST ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_romberg_test,
                300,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        SIT_TO_STAND ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_sit_to_stand,
                300,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        ORTHOSTATIC_BP ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_orthostatic_bp,
                300,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        BIA_MEASUREMENT ->
            WearableActivity(
                task,
                R.drawable.ic_activity_bia_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        BP_MEASUREMENT ->
            WearableActivity(
                task,
                R.drawable.ic_activity_bp_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        ECG_MEASUREMENT ->
            WearableActivity(
                task,
                R.drawable.ic_activity_ecg_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        PPG_MEASUREMENT ->
            WearableActivity(
                task,
                R.drawable.ic_activity_ppg_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        SPO2_MEASUREMENT ->
            WearableActivity(
                task,
                R.drawable.ic_activity_spo2_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        BP_AND_BIA_MEASUREMENT ->
            LinkedWearableActivity(
                task,
                if (activityTaskViewModel.isEcgMeasurementEnabled().collectAsState(true).value)
                    listOf(BP_MEASUREMENT, BIA_MEASUREMENT)
                else
                    listOf(BP_MEASUREMENT),
                R.drawable.ic_activity_bp_and_bia_measurement,
                activityTaskViewModel,
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }

        ActivityType.STABLE_MEASUREMENT ->
            SimpleActivity(
                task,
                R.drawable.ic_activity_stable_measurement,
                120,
                activityTaskViewModel
            ) {
                activityTaskViewModel.saveTaskResult(
                    ActivityResult(
                        task.id ?: 0,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        task.activityType,
                        activityTaskViewModel.result,
                    )
                )
            }
    }

    if (taskState == Saving) {
        LoadingIndicator()
    } else if (taskState == Complete) {
        LocalNavController.current.navigate("${Main.name}/${MainPage.Task.ordinal}") {
            popUpTo(0)
        }
    }
}
