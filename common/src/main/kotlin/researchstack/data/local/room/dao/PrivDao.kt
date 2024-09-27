package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import researchstack.domain.model.Timestamp
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss

abstract class PrivDao<T : Timestamp>(
    private val tableName: String,
) {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(data: Collection<T>)

    @RawQuery(
        observedEntities =
            [
                Accelerometer::class,
                Bia::class,
                EcgSet::class,
                PpgGreen::class,
                PpgIr::class,
                PpgRed::class,
                SpO2::class,
                SweatLoss::class,
                HeartRate::class,
            ],
    )
    protected abstract fun getGreaterThan(query: SupportSQLiteQuery): PagingSource<Int, T>

    fun getGreaterThan(timeStamp: Long): PagingSource<Int, T> =
        getGreaterThan(
            SimpleSQLiteQuery("SELECT * FROM $tableName WHERE timeStamp > $timeStamp ORDER BY timeStamp ASC"),
        )

    @RawQuery
    abstract fun deleteLEThan(query: SupportSQLiteQuery): Boolean

    fun deleteLEThan(timeStamp: Long) {
        deleteLEThan(SimpleSQLiteQuery("DELETE FROM $tableName  WHERE timeStamp <= $timeStamp"))
    }
}
