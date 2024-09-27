package researchstack.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.io.TempDir
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
internal open class DataStoreTestHelper(private val fileName: String) {

    private val testDispatcher = UnconfinedTestDispatcher()
    protected val testScope = TestScope(testDispatcher + Job())

    @TempDir
    private var tempDir: File? = null

    protected val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { File(tempDir, fileName) }
    )
}
