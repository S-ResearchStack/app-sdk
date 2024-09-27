package researchstack.wearable.standalone.data.repository

import android.util.Log
import io.grpc.Status
import researchstack.backend.grpc.RegisterSubjectRequest
import researchstack.backend.grpc.SubjectGrpcKt.SubjectCoroutineStub
import researchstack.wearable.standalone.data.datasource.grpc.mapper.toData
import researchstack.wearable.standalone.domain.exception.AlreadyExistedUserException
import researchstack.wearable.standalone.domain.model.UserProfileModel
import researchstack.wearable.standalone.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val subjectServiceStub: SubjectCoroutineStub) : ProfileRepository {

    override suspend fun registerProfile(userProfileModel: UserProfileModel): Result<Unit> =
        runCatching {
            try {
                subjectServiceStub.registerSubject(
                    RegisterSubjectRequest.newBuilder().setSubjectProfile(userProfileModel.toData())
                        .build()
                )
            } catch (ex: Throwable) {
                if (Status.fromThrowable(ex).code == Status.ALREADY_EXISTS.code) throw AlreadyExistedUserException
                Log.e(ProfileRepositoryImpl::class.simpleName, ex.stackTraceToString())
                throw ex
            }
        }
}
