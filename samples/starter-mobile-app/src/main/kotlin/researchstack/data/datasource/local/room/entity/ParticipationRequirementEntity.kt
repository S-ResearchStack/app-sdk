package researchstack.data.datasource.local.room.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.eligibilitytest.answer.Answer
import researchstack.domain.model.events.DeviceStatDataType
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.SHealthDataType
import researchstack.domain.model.task.Section
import researchstack.domain.model.task.taskresult.SurveyResult

@Entity(
    tableName = "participationRequirements"
)
data class ParticipationRequirementEntity(
    @PrimaryKey
    val studyId: String,
    val SHealthDataTypes: List<SHealthDataType>,
    val trackerDataTypes: List<TrackerDataType>,
    val privDataTypes: List<PrivDataType>,
    val deviceStatDataTypes: List<DeviceStatDataType>,
    val sections: List<Section>,
    val answers: List<Answer>,
    val surveyResult: SurveyResult? = null,
    @Embedded val informedConsentEntity: InformedConsentEntity,
) {
    data class InformedConsentEntity(
        val informedConsentUrl: String,
        val signedInformedConsentUrl: String? = null
    )
}
