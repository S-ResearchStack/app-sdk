package researchstack.data.datasource.grpc.mapper

import researchstack.backend.grpc.GetParticipationRequirementListResponse
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_ACCELEROMETER
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_BATTERY
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_BLOOD_GLUCOSE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_BLOOD_PRESSURE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_MOBILE_WEAR_CONNECTION
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_BATTERY
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_OFF_BODY
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_POWER_ON_OFF
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_EXERCISE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_GYROSCOPE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_HEART_RATE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_HEIGHT
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_LIGHT
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_OFF_BODY
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_OXYGEN_SATURATION
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_RESPIRATORY_RATE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_SLEEP_SESSION
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_SLEEP_STAGE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_STEPS
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_TOTAL_CALORIES_BURNED
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_UNSPECIFIED
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_ACCELEROMETER
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_BATTERY
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_BIA
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_BLOOD_PRESSURE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_ECG
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_GYROSCOPE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_HEALTH_EVENT
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_HEART_RATE
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_GREEN
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_IR
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_PPG_RED
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_SPO2
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS
import researchstack.backend.grpc.HealthData.HealthDataType.HEALTH_DATA_TYPE_WEIGHT
import researchstack.backend.grpc.HealthData.HealthDataType.UNRECOGNIZED
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.domain.model.InformedConsent
import researchstack.domain.model.ParticipationRequirement
import researchstack.domain.model.eligibilitytest.EligibilityTest
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.eligibilitytest.answer.ChoiceAnswer
import researchstack.domain.model.eligibilitytest.answer.DateTimeAnswer
import researchstack.domain.model.eligibilitytest.answer.RankingAnswer
import researchstack.domain.model.eligibilitytest.answer.ScaleAnswer
import researchstack.domain.model.eligibilitytest.answer.TextAnswer
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.sensor.TrackerDataType.ACCELEROMETER
import researchstack.domain.model.sensor.TrackerDataType.LIGHT
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.shealth.SHealthDataType.BLOOD_GLUCOSE
import researchstack.domain.model.shealth.SHealthDataType.BLOOD_PRESSURE
import researchstack.domain.model.shealth.SHealthDataType.EXERCISE
import researchstack.domain.model.shealth.SHealthDataType.HEART_RATE
import researchstack.domain.model.shealth.SHealthDataType.HEIGHT
import researchstack.domain.model.shealth.SHealthDataType.OXYGEN_SATURATION
import researchstack.domain.model.shealth.SHealthDataType.RESPIRATORY_RATE
import researchstack.domain.model.shealth.SHealthDataType.SLEEP_SESSION
import researchstack.domain.model.shealth.SHealthDataType.SLEEP_STAGE
import researchstack.domain.model.shealth.SHealthDataType.STEPS
import researchstack.domain.model.shealth.SHealthDataType.TOTAL_CALORIES_BURNED
import researchstack.domain.model.shealth.SHealthDataType.UNSPECIFIED
import researchstack.domain.model.shealth.SHealthDataType.WEIGHT
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.question.common.QuestionType
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.backend.grpc.EligibilityTest.Answer as GrpcAnswer
import researchstack.backend.grpc.EligibilityTestResult as GrpcEligibilityTestResult
import researchstack.backend.grpc.HealthData.HealthDataType as GrpcHealthDataType
import researchstack.backend.grpc.SignedInformedConsent as GrpcSignedInformedConsent
import researchstack.backend.grpc.TaskResult.QuestionResult as GrpcQuestionResult
import researchstack.backend.grpc.TaskResult.SurveyResult as GrpcSurveyResult

fun GetParticipationRequirementListResponse.toDomain(studyId: String) =
    ParticipationRequirement(
        dataTypesList.map { it.toDomain() }.filterIsInstance<SHealthDataType>(),
        dataTypesList.map { it.toDomain() }.filterIsInstance<TrackerDataType>(),
        dataTypesList.map { it.toDomain() }.filterIsInstance<PrivDataType>(),
        dataTypesList.map { it.toDomain() }.filterIsInstance<DeviceStatDataType>(),
        EligibilityTest(
            studyId,
            eligibilityTest.surveyTask.sectionsList.map { it.toDomain() },
            eligibilityTest.answersList.map { it.toDomain() },
        ),
        InformedConsent(studyId, informedConsent.imagePath)
    )

fun ParticipationRequirement.toEntity(studyId: String) =
    ParticipationRequirementEntity(
        studyId,
        SHealthDataTypes,
        trackerDataTypes,
        privDataTypes,
        deviceStatDataTypes,
        eligibilityTest.sections,
        eligibilityTest.answers,
        informedConsentEntity = ParticipationRequirementEntity.InformedConsentEntity(
            informedConsent.imageUrl,
            null
        )
    )

fun GrpcAnswer.toDomain(): Answer {
    return when (getType()) {
        QuestionType.CHOICE -> ChoiceAnswer(
            questionId,
            choiceAnswers.optionsList.map { Option(it.value, it.label) }
        )

        QuestionType.DATETIME -> DateTimeAnswer(
            questionId,
            fromDate = dateTimeAnswers.fromDate.toLocalDate(),
            toDate = dateTimeAnswers.toDate.toLocalDate(),
            fromTime = dateTimeAnswers.fromTime.toLocalTime(),
            toTime = dateTimeAnswers.toTime.toLocalTime()
        )

        QuestionType.RANK -> RankingAnswer(
            questionId,
            answers = rankingAnswers.answersList
        )

        QuestionType.SCALE -> ScaleAnswer(
            questionId,
            from = scaleAnswers.from,
            to = scaleAnswers.to
        )

        QuestionType.TEXT -> TextAnswer(
            questionId,
            textAnswers.answersList
        )
    }
}

@Suppress("CyclomaticComplexMethod")
fun GrpcHealthDataType.toDomain() =
    when (this) {
        HEALTH_DATA_TYPE_UNSPECIFIED -> UNSPECIFIED
        UNRECOGNIZED -> UNSPECIFIED
        HEALTH_DATA_TYPE_BLOOD_PRESSURE -> BLOOD_PRESSURE
        HEALTH_DATA_TYPE_HEART_RATE -> HEART_RATE
        HEALTH_DATA_TYPE_SLEEP_SESSION -> SLEEP_SESSION
        HEALTH_DATA_TYPE_SLEEP_STAGE -> SLEEP_STAGE
        HEALTH_DATA_TYPE_STEPS -> STEPS
        HEALTH_DATA_TYPE_WEIGHT -> WEIGHT
        HEALTH_DATA_TYPE_OXYGEN_SATURATION -> OXYGEN_SATURATION
        HEALTH_DATA_TYPE_HEIGHT -> HEIGHT
        HEALTH_DATA_TYPE_RESPIRATORY_RATE -> RESPIRATORY_RATE
        HEALTH_DATA_TYPE_TOTAL_CALORIES_BURNED -> TOTAL_CALORIES_BURNED
        HEALTH_DATA_TYPE_BLOOD_GLUCOSE -> BLOOD_GLUCOSE
        HEALTH_DATA_TYPE_EXERCISE -> EXERCISE
        HEALTH_DATA_TYPE_LIGHT -> LIGHT
        HEALTH_DATA_TYPE_ACCELEROMETER -> ACCELEROMETER
        HEALTH_DATA_TYPE_WEAR_ACCELEROMETER -> PrivDataType.WEAR_ACCELEROMETER
        HEALTH_DATA_TYPE_WEAR_BIA -> PrivDataType.WEAR_BIA
        HEALTH_DATA_TYPE_WEAR_ECG -> PrivDataType.WEAR_ECG
        HEALTH_DATA_TYPE_WEAR_PPG_GREEN -> PrivDataType.WEAR_PPG_GREEN
        HEALTH_DATA_TYPE_WEAR_PPG_IR -> PrivDataType.WEAR_PPG_IR
        HEALTH_DATA_TYPE_WEAR_PPG_RED -> PrivDataType.WEAR_PPG_RED
        HEALTH_DATA_TYPE_WEAR_SPO2 -> PrivDataType.WEAR_SPO2
        HEALTH_DATA_TYPE_WEAR_SWEAT_LOSS -> PrivDataType.WEAR_SWEAT_LOSS
        HEALTH_DATA_TYPE_WEAR_HEART_RATE -> PrivDataType.WEAR_HEART_RATE
        HEALTH_DATA_TYPE_WEAR_HEALTH_EVENT -> UNSPECIFIED
        HEALTH_DATA_TYPE_WEAR_GYROSCOPE -> UNSPECIFIED
        HEALTH_DATA_TYPE_WEAR_BLOOD_PRESSURE -> UNSPECIFIED
        HEALTH_DATA_TYPE_OFF_BODY -> UNSPECIFIED
        HEALTH_DATA_TYPE_BATTERY -> UNSPECIFIED
        HEALTH_DATA_TYPE_WEAR_BATTERY -> UNSPECIFIED
        HEALTH_DATA_TYPE_GYROSCOPE -> UNSPECIFIED
        HEALTH_DATA_TYPE_DEVICE_STAT_MOBILE_WEAR_CONNECTION -> DeviceStatDataType.MOBILE_WEAR_CONNECTION
        HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_BATTERY -> DeviceStatDataType.WEAR_BATTERY
        HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_OFF_BODY -> DeviceStatDataType.WEAR_OFF_BODY
        HEALTH_DATA_TYPE_DEVICE_STAT_WEAR_POWER_ON_OFF -> DeviceStatDataType.WEAR_POWER_ON_OFF
    }

fun GrpcAnswer.getType(): QuestionType {
    return if (hasChoiceAnswers()) QuestionType.CHOICE
    else if (hasRankingAnswers()) QuestionType.RANK
    else if (hasScaleAnswers()) QuestionType.SCALE
    else if (hasDateTimeAnswers()) QuestionType.DATETIME
    else if (hasTextAnswers()) QuestionType.TEXT
    else throw IllegalStateException()
}

fun EligibilityTestResult.toData(): GrpcEligibilityTestResult =
    GrpcEligibilityTestResult.newBuilder()
        .setResult(surveyResult.toData())
        .build()

fun SurveyResult.toData(): GrpcSurveyResult =
    GrpcSurveyResult.newBuilder()
        .addAllQuestionResults(questionResults.map { it.toData() })
        .build()

fun QuestionResult.toData(): GrpcQuestionResult =
    GrpcQuestionResult.newBuilder()
        .setId(id)
        .setResult(result)
        .build()

fun InformedConsent.toData(): GrpcSignedInformedConsent =
    GrpcSignedInformedConsent.newBuilder()
        .setImagePath(imageUrl)
        .build()
