package researchstack.domain.usecase

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import researchstack.domain.repository.StudyRepository
import researchstack.presentation.worker.FetchMyTasksWorker
import javax.inject.Inject

// FIXME naming / location
class ReLoginUseCase @Inject constructor(
    private val studyRepository: StudyRepository,
) {

    suspend operator fun invoke(context: Context): Result<Unit> = runCatching {
        /* TODO handle reinstall or re-login user
            remove all old data(if new account), save account,
            getUserProfile, getPublicStudy, getJoinedStudy, getTasksOfJoinStudy
         */
        studyRepository.fetchStudiesFromNetwork()

        studyRepository.fetchJoinedStudiesFromNetwork()

        WorkManager
            .getInstance(context)
            .enqueue(
                OneTimeWorkRequestBuilder<FetchMyTasksWorker>()
                    .build()
            )
    }
}
