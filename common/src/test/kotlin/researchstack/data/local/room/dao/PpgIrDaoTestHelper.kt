package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.PpgIr

class PpgIrDaoTestHelper : PrivDaoTestHelper<PpgIr>() {

    override var data = PpgIr(0L, 0)
    private val ppgIrDaoDao = spyk<PpgIrDao>()
    override var wearableDao: PrivDao<PpgIr> = ppgIrDaoDao

    init {
        every { wearableAppDataBase.ppgIrDao() } returns ppgIrDaoDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, PpgIr> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, PpgIr> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, PpgIr> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
