package cz.legat.authors.di

import cz.legat.authors.data.remote.AuthorsService
import cz.legat.authors.navigation.AuthorsNavigatorImpl
import cz.legat.authors.repository.AuthorsRepositoryImpl
import cz.legat.core.repository.AuthorsRepository
import cz.legat.navigation.AuthorsNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AuthorsModule {

    @Provides
    fun provideAuthorsService(retrofit: Retrofit): AuthorsService {
        return retrofit.create(AuthorsService::class.java)
    }

    @Provides
    fun provideAuthorsNavigator(): AuthorsNavigator {
        return AuthorsNavigatorImpl()
    }

    @Provides
    @Singleton
    fun provideAuthorsRepository(
        authorsService: AuthorsService
    ): AuthorsRepository {
        return AuthorsRepositoryImpl(authorsService)
    }
}