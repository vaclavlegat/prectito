package cz.legat.scanner.ui

import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.Barcode
import cz.legat.core.persistence.SavedBook
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashSet

class ISBNViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) :
    ViewModel() {

    val searchedBook: MutableLiveData<SavedBook> = MutableLiveData()
    var myBooks: MutableLiveData<List<SavedBook>> = MutableLiveData()

    val workflowState = MutableLiveData<WorkflowState>()
    val detectedBarcode = MutableLiveData<Barcode>()

    private val objectIdsToSearch = HashSet<Int>()

    var isCameraLive = false
        private set


    fun getBookByISBN(isbn: String) {
        val query = isbn.replace("-", "")
        viewModelScope.launch {
            val book = booksRepository.getBookByISBN(query)
            searchedBook.value = book
        }
    }

    fun saveBook(savedBook: SavedBook) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.saveBook(savedBook)
        }
    }

    fun removeBook(book: SavedBook) {
        viewModelScope.launch(Dispatchers.IO) {
            booksRepository.removeBook(book)
        }
    }

    fun loadMyBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val books = booksRepository.getMyBooks()
            withContext(Dispatchers.Main) {
                myBooks.value = books
            }
        }
    }

    enum class WorkflowState {
        NOT_STARTED,
        DETECTING,
        DETECTED,
        CONFIRMING,
        CONFIRMED,
        SEARCHING,
        SEARCHED
    }

    @MainThread
    fun setWorkflowState(workflowState: WorkflowState) {
        this.workflowState.value = workflowState
    }

    fun markCameraLive() {
        isCameraLive = true
        objectIdsToSearch.clear()
    }

    fun markCameraFrozen() {
        isCameraLive = false
    }
}
