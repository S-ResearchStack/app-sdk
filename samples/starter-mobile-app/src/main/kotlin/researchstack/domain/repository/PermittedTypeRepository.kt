package researchstack.domain.repository

import kotlinx.coroutines.flow.Flow

interface PermittedTypeRepository<T : Enum<*>> {
    fun getPermittedTypes(): Flow<Set<T>>
    suspend fun getPermittedTypeStudyIdMap(): Map<T, List<String>>
}
