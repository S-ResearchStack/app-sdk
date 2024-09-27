package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import researchstack.data.datasource.local.room.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events")
    fun findAll(): Flow<List<EventEntity>>

    @Query("DELETE FROM events WHERE timestamp = :timestamp")
    suspend fun deleteByTimestamp(timestamp: Long)

    @Query("DELETE FROM events")
    suspend fun removeAll()

    @Insert
    suspend fun insert(data: EventEntity)
}
