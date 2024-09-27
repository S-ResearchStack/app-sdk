package researchstack.data.datasource.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.sensor.Accelerometer

class AccelerometerDaoTestHelper : TimestampEntityBaseDaoTestHelper<Accelerometer>() {

    override var data = Accelerometer(0f, 0f, 0f)

    private val accelerometerDao = spyk<AccelerometerDao>()
    override var dao: TimestampEntityBaseDao<Accelerometer> = accelerometerDao

    init {
        every { db.accelerometerDao() } returns accelerometerDao
    }

    override fun `insert should return null`(): PagingSource<Int, Accelerometer> {
        dao.insert(data)
        return dao.getGreaterThan(0L)
    }

    override fun `insertAll should return null`(): PagingSource<Int, Accelerometer> {
        dao.insertAll(data)
        return dao.getGreaterThan(0L)
    }

    override fun `deleteLEThan should throw NullPointerException`(): PagingSource<Int, Accelerometer> {
        dao.deleteLEThan(0)
        return dao.getGreaterThan(0L)
    }
}
