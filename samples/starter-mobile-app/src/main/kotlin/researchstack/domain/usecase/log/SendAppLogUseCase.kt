package researchstack.domain.usecase.log

import researchstack.domain.repository.LogRepository
import javax.inject.Inject

class SendAppLogUseCase @Inject constructor(
    private val logRepository: LogRepository,
) {
    suspend operator fun invoke(): Result<Unit> = logRepository.sendAppLogToSever()
}
