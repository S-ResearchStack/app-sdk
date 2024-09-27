package researchstack.data.datasource.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.data.datasource.local.room.entity.Speed

class SpeedDaoTestHelper : TimestampEntityBaseDaoTestHelper<Speed>() {

    override var data = Speed(0f, 0, 0)

    private val speedDao = spyk<SpeedDao>()
    override var dao: TimestampEntityBaseDao<Speed> = speedDao

    init {
        every { db.speedDao() } returns speedDao
    }

    override fun `insert should return null`(): PagingSource<Int, Speed> {
        dao.insert(data)
        return dao.getGreaterThan(0L)
    }

    override fun `insertAll should return null`(): PagingSource<Int, Speed> {
        dao.insertAll(data)
        return dao.getGreaterThan(0L)
    }

    override fun `deleteLEThan should throw NullPointerException`(): PagingSource<Int, Speed> {
        dao.deleteLEThan(0)
        return dao.getGreaterThan(0L)
    }
}
