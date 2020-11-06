package cz.legat.books.di

import cz.legat.books.data.remote.BooksService
import cz.legat.books.data.remote.PdfService
import cz.legat.books.navigation.BooksNavigatorImpl
import cz.legat.books.repository.BooksRepositoryImpl
import cz.legat.books.repository.PdfRepositoryImpl
import cz.legat.core.persistence.OverviewDao
import cz.legat.core.persistence.SavedBookDao
import cz.legat.core.repository.BooksRepository
import cz.legat.core.repository.PdfRepository
import cz.legat.navigation.BooksNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object BooksModule {

    @Provides
    fun provideBookService(@Named("retrofit") retrofit: Retrofit): BooksService {
        return retrofit.create(BooksService::class.java)
    }

    @Provides
    fun providePdfService(@Named("pdfRetrofit") retrofit: Retrofit): PdfService {
        return retrofit.create(PdfService::class.java)
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
        overviewDao: OverviewDao
    ): BooksRepository {
        return BooksRepositoryImpl(booksService, savedBookDao, overviewDao)
    }

    @Provides
    @Singleton
    fun providePdfRepository(
        pdfService: PdfService
    ): PdfRepository {
        return PdfRepositoryImpl(pdfService)
    }
}