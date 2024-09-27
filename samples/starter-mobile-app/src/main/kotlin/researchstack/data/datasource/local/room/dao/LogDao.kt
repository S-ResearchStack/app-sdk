package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import researchstack.data.datasource.local.room.entity.AppLogEntity

@Dao
interface LogDao {
    @Query("SELECT * FROM logs")
    suspend fun findAll(): List<AppLogEntity>

    @Query("SELECT * FROM logs WHERE sent = 0")
    suspend fun findUnsentLogs(): List<AppLogEntity>

    @Query("UPDATE logs SET sent = 1 WHERE id=:id")
    suspend fun setLogAsSent(id: Int)

    @Delete
    suspend fun removeData(data: AppLogEntity): Int

    @Query("DELETE FROM logs")
    suspend fun removeAll()

    @Query("DELETE FROM logs WHERE sent = 1 AND timeStamp < :timestamp")
    suspend fun deleteSentLogsBefore(timestamp: Long)

    @Insert
    suspend fun insert(data: AppLogEntity)

    // TODO should we return flow?
    @Query("SELECT * FROM logs WHERE name = :name ORDER by timeStamp desc LIMIT :limit")
    suspend fun getLatestLogs(name: String, limit: Int): List<AppLogEntity>
}
