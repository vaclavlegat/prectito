package cz.legat.prectito.di

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.persistence.SavedBookDao
import cz.legat.prectito.repository.AuthorsRepository
import cz.legat.prectito.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBooksRepository(
        booksService: BooksService,
        savedBookDao: SavedBookDao
    ): BooksRepository {
        return BooksRepository(booksService, savedBookDao)
    }

    @Provides
    @Singleton
    fun provideAuthorsRepository(
        booksService: BooksService
    ): AuthorsRepository {
        return AuthorsRepository(booksService)
    }
}