package researchstack.domain.repository

interface LocalDBRepository {
    suspend fun clearLocalDB()
}
