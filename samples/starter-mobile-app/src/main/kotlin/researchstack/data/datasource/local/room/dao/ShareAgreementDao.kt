package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import researchstack.data.datasource.local.room.entity.ShareAgreementEntity

@Dao
interface ShareAgreementDao {
    @Query("SELECT * FROM shareAgreements WHERE studyId = :studyId")
    fun getShareAgreements(studyId: String): Flow<List<ShareAgreementEntity>>

    @Query("SELECT * FROM shareAgreements WHERE studyId = :studyId AND approval = 1")
    fun getAgreedShareAgreement(studyId: String): Flow<List<ShareAgreementEntity>>

    @Query("SELECT * FROM shareAgreements WHERE dataType = :dataType AND approval = 1")
    fun getAgreedShareAgreementFromDataType(dataType: String): Flow<List<ShareAgreementEntity>>

    @Query("SELECT approval FROM shareAgreements WHERE studyId = :studyId AND dataType = :dataType")
    fun getApprovalShareAgreementWithStudyAndDataType(studyId: String, dataType: String): Boolean

    @Insert
    suspend fun insert(dataShareAgreementEntity: ShareAgreementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dataShareAgreementEntity: List<ShareAgreementEntity>)

    @Update
    suspend fun update(dataShareAgreementEntity: ShareAgreementEntity)

    @Query("DELETE FROM shareAgreements WHERE studyId = :studyId")
    suspend fun deleteAll(studyId: String)

    @Query("Update shareAgreements SET approval=:approval WHERE studyId = :studyId AND dataType = :dataType")
    suspend fun updateApproval(studyId: String, dataType: String, approval: Boolean)
}
