package cz.legat.books.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import cz.legat.core.repository.BooksRepository

const val POPULAR = "popular"
const val NEW = "new"
const val EBOOK = "ebook"

class BooksViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val type =
        savedStateHandle.get<String>("type") ?: throw NullPointerException("Type must be provided")

    val books = when (type) {
        POPULAR -> booksRepository.getPopularBooks().asLiveData()
        NEW -> booksRepository.getNewBooks().asLiveData()
        EBOOK -> booksRepository.getEBooks().asLiveData()
        else -> throw IllegalArgumentException("Unknown book type")
    }

}
