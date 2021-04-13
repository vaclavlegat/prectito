package cz.legat.prectito.di

import cz.legat.navigation.MainNavigator
import cz.legat.prectito.navigation.MainNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object MainModule {

    @Provides
    fun provideMainNavigator(): MainNavigator {
        return MainNavigatorImpl()
    }
}