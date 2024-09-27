package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import researchstack.data.datasource.local.room.entity.ParticipationRequirementEntity
import researchstack.domain.model.task.taskresult.SurveyResult

@Dao
interface ParticipationRequirementDao {
    @Query("SELECT * FROM participationRequirements WHERE studyId = :studyId")
    suspend fun getParticipationRequirement(studyId: String): ParticipationRequirementEntity?

    @Query(
        """
        UPDATE participationRequirements
        SET surveyResult = :surveyResult, signedInformedConsentUrl = :signedInformedConsentUrl
        WHERE studyId = :studyId"""
    )
    suspend fun setResult(
        studyId: String,
        surveyResult: SurveyResult,
        signedInformedConsentUrl: String,
    )

    @Query("SELECT * FROM participationRequirements")
    suspend fun findAll(): List<ParticipationRequirementEntity>

    @Query("DELETE FROM participationRequirements")
    suspend fun removeAll()

    @Insert
    suspend fun insertAll(studies: List<ParticipationRequirementEntity>)
}
