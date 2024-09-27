package researchstack.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.mapper.toDomain
import kotlin.reflect.KClass

abstract class PermittedDataTypeRepository<T : Enum<*>> {
    protected abstract val studyDao: StudyDao
    protected abstract val shareAgreementDao: ShareAgreementDao
    protected abstract val tClass: KClass<T>

    suspend fun getPermittedTypeStudyIdMap(): Map<T, List<String>> =
        withContext(
            Dispatchers.IO,
        ) {
            studyDao
                .getActiveStudies()
                .first()
                .associate { (id) -> getAgreedUsageStatTypes(id).first() }
                .reverse()
        }

    fun getPermittedTypes(): Flow<Set<T>> =
        callbackFlow {
            studyDao.getActiveStudies().collect { studies ->
                val typeStudyMap = mutableMapOf<String, List<T>>()
                if (studies.isNullOrEmpty()) {
                    trySend(typeStudyMap)
                } else {
                    studies.forEach { (id) ->
                        CoroutineScope(Dispatchers.IO).launch {
                            getAgreedUsageStatTypes(id).collect {
                                typeStudyMap[it.first] = it.second
                                trySend(typeStudyMap)
                            }
                        }
                    }
                }
            }

            awaitClose()
        }.map { it.values.flatten().toSet() }.flowOn(Dispatchers.IO)

    private fun getAgreedUsageStatTypes(studyId: String) =
        shareAgreementDao
            .getAgreedShareAgreement(studyId)
            .map { entities -> entities.map { it.toDomain().dataType }.filterIsInstance(tClass.java) }
            .map { sensorDataTypes -> studyId to sensorDataTypes }

    private fun <T1, T2> Map<T1, List<T2>>.reverse(): Map<T2, List<T1>> {
        val reversed = mutableMapOf<T2, MutableList<T1>>()

        forEach { (key, value) ->
            value.forEach {
                if (!reversed.contains(it)) reversed[it] = mutableListOf()
                reversed[it]?.add(key)
            }
        }

        return reversed
    }
}
