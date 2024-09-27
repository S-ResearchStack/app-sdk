package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_upload_requests")
data class FileUploadRequestEntity(
    @PrimaryKey
    val jobId: String,
    val jobName: String,
    val studyId: String,
    val params: Map<String, String> = mutableMapOf(),
)
