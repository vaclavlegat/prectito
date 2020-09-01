package cz.legat.prectito.ui.main

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.prectito.model.Book
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers

class BookDetailViewModel  @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val book = liveData(Dispatchers.IO) {
        emit(booksRepository.getBook(savedStateHandle.get<Book>("book")?.id))
    }
}
