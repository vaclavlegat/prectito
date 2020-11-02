package cz.legat.books.di

import cz.legat.books.data.remote.BooksService
import cz.legat.books.navigation.BooksNavigatorImpl
import cz.legat.books.repository.BooksRepositoryImpl
import cz.legat.core.persistence.HomeBooksDao
import cz.legat.core.persistence.SavedBookDao
import cz.legat.core.repository.BooksRepository
import cz.legat.navigation.BooksNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BooksModule {

    @Provides
    fun provideBookService(retrofit: Retrofit): BooksService {
        return retrofit.create(BooksService::class.java)
    }

    @Provides
    fun provideBooksNavigator(): BooksNavigator {
        return BooksNavigatorImpl()
    }

    @Provides
    @Singleton
    fun provideBooksRepository(
        booksService: BooksService,
        savedBookDao: SavedBookDao,
        homeBooksDao: HomeBooksDao
    ): BooksRepository {
        return BooksRepositoryImpl(booksService, savedBookDao, homeBooksDao)
    }
}