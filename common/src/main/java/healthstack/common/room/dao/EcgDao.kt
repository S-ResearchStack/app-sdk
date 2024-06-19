package healthstack.common.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import healthstack.common.model.EcgSet
import healthstack.common.model.ECG_TABLE_NAME

@Dao
interface EcgDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: EcgSet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: Collection<EcgSet>)

    @RawQuery(
        observedEntities =
        [
            EcgSet::class,
        ]
    )
    fun getGreaterThan(query: SupportSQLiteQuery): PagingSource<Int, EcgSet>

    fun getGreaterThan(timeStamp: Long): PagingSource<Int, EcgSet> {
        return getGreaterThan(
            SimpleSQLiteQuery("SELECT * FROM $ECG_TABLE_NAME WHERE timeStamp > $timeStamp ORDER BY timeStamp ASC")
        )
    }

    @RawQuery
    fun deleteLEThan(query: SupportSQLiteQuery): Boolean

    fun deleteLEThan(timeStamp: Long) {
        deleteLEThan(SimpleSQLiteQuery("DELETE FROM $ECG_TABLE_NAME  WHERE timeStamp <= $timeStamp"))
    }
}
