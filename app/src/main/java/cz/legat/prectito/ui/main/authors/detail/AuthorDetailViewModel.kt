package cz.legat.prectito.ui.main.authors.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.model.Book
import cz.legat.prectito.repository.AuthorsRepository
import kotlinx.coroutines.Dispatchers

class AuthorDetailViewModel  @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val author = liveData(Dispatchers.IO) {
        savedStateHandle.get<String>("id")?.let {
            emit(authorsRepository.getAuthor(it))
        }
    }
}
