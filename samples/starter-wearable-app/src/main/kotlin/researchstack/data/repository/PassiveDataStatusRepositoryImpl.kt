package researchstack.data.repository

import kotlinx.coroutines.flow.Flow
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.PassiveDataStatusRepository
import javax.inject.Inject

class PassiveDataStatusRepositoryImpl @Inject constructor(
    private val passiveDataStatusDao: PassiveDataStatusDao,
) :
    PassiveDataStatusRepository {
    override fun getStatus(): Flow<List<PassiveDataStatusEntity>> = passiveDataStatusDao.getEnabledData()

    override suspend fun setStatus(dataType: PrivDataType, status: Boolean) {
        passiveDataStatusDao.save(PassiveDataStatusEntity(dataType.name, status))
    }
}
