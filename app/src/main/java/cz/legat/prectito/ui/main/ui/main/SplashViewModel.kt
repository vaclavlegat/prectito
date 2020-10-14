package cz.legat.prectito.ui.main.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class SplashViewModel : ViewModel() {

    var goHome = liveData(Dispatchers.IO) {
        delay(2000)
        emit(true)
    }
}