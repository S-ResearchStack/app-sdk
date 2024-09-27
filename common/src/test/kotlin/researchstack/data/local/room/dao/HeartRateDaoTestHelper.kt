package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.HeartRate

class HeartRateDaoTestHelper : PrivDaoTestHelper<HeartRate>() {

    override var data = HeartRate(0L, 0, listOf(123, 456, 777), listOf(123, 456, 777), 88)
    private val heartRateDao = spyk<HeartRateDao>()
    override var wearableDao: PrivDao<HeartRate> = heartRateDao

    init {
        every { wearableAppDataBase.heartRateDao() } returns heartRateDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, HeartRate> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, HeartRate> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, HeartRate> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
