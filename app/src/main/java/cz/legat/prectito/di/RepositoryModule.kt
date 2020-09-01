package cz.legat.prectito.di

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.persistence.SavedBookDao
import cz.legat.prectito.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    fun provideBooksRepository(
        booksService: BooksService,
        savedBookDao: SavedBookDao
    ): BooksRepository {
        return BooksRepository(booksService, savedBookDao)
    }
}