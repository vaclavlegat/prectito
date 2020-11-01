package cz.legat.books.di

import cz.legat.books.navigation.BooksNavigatorImpl
import cz.legat.navigation.BooksNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object NavigationModule {

    @Provides
    fun provideBooksNavigator(): BooksNavigator {
        return BooksNavigatorImpl()
    }
}