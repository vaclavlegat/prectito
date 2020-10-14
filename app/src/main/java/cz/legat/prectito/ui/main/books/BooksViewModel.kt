package cz.legat.prectito.ui.main.books

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.legat.core.model.Author
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.repository.AuthorsRepository
import cz.legat.prectito.repository.BooksRepository
import cz.legat.prectito.ui.main.paging.BaseDataSourceFactory
import cz.legat.prectito.ui.main.paging.BasePageKeyedDataSource
import cz.legat.core.model.SearchResult
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

    var searchAuthors: MutableLiveData<List<cz.legat.core.model.Author>> = MutableLiveData()

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
        query?.let {
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

    fun searchAuthor(query: String?) {
        query?.let {
            viewModelScope.launch(Dispatchers.IO) {
                val authors = authorsRepository.searchAuthor(query)
                withContext(Dispatchers.Main) {
                    searchAuthors.value = authors
                }
            }
        }
    }

    private val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(40)
        .setPageSize(40)
        .setEnablePlaceholders(false)
        .build()
    val authors: LiveData<PagedList<Author>> = initializedPagedListBuilder(config).build()

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Author> {
        val dataSourceFactory =
            BaseDataSourceFactory(BasePageKeyedDataSource<Author>(authorsRepository::getAuthors))
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}
