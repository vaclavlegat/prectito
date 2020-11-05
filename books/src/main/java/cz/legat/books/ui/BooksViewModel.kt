package cz.legat.books.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.legat.core.model.SearchResult
import cz.legat.core.persistence.SavedBook
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class BooksViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

    var overview = liveData(Dispatchers.IO) {
        emit(booksRepository.getOverview())
    }
}
