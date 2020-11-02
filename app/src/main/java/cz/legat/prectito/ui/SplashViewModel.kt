package cz.legat.prectito.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SplashViewModel @ViewModelInject constructor(booksRepository: BooksRepository) : ViewModel() {

    var goHome = liveData(Dispatchers.IO) {
        booksRepository.getPopularBooks()
        booksRepository.getNewBooks()
        delay(2000)
        emit(true)
    }
}