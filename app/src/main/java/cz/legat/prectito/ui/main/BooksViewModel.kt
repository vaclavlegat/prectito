package cz.legat.prectito.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.prectito.model.Book
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel() {

    var books: LiveData<List<Book>>? = null

    fun getBooks(type: Int) {
        books = liveData(Dispatchers.IO) {
            emit(if (type == POPULAR_BOOKS) booksRepository.getPopularBooks() else booksRepository.getNewBooks())
        }
    }
}
