package researchstack.config.provider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.domain.usecase.log.AppLogger
import researchstack.requiredinterfaces.AppLoggable
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExternalInterfaceProvider {

    @Singleton
    @Provides
    fun provideAppLogger(): AppLoggable = AppLogger
}
