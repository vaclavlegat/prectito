package cz.legat.scanner.ui

import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.mlkit.vision.barcode.Barcode
import cz.legat.core.persistence.SavedBook
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ISBNViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) :
    ViewModel() {

    var myBooks: MutableLiveData<List<SavedBook>> = MutableLiveData()

    val workflowState = MutableLiveData<WorkflowState>()
    val detectedBarcode = MutableLiveData<Barcode>()

    var isCameraLive = false
        private set

    fun getBookByISBN(isbn: String): LiveData<SavedBook?> {
        val query = isbn.replace("-", "")
        return booksRepository.getBookByISBN(query).asLiveData()
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
        SEARCHED
    }

    @MainThread
    fun setWorkflowState(workflowState: WorkflowState) {
        this.workflowState.value = workflowState
    }

    fun markCameraLive() {
        isCameraLive = true
    }

    fun markCameraFrozen() {
        isCameraLive = false
        setWorkflowState(WorkflowState.NOT_STARTED)
    }
}
