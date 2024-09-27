package researchstack.presentation.worker

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import researchstack.R
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.shareagreement.GetShareAgreementUseCase
import researchstack.domain.usecase.study.FetchStudyStatusUseCase
import researchstack.domain.usecase.study.GetStudyByIdUseCase
import researchstack.domain.usecase.wearable.GetShareAgreementFromDataTypeUseCase
import researchstack.domain.usecase.wearable.PassiveDataStatusUseCase
import researchstack.domain.usecase.wearable.WearablePassiveDataStatusSenderUseCase
import researchstack.presentation.initiate.MainActivity

@HiltWorker
class FetchStudyStatusWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchStudyStatusUseCase: FetchStudyStatusUseCase,
    private val getShareAgreementFromDataTypeUseCase: GetShareAgreementFromDataTypeUseCase,
    private val passiveDataStatusUseCase: PassiveDataStatusUseCase,
    private val wearablePassiveDataStatusSenderUseCase: WearablePassiveDataStatusSenderUseCase,
    private val getShareAgreementUseCase: GetShareAgreementUseCase,
    private val getStudyByIdUseCase: GetStudyByIdUseCase
) : CoroutineWorker(appContext, workerParams) {
    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val listStudyChange = fetchStudyStatusUseCase()
                listStudyChange.forEach { study ->
                    getShareAgreementUseCase(study.id).first()
                        .filter { (it.dataType is PrivDataType) }.forEach {
                            if ((it.dataType as PrivDataType).isPassive) {
                                val listPrivData =
                                    getShareAgreementFromDataTypeUseCase(it.dataType.name).first()
                                        .filter { shareAgreement ->
                                            (
                                                getStudyByIdUseCase(shareAgreement.studyId).first().status
                                                    ?: StudyStatusModel.STUDY_STATUS_PARTICIPATING
                                                ) == StudyStatusModel.STUDY_STATUS_PARTICIPATING
                                        }
                                if (passiveDataStatusUseCase(
                                        it.dataType,
                                        listPrivData.isNotEmpty()
                                    )
                                ) {
                                    wearablePassiveDataStatusSenderUseCase(
                                        it.dataType,
                                        listPrivData.isNotEmpty(),
                                    ).onFailure { throwE ->
                                        Log.e(
                                            FetchStudyStatusWorker::class.java.name,
                                            throwE.message.toString()
                                        )
                                    }
                                }
                            }
                        }
                }
                if (isPostNotificationGranted()) {
                    val notificationManager = createNotificationChannel()
                    listStudyChange.forEach { study ->
                        val status = when (study.status) {
                            StudyStatusModel.STUDY_STATUS_DROP -> "dropped"
                            StudyStatusModel.STUDY_STATUS_COMPLETE -> "completed"
                            StudyStatusModel.STUDY_STATUS_WITHDRAW -> "withdraw"
                            else -> "participating"
                        }
                        val notification = createNotification("You have $status ${study.name}")
                        notificationManager.notify(1, notification)
                        applicationContext.sendBroadcast(
                            Intent("com.samsung.research.health.STUDY_STATUS_UPDATED")
                                .putExtra("studyId", study.id)
                                .putExtra("status", study.status)
                                .putExtra("subjectId", study.registrationId)
                        )
                    }
                }
                Result.success()
            } catch (ex: Exception) {
                Log.e(FetchStudyWorker::javaClass.name, ex.stackTraceToString())
                Result.retry()
            }
        }
    }

    private fun isPostNotificationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.packageManager.checkPermission(
                Manifest.permission.POST_NOTIFICATIONS,
                applicationContext.packageName
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun createNotification(message: String): Notification {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            PENDING_INTENT_REQUEST_CODE,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setContentTitle("Samsung Research")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel(): NotificationManager {
        val name = "status notification"
        val descriptionText = ""
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
        return notificationManager
    }

    companion object {
        private const val CHANNEL_ID = "STATUS_NOTIFICATION_CHANNEL"
        private const val PENDING_INTENT_REQUEST_CODE = 0
    }
}
