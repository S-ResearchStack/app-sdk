package researchstack.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.data.local.room.WearableAppDataBase
import researchstack.data.local.room.dao.PassiveDataStatusDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoProvider {
    @Singleton
    @Provides
    fun providePassiveDataStatusDao(@ApplicationContext context: Context): PassiveDataStatusDao =
        WearableAppDataBase.getDatabase(context).passiveDataStatusDao()
}
