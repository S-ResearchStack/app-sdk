package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.SpO2

class SpO2DaoTestHelper : PrivDaoTestHelper<SpO2>() {

    override var data = SpO2(0L, 60, 40, SpO2.Flag.LOW_SIGNAL)
    private val spO2Dao = spyk<SpO2Dao>()
    override var wearableDao: PrivDao<SpO2> = spO2Dao

    init {
        every { wearableAppDataBase.spO2Dao() } returns spO2Dao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, SpO2> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, SpO2> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, SpO2> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
