package researchstack.data.local.room.dao

import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.spyk
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.EcgSet

class EcgDaoTestHelper : PrivDaoTestHelper<EcgSet>() {

    override var data = EcgSet(listOf(Ecg(0L, -70f)), listOf(), 1, 2f, 3f, 4)
    private val ecgDao = spyk<EcgDao>()
    override var wearableDao: PrivDao<EcgSet> = ecgDao

    init {
        every { wearableAppDataBase.ecgDao() } returns ecgDao
    }

    override fun `insert_should_return_null`(): PagingSource<Int, EcgSet> {
        wearableDao.insert(data)
        return wearableDao.getGreaterThan(0L)
    }

    override fun `insertAll_should_return_null`(): PagingSource<Int, EcgSet> {
        wearableDao.insertAll(listOf(data))
        return wearableDao.getGreaterThan(0L)
    }

    override fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, EcgSet> {
        wearableDao.deleteLEThan(0)
        return wearableDao.getGreaterThan(0L)
    }
}
