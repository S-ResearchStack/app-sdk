package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.PpgRed

class PpgRedDaoTestHelper : PrivDaoTestHelper<PpgRed>() {

    override var data = PpgRed(0L, 0)
    private val ppgRedDao = spyk<PpgRedDao>()
    override var wearableDao: PrivDao<PpgRed> = ppgRedDao

    init {
        every { wearableAppDataBase.ppgRedDao() } returns ppgRedDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, PpgRed> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, PpgRed> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, PpgRed> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
