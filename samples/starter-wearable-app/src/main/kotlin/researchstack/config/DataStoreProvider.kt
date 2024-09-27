package researchstack.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.data.local.pref.TrackMeasureTimePref
import researchstack.data.local.pref.UserProfilePref
import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.data.local.pref.dataStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProvider {

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
    fun provideUserProfilePref(dataStore: DataStore<Preferences>): UserProfilePref =
        UserProfilePref(dataStore)

    @Singleton
    @Provides
    fun provideWearableMeasurementPref(dataStore: DataStore<Preferences>): WearableMeasurementPref =
        WearableMeasurementPref(dataStore)
}
