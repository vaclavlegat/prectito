package cz.legat.search.di

import cz.legat.navigation.SearchNavigator
import cz.legat.search.navigation.SearchNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object SearchModule {

    @Provides
    fun provideSearchNavigator(): SearchNavigator {
        return SearchNavigatorImpl()
    }
}