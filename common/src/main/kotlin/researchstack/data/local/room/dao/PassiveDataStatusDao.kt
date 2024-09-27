package researchstack.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import researchstack.data.local.room.entity.PassiveDataStatusEntity

@Dao
interface PassiveDataStatusDao {
    @Query("SELECT * FROM passive_data_status WHERE enabled = 1")
    fun getEnabledData(): Flow<List<PassiveDataStatusEntity>>

    @Upsert
    suspend fun save(passiveDataStatusEntity: PassiveDataStatusEntity)

    @Query("SELECT * FROM passive_data_status WHERE dataType == :dataType")
    suspend fun getDataByType(dataType: String): PassiveDataStatusEntity

    @Query("SELECT EXISTS(SELECT * FROM passive_data_status WHERE dataType == :dataType AND enabled == true) ")
    suspend fun isEnable(dataType: String): Boolean
}
