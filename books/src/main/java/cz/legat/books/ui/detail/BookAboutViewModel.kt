package cz.legat.books.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers

class BookAboutViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookId = savedStateHandle.get<String>(ID_KEY)

    val book = liveData(Dispatchers.IO) {
        emit(booksRepository.getBook(bookId))
    }
}
