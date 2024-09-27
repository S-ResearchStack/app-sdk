package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.SweatLoss

class SweatLossDaoTestHelper : PrivDaoTestHelper<SweatLoss>() {

    override var data = SweatLoss(0L, 0f, 0)
    private val sweatLossDao = spyk<SweatLossDao>()
    override var wearableDao: PrivDao<SweatLoss> = sweatLossDao

    init {
        every { wearableAppDataBase.sweatLossDao() } returns sweatLossDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, SweatLoss> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, SweatLoss> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, SweatLoss> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
