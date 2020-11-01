package cz.legat.books

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookDetailViewModel  @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookId = savedStateHandle.get<String>(ID_KEY) ?: throw IllegalArgumentException("Book id not provided")

    val book = liveData(Dispatchers.IO) {
        emit(booksRepository.getBook(bookId))
    }

    val comments = liveData(Dispatchers.IO) {
        val comments = booksRepository.getBookComments(bookId)
        withContext(Dispatchers.Main){
            showMoreCommentsVisible.value = comments.size > 3
        }
        emit(comments.take(3))
    }

    val allComments = liveData(Dispatchers.IO) {
        val comments = booksRepository.getBookComments(bookId)
        emit(comments)
    }

    val showMoreCommentsVisible: MutableLiveData<Boolean> = MutableLiveData()
}
