package researchstack.data.datasource.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import researchstack.domain.model.Timestamp
import researchstack.domain.model.sensor.Accelerometer
import researchstack.domain.model.sensor.Light

abstract class TimestampEntityBaseDao<T : Timestamp>(
    private val tableName: String,
) {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(vararg data: T)

    @RawQuery(
        observedEntities =
        [
            Light::class,
            Accelerometer::class,
        ]
    )
    protected abstract fun getGreaterThan(query: SupportSQLiteQuery): PagingSource<Int, T>

    fun getGreaterThan(timestamp: Long): PagingSource<Int, T> {
        return getGreaterThan(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timestamp > $timestamp ORDER BY timeStamp ASC")
        )
    }

    @RawQuery
    abstract fun deleteLEThan(query: SupportSQLiteQuery): Boolean

    fun deleteLEThan(timestamp: Long) {
        deleteLEThan(SimpleSQLiteQuery("DELETE FROM $tableName  WHERE timestamp <= $timestamp"))
    }
}
