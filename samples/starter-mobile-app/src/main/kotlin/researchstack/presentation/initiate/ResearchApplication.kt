package researchstack.presentation.initiate

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import researchstack.BuildConfig
import researchstack.auth.data.datasource.auth.SAIdTokenRequester
import javax.inject.Inject

@HiltAndroidApp
class ResearchApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (getProcessName() == packageName)
            SAIdTokenRequester.initialize(this.applicationContext, BuildConfig.SA_CLIENT_ID, BuildConfig.SA_CLIENT_SECRET)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
