package researchstack.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.domain.repository.LocalDBRepository
import javax.inject.Inject

class LocalDBRepositoryImpl @Inject constructor(private val researchAppDatabase: ResearchAppDatabase) : LocalDBRepository {
    override suspend fun clearLocalDB() {
        withContext(Dispatchers.IO) {
            researchAppDatabase.clearAllTables()
        }
    }
}
