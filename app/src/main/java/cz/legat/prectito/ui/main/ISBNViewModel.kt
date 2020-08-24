package cz.legat.prectito.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import cz.legat.prectito.model.google.VolumeInfo
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ISBNViewModel @Inject constructor(private val booksRepository: BooksRepository) : ViewModel() {

    val searchedBook: MutableLiveData<VolumeInfo> = MutableLiveData()

    fun getBookByISBN(isbn: String){
        val query = isbn.replace("-","")
        viewModelScope.launch {
            searchedBook.value = booksRepository.getBookByISBN(query)
        }
    }

}
