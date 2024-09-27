package researchstack.data.datasource.grpc.mapper

import com.google.protobuf.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.quartz.CronExpression
import researchstack.backend.grpc.TaskSpec
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.TaskType
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.DateTimeQuestion
import researchstack.domain.model.task.question.RankQuestion
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.TextQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.question.common.QuestionTag.CHECKBOX
import researchstack.domain.model.task.question.common.QuestionTag.DATETIME
import researchstack.domain.model.task.question.common.QuestionTag.DROPDOWN
import researchstack.domain.model.task.question.common.QuestionTag.IMAGE
import researchstack.domain.model.task.question.common.QuestionTag.RADIO
import researchstack.domain.model.task.question.common.QuestionTag.RANK
import researchstack.domain.model.task.question.common.QuestionTag.SLIDER
import researchstack.domain.model.task.question.common.QuestionTag.TEXT
import researchstack.domain.model.task.question.common.QuestionTag.UNSPECIFIED
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.domain.model.task.taskresult.TaskResult
import researchstack.util.getCurrentTimeOffset
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import researchstack.backend.grpc.ActivityType as GrpcActivityType
import researchstack.backend.grpc.Question as GrpcQuestion
import researchstack.backend.grpc.Question.TAG as GrpcQuestionTag
import researchstack.backend.grpc.Section as GrpcSection
import researchstack.backend.grpc.TaskResult as GrpcTaskResult
import researchstack.backend.grpc.TaskResult.ActivityResult as GrpcActivityResult
import researchstack.backend.grpc.TaskResult.QuestionResult as GrpcQuestionResult
import researchstack.backend.grpc.TaskResult.SurveyResult as GrpcSurveyResult
import researchstack.backend.grpc.TaskSpec as GrpcTaskSpec

fun GrpcTaskSpec.toEntitiesFlow(): Flow<TaskEntity> = flow {
    val minStartTime = Calendar.getInstance()
        .apply {
            add(Calendar.MINUTE, -(validMin.toInt()))
        }
        .time

    val startTime: Date =
        startTime.toDate()
            .let {
                if (it.after(minStartTime))
                    it
                else
                    minStartTime
            }

    val endTime: Date = endTime.toDate()
    val cronExpression = CronExpression(schedule)

    var taskStartTime: Date =
        if (cronExpression.isSatisfiedBy(startTime))
            startTime
        else
            cronExpression.getTimeAfter(startTime)

    while (taskStartTime <= endTime) {
        val taskEntity = createTaskEntity(taskStartTime, getTaskType())
        emit(taskEntity)

        taskStartTime = cronExpression.getTimeAfter(taskStartTime)
    }
}

private fun TaskSpec.createTaskEntity(
    taskStartTime: Date,
    taskType: TaskType,
) = TaskEntity(
    id = null,
    taskId = id,
    scheduledAt = dateToLocalDateTimeSystem(taskStartTime),
    validUntil = dateToLocalDateTimeSystem(taskStartTime).plusMinutes(validMin),
    taskResult = null,
    studyId = studyId,
    task = when (taskType) {
        TaskType.ACTIVITY -> ActivityTask(
            id = null,
            taskId = id,
            studyId = studyId,
            title = title,
            description = description,
            scheduledAt = dateToLocalDateTimeSystem(taskStartTime),
            validUntil = dateToLocalDateTimeSystem(taskStartTime).plusMinutes(validMin),
            inClinic = inClinic,
            activityResult = null,
            completionTitle = activityTask.completionTitle,
            completionDescription = activityTask.completionDescription,
            activityType = activityTask.type.toDomain()
        )

        TaskType.SURVEY -> SurveyTask(
            id = null,
            taskId = id,
            studyId = studyId,
            title = title,
            description = description,
            scheduledAt = dateToLocalDateTimeSystem(taskStartTime),
            validUntil = dateToLocalDateTimeSystem(taskStartTime).plusMinutes(validMin),
            inClinic = inClinic,
            sections = surveyTask.sectionsList.map { section -> section.toDomain() },
            surveyResult = null
        )
    }
)

fun dateToLocalDateTimeSystem(date: Date): LocalDateTime =
    date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

fun GrpcTaskSpec.getTaskType(): TaskType {
    return if (hasActivityTask()) TaskType.ACTIVITY
    else if (hasSurveyTask()) TaskType.SURVEY
    else throw IllegalStateException("")
}

fun GrpcActivityType.toDomain() = when (this) {
    GrpcActivityType.ACTIVITY_TYPE_UNSPECIFIED -> ActivityType.UNSPECIFIED
    GrpcActivityType.UNRECOGNIZED -> ActivityType.UNSPECIFIED
    GrpcActivityType.ACTIVITY_TYPE_TAPPING_SPEED -> ActivityType.TAPPING_SPEED
    GrpcActivityType.ACTIVITY_TYPE_REACTION_TIME -> ActivityType.REACTION_TIME
    GrpcActivityType.ACTIVITY_TYPE_GUIDED_BREATHING -> ActivityType.GUIDED_BREATHING
    GrpcActivityType.ACTIVITY_TYPE_RANGE_OF_MOTION -> ActivityType.RANGE_OF_MOTION
    GrpcActivityType.ACTIVITY_TYPE_GAIT_AND_BALANCE -> ActivityType.GAIT_AND_BALANCE
    GrpcActivityType.ACTIVITY_TYPE_STROOP_TEST -> ActivityType.STROOP_TEST
    GrpcActivityType.ACTIVITY_TYPE_SPEECH_RECOGNITION -> ActivityType.SPEECH_RECOGNITION
    GrpcActivityType.ACTIVITY_TYPE_MOBILE_SPIROMETRY -> ActivityType.MOBILE_SPIROMETRY
    GrpcActivityType.ACTIVITY_TYPE_SUSTAINED_PHONATION -> ActivityType.SUSTAINED_PHONATION
    GrpcActivityType.ACTIVITY_TYPE_FIVE_METER_WALK_TEST -> ActivityType.FIVE_METER_WALK_TEST
    GrpcActivityType.ACTIVITY_TYPE_STATE_BALANCE_TEST -> ActivityType.STATE_BALANCE_TEST
    GrpcActivityType.ACTIVITY_TYPE_ROMBERG_TEST -> ActivityType.ROMBERG_TEST
    GrpcActivityType.ACTIVITY_TYPE_SIT_TO_STAND -> ActivityType.SIT_TO_STAND
    GrpcActivityType.ACTIVITY_TYPE_ORTHOSTATIC_BP -> ActivityType.ORTHOSTATIC_BP
    GrpcActivityType.ACTIVITY_TYPE_BIA_MEASUREMENT -> ActivityType.BIA_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_BP_MEASUREMENT -> ActivityType.BP_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_ECG_MEASUREMENT -> ActivityType.ECG_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_PPG_MEASUREMENT -> ActivityType.PPG_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_SPO2_MEASUREMENT -> ActivityType.SPO2_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_BP_AND_BIA_MEASUREMENT -> ActivityType.BP_AND_BIA_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_STABLE_MEASUREMENT -> ActivityType.STABLE_MEASUREMENT
    GrpcActivityType.ACTIVITY_TYPE_SHAPE_PAINTING -> ActivityType.SHAPE_PAINTING
    GrpcActivityType.ACTIVITY_TYPE_CATCH_LADYBUG -> ActivityType.CATCH_LADYBUG
    GrpcActivityType.ACTIVITY_TYPE_MEMORIZE -> ActivityType.MEMORIZE
    GrpcActivityType.ACTIVITY_TYPE_MEMORIZE_WORDS_START -> ActivityType.MEMORIZE_WORDS_START
    GrpcActivityType.ACTIVITY_TYPE_MEMORIZE_WORDS_END -> ActivityType.MEMORIZE_WORDS_END
    GrpcActivityType.ACTIVITY_TYPE_DESCRIBE_IMAGE -> ActivityType.DESCRIBE_IMAGE
    GrpcActivityType.ACTIVITY_TYPE_READ_TEXT_ALOUD -> ActivityType.READ_TEXT_ALOUD
    GrpcActivityType.ACTIVITY_TYPE_ANSWER_VERBALLY -> ActivityType.ANSWER_VERBALLY
    GrpcActivityType.ACTIVITY_TYPE_ANSWER_WRITTEN -> ActivityType.ANSWER_WRITTEN
}

fun GrpcSection.toDomain() = researchstack.domain.model.task.Section(
    questionsList.map { it.toDomain() }
)

fun GrpcQuestion.toDomain() = when (tag.toDomain()) {
    UNSPECIFIED -> throw IllegalArgumentException("Not supported tag.")
    RADIO, CHECKBOX, IMAGE, DROPDOWN -> ChoiceQuestion(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag.toDomain(),
        isRequired = required,
        options = choiceProperties.optionsList.map { Option(it.value, it.label) }
    )

    SLIDER -> ScaleQuestion(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag.toDomain(),
        isRequired = required,
        low = scaleProperties.low,
        high = scaleProperties.high,
        highLabel = scaleProperties.lowLabel,
        lowLabel = scaleProperties.highLabel
    )

    DATETIME -> DateTimeQuestion(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag.toDomain(),
        isRequired = required,
        isDate = dateTimeProperties.isDate,
        isTime = dateTimeProperties.isTime,
        isRange = dateTimeProperties.isRange
    )

    RANK -> RankQuestion(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag.toDomain(),
        isRequired = required,
        options = rankingProperties.optionsList.map { Option(it.value, it.label) }
    )

    TEXT -> TextQuestion(
        id = id,
        title = title,
        explanation = explanation,
        tag = tag.toDomain(),
        isRequired = required,
    )
}

fun GrpcQuestionTag.toDomain() = when (this) {
    GrpcQuestion.TAG.TAG_UNSPECIFIED -> UNSPECIFIED
    GrpcQuestion.TAG.UNRECOGNIZED -> UNSPECIFIED
    GrpcQuestion.TAG.TAG_SLIDER -> SLIDER
    GrpcQuestion.TAG.TAG_RADIO -> RADIO
    GrpcQuestion.TAG.TAG_CHECKBOX -> CHECKBOX
    GrpcQuestion.TAG.TAG_IMAGE -> IMAGE
    GrpcQuestion.TAG.TAG_DROPDOWN -> DROPDOWN
    GrpcQuestion.TAG.TAG_DATETIME -> DATETIME
    GrpcQuestion.TAG.TAG_TEXT -> TEXT
    GrpcQuestion.TAG.TAG_RANK -> RANK
}

fun ActivityResult.toGrpcData(): GrpcTaskResult.ActivityResult {
    val builder = GrpcActivityResult.newBuilder()
    return when (activityType) {
        ActivityType.MOBILE_SPIROMETRY ->
            builder.setMobileSpirometry(
                GrpcActivityResult.newBuilder().mobileSpirometryBuilder
                    .setFilePath(
                        result["filePath"] as String
                    )
                    .build()
            ).build()

        ActivityType.FIVE_METER_WALK_TEST -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setFiveMeterWalkTest(
                GrpcActivityResult.newBuilder().fiveMeterWalkTestBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.STATE_BALANCE_TEST -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setStateBalanceTest(
                GrpcActivityResult.newBuilder().stateBalanceTestBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.ROMBERG_TEST -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setRombergTest(
                GrpcActivityResult.newBuilder().rombergTestBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.SIT_TO_STAND -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setSitToStand(
                GrpcActivityResult.newBuilder().sitToStandBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.ORTHOSTATIC_BP -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setOrthostaticBp(
                GrpcActivityResult.newBuilder().orthostaticBpBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.BIA_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setBiaMeasurement(
                GrpcActivityResult.newBuilder().biaMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.BP_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setBpMeasurement(
                GrpcActivityResult.newBuilder().bpMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.ECG_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setEcgMeasurement(
                GrpcActivityResult.newBuilder().ecgMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.PPG_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setPpgMeasurement(
                GrpcActivityResult.newBuilder().ppgMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.SPO2_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setSpo2Measurement(
                GrpcActivityResult.newBuilder().spo2MeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.BP_AND_BIA_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setBpAndBiaMeasurement(
                GrpcActivityResult.newBuilder().bpAndBiaMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.STABLE_MEASUREMENT -> {
            val startTime = OffsetDateTime.parse(result["startTime"] as String)
            val endTime = OffsetDateTime.parse(result["endTime"] as String)
            builder.setStableMeasurement(
                GrpcActivityResult.newBuilder().stableMeasurementBuilder
                    .setStartTime(
                        Timestamp.newBuilder()
                            .setSeconds(startTime.toEpochSecond())
                            .setNanos(startTime.nano)
                            .build()
                    )
                    .setEndTime(
                        Timestamp.newBuilder()
                            .setSeconds(endTime.toEpochSecond())
                            .setNanos(endTime.nano)
                            .build()
                    )
                    .build()
            ).build()
        }

        ActivityType.UNSPECIFIED -> TODO()
        ActivityType.TAPPING_SPEED -> TODO()
        ActivityType.REACTION_TIME -> TODO()
        ActivityType.GUIDED_BREATHING -> TODO()
        ActivityType.RANGE_OF_MOTION -> TODO()
        ActivityType.GAIT_AND_BALANCE -> TODO()
        ActivityType.STROOP_TEST -> TODO()
        ActivityType.SPEECH_RECOGNITION -> TODO()
        ActivityType.SUSTAINED_PHONATION -> TODO()
        ActivityType.SHAPE_PAINTING -> TODO()
        ActivityType.CATCH_LADYBUG -> TODO()
        ActivityType.MEMORIZE -> TODO()
        ActivityType.MEMORIZE_WORDS_START -> TODO()
        ActivityType.MEMORIZE_WORDS_END -> TODO()
        ActivityType.DESCRIBE_IMAGE -> TODO()
        ActivityType.READ_TEXT_ALOUD -> TODO()
        ActivityType.ANSWER_VERBALLY -> TODO()
        ActivityType.ANSWER_WRITTEN -> TODO()
    }
}

fun SurveyResult.toGrpcData(): GrpcTaskResult.SurveyResult = GrpcSurveyResult.newBuilder()
    .addAllQuestionResults(questionResults.map { it.toGrpcData() }).build()

fun QuestionResult.toGrpcData(): GrpcTaskResult.QuestionResult = GrpcQuestionResult.newBuilder()
    .setId(id).setResult(result).build()

fun TaskResult.toGrpcData(studyId: String, taskId: String): GrpcTaskResult =
    GrpcTaskResult.newBuilder()
        .setStudyId(studyId)
        .setTaskId(taskId)
        .setStartedAt(startedAt.toTimeStamp())
        .setFinishedAt(finishedAt.toTimeStamp())
        .setTimeOffset(getCurrentTimeOffset())
        .apply {
            when (this@toGrpcData) {
                is ActivityResult -> activityResult = this@toGrpcData.toGrpcData()
                is SurveyResult -> surveyResult = this@toGrpcData.toGrpcData()
            }
        }.build()
