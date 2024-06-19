package healthstack.wearable.support

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import healthstack.common.room.WearableAppDataBase
import healthstack.common.room.dao.EcgDao
import healthstack.wearable.support.data.DataSender
import healthstack.wearable.support.data.EcgTracker
import healthstack.wearable.support.data.pref.LastSyncTimePref
import healthstack.wearable.support.data.pref.TrackMeasureTimePref
import healthstack.wearable.support.data.pref.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Provider {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideLastMeasureTimePref(dataStore: DataStore<Preferences>): TrackMeasureTimePref =
        TrackMeasureTimePref(dataStore)

    @Singleton
    @Provides
    fun provideLastSyncTimePref(dataStore: DataStore<Preferences>): LastSyncTimePref =
        LastSyncTimePref(dataStore)

    @Singleton
    @Provides
    fun provideEcgTracker(): EcgTracker =
        EcgTracker()

    @Singleton
    @Provides
    fun provideECGDao(@ApplicationContext context: Context): EcgDao =
        WearableAppDataBase.initialize(context).ecgDao()

    @Singleton
    @Provides
    fun provideDataSender(@ApplicationContext context: Context): DataSender = DataSender(context)
}
