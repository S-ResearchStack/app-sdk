package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import researchstack.data.datasource.local.room.entity.FileUploadRequestEntity

@Dao
interface FileUploadRequestDao {

    @Query("SELECT * FROM file_upload_requests")
    suspend fun findAll(): List<FileUploadRequestEntity>

    @Query("SELECT * FROM file_upload_requests WHERE studyId = :studyId")
    fun findAllByStudyId(studyId: String): Flow<List<FileUploadRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fileUploadRequestEntity: FileUploadRequestEntity)

    @Query("DELETE FROM file_upload_requests WHERE jobId = :jobId")
    suspend fun deleteByJobId(jobId: String)
}
