package healthstack.kit.task.activity.predefined

import healthstack.kit.task.activity.ActivityTask

class PredefinedTaskUtil {
    companion object {
        private const val TAPPING_SPEED = "TAPPING_SPEED"
        private const val GUIDED_BREATHING = "GUIDED_BREATHING"
        private const val STROOP_TEST = "STROOP_TEST"
        private const val GAIT_AND_BALANCE = "GAIT_AND_BALANCE"
        private const val RANGE_OF_MOTION = "RANGE_OF_MOTION"
        private const val REACTION_TIME = "REACTION_TIME"
        private const val MOBILE_SPIROMETRY = "MOBILE_SPIROMETRY"
        private const val SUSTAINED_PHONATION = "SUSTAINED_PHONATION"
        private const val SPEECH_RECOGNITION = "SPEECH_RECOGNITION"

        fun generatePredefinedTask(
            id: String,
            taskId: String,
            name: String,
            description: String,
            isCompleted: Boolean,
            isActive: Boolean,
            completionTitle: String,
            completionDescription: List<String>?,
            activityType: String,
        ): ActivityTask =
            when (activityType) {
                TAPPING_SPEED -> TappingSpeedActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                GUIDED_BREATHING -> GuidedBreathingActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                GAIT_AND_BALANCE -> GaitAndBalanceActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                RANGE_OF_MOTION -> RangeOfMotionActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                REACTION_TIME -> ReactionTimeActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                STROOP_TEST -> ColorWordChallengeActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                MOBILE_SPIROMETRY -> MobileSpirometryActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                SUSTAINED_PHONATION -> SustainedPhonationActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                SPEECH_RECOGNITION -> SpeechRecognitionActivityTask(
                    id,
                    taskId,
                    name,
                    description,
                    completionTitle,
                    completionDescription,
                    isCompleted = isCompleted,
                    isActive = isActive
                )
                else -> throw IllegalArgumentException("not supported activity task")
            }
    }
}
