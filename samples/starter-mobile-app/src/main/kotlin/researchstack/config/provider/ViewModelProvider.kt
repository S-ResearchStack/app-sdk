package researchstack.config.provider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.presentation.viewmodel.study.SharedStudyJoinViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelProvider {
    @Singleton
    @Provides
    fun provideStudySharedViewModel(): SharedStudyJoinViewModel = SharedStudyJoinViewModel()
}
