package cz.legat.scanner.di

import cz.legat.navigation.MyBooksNavigator
import cz.legat.scanner.navigation.MyBooksNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object MyBooksModule {

    @Provides
    fun provideMyBooksNavigator(): MyBooksNavigator {
        return MyBooksNavigatorImpl()
    }
}