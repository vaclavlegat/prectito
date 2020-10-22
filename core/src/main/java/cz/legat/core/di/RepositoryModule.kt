package cz.legat.core.di

import cz.legat.core.api.BooksService
import cz.legat.core.persistence.HomeBooksDao
import cz.legat.core.persistence.SavedBookDao
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository
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
        savedBookDao: SavedBookDao,
        homeBooksDao: HomeBooksDao
    ): BooksRepository {
        return BooksRepository(booksService, savedBookDao, homeBooksDao)
    }

    @Provides
    @Singleton
    fun provideAuthorsRepository(
        booksService: BooksService
    ): AuthorsRepository {
        return AuthorsRepository(booksService)
    }
}