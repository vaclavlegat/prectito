package cz.legat.books.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.paging.BasePagingInternalSource
import cz.legat.core.repository.BooksRepository

class BookCommentsViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookId = savedStateHandle.get<String>(ID_KEY)

    val flow = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            BasePagingInternalSource(booksRepository::getBookComments, bookId!!)
        }
    ).flow.cachedIn(viewModelScope)
}
