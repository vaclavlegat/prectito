package cz.legat.authors.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.paging.BasePagingSource
import cz.legat.authors.repository.AuthorsRepositoryImpl
import kotlinx.coroutines.Dispatchers

class AuthorDetailViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepositoryImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val author = liveData(Dispatchers.IO) {
        savedStateHandle.get<String>(ID_KEY)?.let {
            emit(authorsRepository.getAuthor(it))
        }
    }

    val booksFlow = Pager(
        config = PagingConfig(pageSize = 40, initialLoadSize = 40, enablePlaceholders = false),
        pagingSourceFactory = {
            BasePagingSource(authorsRepository::getAuthorBooks, savedStateHandle.get<String>(ID_KEY)!!)
        }
    ).flow.cachedIn(viewModelScope)

    val books = liveData(Dispatchers.IO) {
        savedStateHandle.get<String>(ID_KEY)?.let {
            emit(authorsRepository.getAuthorBooks(1, it))
        }
    }
}
