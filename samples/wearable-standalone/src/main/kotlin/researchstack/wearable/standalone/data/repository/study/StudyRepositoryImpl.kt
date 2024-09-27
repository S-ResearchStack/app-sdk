package researchstack.wearable.standalone.data.repository.study

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.wearable.standalone.data.datasource.grpc.GrpcStudyDataSource
import researchstack.wearable.standalone.data.local.pref.SubjectIdPref
import researchstack.wearable.standalone.domain.repository.StudyRepository
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val subjectIdPref: SubjectIdPref,
    private val grpcStudyDataSource: GrpcStudyDataSource,
) : StudyRepository {
    override suspend fun joinStudy(studyId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            grpcStudyDataSource.participateInStudy(
                studyId,
            ).onSuccess { subjectNumber ->
                subjectIdPref.save(subjectNumber)
            }.onFailure {
                Log.e("JoinStudy", it.stackTraceToString())
            }.map { }
        }
    }

    companion object {
        private val TAG = StudyRepositoryImpl::class.simpleName
    }
}
