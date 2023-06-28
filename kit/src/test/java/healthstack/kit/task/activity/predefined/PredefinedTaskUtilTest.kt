package healthstack.kit.task.activity.predefined

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows

@DisplayName("Predefined Task Util Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PredefinedTaskUtilTest {
    @Tag("positive")
    @Test
    fun `to ViewTask`() {
        // when
        var model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "TAPPING_SPEED"
        )

        assertTrue(model is TappingSpeedActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "GUIDED_BREATHING"
        )

        assertTrue(model is GuidedBreathingActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "STROOP_TEST"
        )

        assertTrue(model is ColorWordChallengeActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "GAIT_AND_BALANCE"
        )

        assertTrue(model is GaitAndBalanceActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "RANGE_OF_MOTION"
        )

        assertTrue(model is RangeOfMotionActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "REACTION_TIME"
        )

        assertTrue(model is ReactionTimeActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "MOBILE_SPIROMETRY"
        )

        assertTrue(model is MobileSpirometryActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "SUSTAINED_PHONATION"
        )

        assertTrue(model is SustainedPhonationActivityTask)
        assertEquals(model.id, "id")

        model = PredefinedTaskUtil.generatePredefinedTask(
            id = "id",
            taskId = "taskId",
            name = "name",
            description = "description",
            isCompleted = false,
            isActive = true,
            completionTitle = "title",
            completionDescription = listOf("description1", "description2"),
            activityType = "SPEECH_RECOGNITION"
        )

        assertTrue(model is SpeechRecognitionActivityTask)
        assertEquals(model.id, "id")
    }

    @Tag("negative")
    @Test
    fun `to not supported`() {
        assertThrows<IllegalArgumentException> {
            PredefinedTaskUtil.generatePredefinedTask(
                id = "id",
                taskId = "taskId",
                name = "name",
                description = "description",
                isCompleted = false,
                isActive = true,
                completionTitle = "title",
                completionDescription = listOf("description1", "description2"),
                activityType = "JIYUN"
            )
        }
    }
}
