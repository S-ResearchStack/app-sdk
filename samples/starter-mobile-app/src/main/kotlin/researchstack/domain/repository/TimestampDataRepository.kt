package researchstack.domain.repository

import androidx.paging.PagingSource
import researchstack.domain.model.Timestamp

interface TimestampDataRepository<T : Timestamp> {
    fun insert(data: T)

    fun insertAll(vararg data: T)

    fun deleteLEThan(timeStamp: Long)

    fun getGreaterThan(timeStamp: Long): PagingSource<Int, T>
}
