package researchstack.data.repository

import android.util.Log
import io.grpc.Status
import researchstack.backend.integration.outport.SubjectOutPort
import researchstack.data.datasource.grpc.mapper.toData
import researchstack.data.datasource.grpc.mapper.toDomain
import researchstack.domain.exception.AlreadyExistedUserException
import researchstack.domain.model.UserProfileModel
import researchstack.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val subjectOutPort: SubjectOutPort) : ProfileRepository {

    override suspend fun registerProfile(userProfileModel: UserProfileModel): Result<Unit> =
        subjectOutPort.registerSubject(userProfileModel.toData()).onFailure {
            Log.e(ProfileRepositoryImpl::class.simpleName, it.stackTraceToString())
            if (Status.fromThrowable(it).code == Status.ALREADY_EXISTS.code) return Result.failure(AlreadyExistedUserException)
            return Result.failure(it)
        }

    override suspend fun getProfile(): Result<UserProfileModel> = subjectOutPort.getSubjectProfile().map { it.toDomain() }

    override suspend fun checkRegistered(): Result<Boolean> =
        getProfile().map { true }
            .recoverCatching { e ->
                if (Status.fromThrowable(e).code == Status.NOT_FOUND.code) return@recoverCatching false
                throw e
            }

    override suspend fun updateProfile(userProfileModel: UserProfileModel): Result<Unit> = subjectOutPort.updateSubjectProfile(userProfileModel.toData())

    override suspend fun deregisterProfile(): Result<Unit> = subjectOutPort.deregisterSubject()
}
