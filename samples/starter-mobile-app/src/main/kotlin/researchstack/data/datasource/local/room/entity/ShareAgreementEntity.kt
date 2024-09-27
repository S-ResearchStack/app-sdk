package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shareAgreements",
    indices = [Index(value = ["studyId"]), Index(value = ["studyId", "dataType"], unique = true) ]
)
data class ShareAgreementEntity(
    val studyId: String,
    val dataType: String,
    val approval: Boolean = false,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
