package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.PpgGreen

class PpgGreenDaoTestHelper : PrivDaoTestHelper<PpgGreen>() {

    override var data = PpgGreen(0L, 0)
    private val ppgGreenDao = spyk<PpgGreenDao>()
    override var wearableDao: PrivDao<PpgGreen> = ppgGreenDao

    init {
        every { wearableAppDataBase.ppgGreenDao() } returns ppgGreenDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, PpgGreen> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, PpgGreen> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, PpgGreen> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
