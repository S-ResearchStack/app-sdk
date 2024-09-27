package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.Accelerometer

class AccelerometerDaoTestHelper : PrivDaoTestHelper<Accelerometer>() {

    override var data = Accelerometer(0L, 0, 0, 0)
    private val accelerometerDao = spyk<AccelerometerDao>()
    override var wearableDao: PrivDao<Accelerometer> = accelerometerDao

    init {
        every { wearableAppDataBase.accelerometerDao() } returns accelerometerDao
    }
    override fun `insert_should_return_null`(): PagingSource<Int, Accelerometer> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, Accelerometer> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, Accelerometer> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
