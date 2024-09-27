package researchstack.data.datasource.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.sensor.Light

class LightDaoTestHelper : TimestampEntityBaseDaoTestHelper<Light>() {

    override var data = Light(0, 0)

    private val lightDao = spyk<LightDao>()
    override var dao: TimestampEntityBaseDao<Light> = lightDao

    init {
        every { db.lightDao() } returns lightDao
    }

    override fun `insert should return null`(): PagingSource<Int, Light> {
        dao.insert(data)
        return dao.getGreaterThan(0L)
    }

    override fun `insertAll should return null`(): PagingSource<Int, Light> {
        dao.insertAll(data)
        return dao.getGreaterThan(0L)
    }

    override fun `deleteLEThan should throw NullPointerException`(): PagingSource<Int, Light> {
        dao.deleteLEThan(0)
        return dao.getGreaterThan(0L)
    }
}
