package cz.legat.prectito.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MainViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel() {

    val popularBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getPopularBooks())
    }

}
