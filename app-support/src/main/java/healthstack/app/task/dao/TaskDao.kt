package healthstack.app.task.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import healthstack.app.task.entity.Task.Result
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE submittedAt is null AND scheduledAt <= :now AND :now <= validUntil")
    fun getActiveTasks(now: String): Flow<List<healthstack.app.task.entity.Task>>

    @Query("SELECT * FROM task WHERE submittedAt >= :from AND submittedAt < :to")
    fun getCompletedTasks(from: String, to: String): Flow<List<healthstack.app.task.entity.Task>>

    @Query("SELECT * FROM task WHERE submittedAt is null AND :from <= scheduledAt AND scheduledAt < :to")
    fun getUpcomingTasks(from: String, to: String): Flow<List<healthstack.app.task.entity.Task>>

    @Insert
    suspend fun insertAll(taskEntities: List<healthstack.app.task.entity.Task>)

    @Query("UPDATE task SET submittedAt = :submittedAt where id = :id")
    suspend fun setSubmittedAt(id: String, submittedAt: String)

    @Query("UPDATE task set result = :result, startedAt = :startedAt, submittedAt = :submittedAt where id = :id ")
    suspend fun setResult(id: String, result: List<Result>, startedAt: String, submittedAt: String)

    @Query("DELETE FROM task")
    suspend fun removeAll()

    @Query("SELECT * FROM task WHERE id = :id")
    fun findById(id: String): Flow<healthstack.app.task.entity.Task>
}
