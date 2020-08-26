package cz.legat.prectito.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel() {

    var popularBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getPopularBooks().dropLast(1))
    }

    var newBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getNewBooks().dropLast(1))
    }

    var myBooks = liveData(Dispatchers.IO) {
        emit(booksRepository.getMyBooks())
    }
}
