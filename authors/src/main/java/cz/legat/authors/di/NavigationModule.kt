package cz.legat.authors.di

import cz.legat.authors.navigation.AuthorsNavigatorImpl
import cz.legat.navigation.AuthorsNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NavigationModule {

    @Provides
    fun provideAuthorsNavigator(): AuthorsNavigator {
        return AuthorsNavigatorImpl()
    }
}