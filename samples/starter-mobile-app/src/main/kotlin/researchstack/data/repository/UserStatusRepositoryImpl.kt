package researchstack.data.repository

import android.util.Log
import kotlinx.coroutines.flow.first
import researchstack.backend.integration.outport.SubjectOutPort
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.domain.model.Study
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.repository.UserStatusRepository

class UserStatusRepositoryImpl(
    private val studyDao: StudyDao,
    private val subjectOutPort: SubjectOutPort,
) : UserStatusRepository {
    override suspend fun getStudyStatusById(studyId: String): Result<StudyStatusModel> =
        subjectOutPort.getSubjectStatus(studyId).map { it.toDomain() }

    override suspend fun fetchStudyStatusFromNetWork(): List<Study> {
        val listStudyChange = mutableListOf<Study>()
        studyDao.getActiveStudies().first()
            .forEach { studyEntity ->
                getStudyStatusById(studyEntity.id)
                    .onSuccess {
                        if (studyEntity.toDomain().status != it) {
                            listStudyChange.add(studyEntity.toDomain().copy(status = it))
                            studyDao.save(studyEntity.copy(status = it.number))
                        }
                    }
                    .onFailure {
                        Log.e("Get status", "fail to get study status")
                    }
            }
        return listStudyChange.toList()
    }
}
