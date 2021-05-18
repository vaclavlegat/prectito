package cz.legat.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import cz.legat.core.model.SearchResult
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.util.*

@FlowPreview
@ExperimentalCoroutinesApi
class SearchResultsViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    companion object {
        const val SEARCH_DELAY = 300L
    }

    var searchBooks: MutableLiveData<List<SearchResult>> = MutableLiveData()

    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(SEARCH_DELAY)
        .flatMapLatest { query ->
            booksRepository.searchBook(query).combine(authorsRepository.searchAuthor(query)) { books, authors ->
                listOf(books, authors).flatten()
            }.conflate()

        }
        .catch { throwable ->

        }.asLiveData()
}
