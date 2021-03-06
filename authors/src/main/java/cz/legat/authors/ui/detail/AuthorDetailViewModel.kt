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
import cz.legat.authors.repository.AuthorsRepositoryImpl
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.paging.BasePagingSource
import kotlinx.coroutines.Dispatchers

class AuthorDetailViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepositoryImpl,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val authorId =
        savedStateHandle.get<String>(ID_KEY) ?: throw IllegalArgumentException("No author id.")

    val author = liveData(Dispatchers.IO) {

        emit(authorsRepository.getAuthor(authorId))
    }
}
