package cz.legat.prectito.di

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.api.GoogleBooksService
import cz.legat.prectito.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    fun provideBooksRepository(booksService: BooksService, googleBooksService: GoogleBooksService): BooksRepository {
        return BooksRepository(booksService, googleBooksService)
    }

}