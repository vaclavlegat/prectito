package cz.legat.prectito.ui.main.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.legat.core.model.SearchResult
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.repository.AuthorsRepository
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BooksViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    var lastQuery: String = ""

    var popularBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getPopularBooks().dropLast(1))
    }

    var newBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getNewBooks().dropLast(1))
    }

    var myBooks: MutableLiveData<List<SavedBook>> = MutableLiveData()

    var searchBooks: MutableLiveData<List<SearchResult>> = MutableLiveData()

    fun removeBook(book: SavedBook) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.removeBook(book)
        }
    }

    fun loadMyBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val books = booksRepository.getMyBooks()
            withContext(Dispatchers.Main) {
                myBooks.value = books
            }
        }
    }

    fun searchBook(query: String?) {
        if (query.isNullOrEmpty()) {
            return
        }
        query.let {
            viewModelScope.launch(Dispatchers.IO) {
                val books = booksRepository.searchBook(query)
                val authors = authorsRepository.searchAuthor(query)
                val all = mutableListOf<SearchResult>()
                all.addAll(books)
                all.addAll(authors)

                val filtered = all.filter {
                    it.getResultTitle().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                }

                lastQuery = query
                withContext(Dispatchers.Main) {
                    searchBooks.value = filtered
                }
            }
        }
    }
}
