package cz.legat.prectito.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ISBNViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) :
    ViewModel() {

    val searchedBook: MutableLiveData<SavedBook> = MutableLiveData()

    fun getBookByISBN(isbn: String) {
        val query = isbn.replace("-", "")
        viewModelScope.launch {
            val bookGoogle = booksRepository.getBookByISBN(query)
            val bookDb = booksRepository.getBookByISBNFromDb(query)
            searchedBook.value = if (!bookGoogle?.title.isNullOrEmpty()) bookGoogle else bookDb
        }
    }

    fun saveBook(savedBook: SavedBook) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.saveBook(savedBook)
        }
    }
}
