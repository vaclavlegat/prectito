package cz.legat.prectito.ui.main.authors.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.paging.preparePagingLiveData
import cz.legat.prectito.navigation.ID_KEY
import cz.legat.prectito.repository.AuthorsRepository
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

    val books = preparePagingLiveData(savedStateHandle.get<String>(ID_KEY)!!, authorsRepository::getAuthorBooks)
}
