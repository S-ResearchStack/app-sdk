package researchstack.domain.usecase.log

import researchstack.domain.model.log.AppLog
import researchstack.domain.repository.LogRepository
import javax.inject.Inject

class CollectAppLogUseCase @Inject constructor(
    private val logRepository: LogRepository,
) {
    suspend operator fun invoke(appLog: AppLog): Result<Unit> = logRepository.saveAppLog(appLog)
}
