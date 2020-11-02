package cz.legat.books.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.legat.core.model.SearchResult
import cz.legat.core.persistence.SavedBook
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository
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

    var searchBooks: MutableLiveData<List<SearchResult>> = MutableLiveData()

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
