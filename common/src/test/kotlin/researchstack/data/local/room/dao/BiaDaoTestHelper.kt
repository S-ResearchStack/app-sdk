package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.Bia

class BiaDaoTestHelper : PrivDaoTestHelper<Bia>() {

    override var data = Bia(0L, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10)
    private val biaDao = spyk<BiaDao>()
    override var wearableDao: PrivDao<Bia> = biaDao

    init {
        every { wearableAppDataBase.biaDao() } returns biaDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, Bia> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, Bia> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, Bia> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
