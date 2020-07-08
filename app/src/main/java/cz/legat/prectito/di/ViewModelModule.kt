package cz.legat.prectito.di

import cz.legat.prectito.repository.BooksRepository
import cz.legat.prectito.ui.main.BooksViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object ViewModelModule {

    @Provides
    fun provideMainViewModel(booksRepository: BooksRepository): BooksViewModel {
        return BooksViewModel(booksRepository)
    }
}