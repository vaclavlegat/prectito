package cz.legat.prectito.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SplashViewModel @ViewModelInject constructor() : ViewModel() {

    var goHome = liveData(Dispatchers.IO) {
        delay(1000)
        emit(true)
    }
}