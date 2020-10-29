package cz.legat.prectito.ui.main.authors.detail

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
import cz.legat.core.paging.AuthorsBooksPagingSource
import cz.legat.core.paging.AuthorsPagingSource
import cz.legat.core.repository.AuthorsRepository
import kotlinx.coroutines.Dispatchers

class AuthorDetailViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository,
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
            AuthorsBooksPagingSource(authorsRepository, savedStateHandle.get<String>(ID_KEY)!!)
        }
    ).flow.cachedIn(viewModelScope)
}
