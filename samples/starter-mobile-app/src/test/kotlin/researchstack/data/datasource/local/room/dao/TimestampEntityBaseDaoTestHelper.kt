package researchstack.data.datasource.local.room.dao

import android.content.Context
import androidx.paging.PagingSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.jupiter.api.io.TempDir
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.domain.model.Timestamp
import java.io.File

abstract class TimestampEntityBaseDaoTestHelper<T : Timestamp> {
    @TempDir
    private var tempDir: File? = null
    private val context = mockk<Context> {
        every { applicationContext } returns this
        every { cacheDir } returns tempDir
    }
    protected val db = spyk(ResearchAppDatabase.getDatabase(context))
    abstract var dao: TimestampEntityBaseDao<T>
    abstract var data: T

    abstract fun `insert should return null`(): PagingSource<Int, T>

    abstract fun `insertAll should return null`(): PagingSource<Int, T>

    abstract fun `deleteLEThan should throw NullPointerException`(): PagingSource<Int, T>
}
