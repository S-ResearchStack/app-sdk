package researchstack.wearable.standalone.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import researchstack.wearable.standalone.data.datasource.local.room.entity.StudyEntity

@Dao
interface StudyDao {
    @Query("SELECT * FROM studies WHERE joined = 0")
    fun getNotJoinedStudies(): Flow<List<StudyEntity>>

    @Query("SELECT * FROM studies WHERE id = :studyId LIMIT 1")
    fun getStudyById(studyId: String): Flow<StudyEntity>

    @Query("SELECT * FROM studies WHERE participationCode = :participationCode LIMIT 1")
    suspend fun getStudyByParticipationCode(participationCode: String): StudyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(studies: List<StudyEntity>)

    @Upsert(entity = StudyEntity::class)
    suspend fun save(study: StudyEntity)

    @Query("UPDATE studies SET joined=:joined WHERE id=:id")
    fun updateStudyJoined(id: String, joined: Boolean)

    @Query("UPDATE studies SET joined=:joined, registrationId=:registrationId, status=:status WHERE id=:id")
    fun updateJoinedAndRegistrationId(id: String, joined: Boolean, registrationId: String?, status: Int)

    @Query("SELECT * FROM studies WHERE joined = 1")
    fun getJoinedStudies(): Flow<List<StudyEntity>>

    @Query("SELECT * FROM studies WHERE joined = 1 AND status = 2")
    fun getActiveStudies(): Flow<List<StudyEntity>>
}
