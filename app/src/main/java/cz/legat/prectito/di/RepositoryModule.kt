package cz.legat.prectito.di

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    fun provideBooksRepository(booksService: BooksService): BooksRepository {
        return BooksRepository(booksService)
    }

}