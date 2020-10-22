package cz.legat.prectito.ui.main.authors

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import cz.legat.core.model.Author
import cz.legat.core.paging.preparePagingLiveData
import cz.legat.core.repository.AuthorsRepository

class AuthorsViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    val authors: LiveData<PagedList<Author>> = preparePagingLiveData(authorsRepository::getAuthors)
}
