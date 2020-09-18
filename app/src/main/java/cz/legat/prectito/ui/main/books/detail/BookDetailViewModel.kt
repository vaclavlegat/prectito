package cz.legat.prectito.ui.main.books.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.model.Book
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers

class BookDetailViewModel  @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val book = liveData(Dispatchers.IO) {
        emit(booksRepository.getBook(savedStateHandle.get<String>("id")))
    }

    val comments = liveData(Dispatchers.IO) {
        emit(booksRepository.getBookComments(savedStateHandle.get<String>("id")))
    }
}
