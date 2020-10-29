package cz.legat.prectito.ui.main.authors

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cz.legat.core.paging.AuthorsPagingSource
import cz.legat.core.repository.AuthorsRepository

class AuthorsViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    private val filterId = MutableLiveData<String>()

    fun filter(id: String) {
        filterId.value = id
    }

    val flow = Pager(
        config = PagingConfig(pageSize = 40, initialLoadSize = 40, enablePlaceholders = false),
        pagingSourceFactory = {
            AuthorsPagingSource(authorsRepository, filterId.value)
        }
    ).flow.cachedIn(viewModelScope)

}
