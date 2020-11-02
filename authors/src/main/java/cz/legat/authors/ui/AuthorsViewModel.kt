package cz.legat.authors.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cz.legat.core.paging.BasePagingSource
import cz.legat.authors.repository.AuthorsRepositoryImpl

class AuthorsViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepositoryImpl
) : ViewModel() {

    private val filterId = MutableLiveData<String>()

    fun filter(id: String) {
        filterId.value = id
    }

    val flow = Pager(
        config = PagingConfig(pageSize = 40, initialLoadSize = 40, enablePlaceholders = false),
        pagingSourceFactory = {
            BasePagingSource(authorsRepository::getAuthors, filterId.value)
        }
    ).flow.cachedIn(viewModelScope)

}
