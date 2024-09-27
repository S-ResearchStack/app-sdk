package researchstack.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.presentation.service.WearableSensorManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WatchManagerProvider {
    @Singleton
    @Provides
    fun provideWearableSensorManager(@ApplicationContext context: Context): WearableSensorManager =
        WearableSensorManager(context)
}
