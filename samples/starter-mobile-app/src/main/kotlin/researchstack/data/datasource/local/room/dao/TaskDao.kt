package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.domain.model.task.taskresult.TaskResult

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE scheduledAt <= :now AND :now <= validUntil")
    fun getTodayTasks(now: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE taskResult is null And scheduledAt <= :now AND :now <= validUntil")
    fun getActiveTasks(now: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE finishedDate == :from  And taskResult is not null")
    fun getCompletedTasks(from: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE taskResult is null  AND :from <= scheduledAt AND scheduledAt < :to")
    fun getUpcomingTasks(from: String, to: String): Flow<List<TaskEntity>>

    @Query("UPDATE task set taskResult = :taskResult, finishedDate = :finishedAt where id = :id")
    suspend fun setResult(
        id: Int,
        taskResult: TaskResult,
        finishedAt: String,
    )

    @Query("SELECT * FROM task")
    suspend fun findAll(): List<TaskEntity>

    @Insert
    suspend fun insertAll(taskEntities: List<TaskEntity>)

    @Query("DELETE FROM task")
    suspend fun removeAll()

    @Query("DELETE FROM task WHERE taskResult is null AND studyId = :studyId")
    suspend fun removeByStudyId(studyId: String)

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun findById(id: Int): TaskEntity?
}
