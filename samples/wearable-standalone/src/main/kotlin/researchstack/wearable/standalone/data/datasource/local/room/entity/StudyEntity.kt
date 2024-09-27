package researchstack.wearable.standalone.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "studies",
    indices = [
        Index(value = ["id"])
    ]
)
data class StudyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val participationCode: String,
    val logoUrl: String,
    val organization: String,
    val duration: String,
    val period: String,
    val requirements: List<String>,
    val joined: Boolean = false,
    val status: Int? = null,
    val registrationId: String? = null,
)
