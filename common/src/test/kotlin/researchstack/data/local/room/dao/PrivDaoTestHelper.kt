package researchstack.data.local.room.dao

import android.content.Context
import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.io.TempDir
import researchstack.data.local.room.WearableAppDataBase
import researchstack.domain.model.Timestamp
import java.io.File

abstract class PrivDaoTestHelper<T : Timestamp> {
    @TempDir
    private var tempDir: File? = null
    private val context = mockk<Context>(relaxed = true) {
        every { applicationContext } returns this
        every { cacheDir } returns tempDir
    }
    protected val wearableAppDataBase = spyk(WearableAppDataBase.getDatabase(context))
    abstract var wearableDao: PrivDao<T>
    abstract var data: T

    abstract fun `insert_should_return_null`(): PagingSource<Int, T>

    abstract fun `insertAll_should_return_null`(): PagingSource<Int, T>

    abstract fun `deleteLEThan_should_throw_NullPointerException`(): PagingSource<Int, T>
}
