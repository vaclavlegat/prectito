package cz.legat.books.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers

const val POPULAR = "popular"
const val NEW = "new"
const val EBOOK = "ebook"

class BooksViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val type = savedStateHandle.get<String>("type") ?: throw NullPointerException("Type must be provided")

    val books = liveData(Dispatchers.IO) {
        emit(
            when (type) {
                POPULAR -> booksRepository.getPopularBooks().dropLast(1)
                NEW -> booksRepository.getNewBooks().dropLast(1)
                EBOOK -> booksRepository.getEBooks().dropLast(2)
                else -> throw IllegalArgumentException("Unknown book type")
            }

        )
    }
}
