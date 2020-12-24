package cz.legat.books.ui.detail

import android.os.Environment
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import cz.legat.core.extensions.ID_KEY
import cz.legat.core.paging.BasePagingInternalSource
import cz.legat.core.paging.BasePagingSource
import cz.legat.core.repository.BooksRepository
import cz.legat.core.repository.PdfRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class BookDetailViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository,
    private val pdfRepository: PdfRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val bookId = savedStateHandle.get<String>(ID_KEY)

    val book = liveData(Dispatchers.IO) {
        emit(booksRepository.getBook(bookId))
    }

    val flow = Pager(
        config = PagingConfig(pageSize = 10, initialLoadSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {
            BasePagingInternalSource(booksRepository::getBookCommentsByPage, bookId)
        }
    ).flow.cachedIn(viewModelScope)

    val filePath: MutableLiveData<String> = MutableLiveData()

    fun downloadPdf(url: String?) {
        if(url.isNullOrEmpty()){
            return
        }
        viewModelScope.launch {
            val responseBody = pdfRepository.downloadPdf(url)
            responseBody?.let {
                filePath.value = saveFile(responseBody, url)
            }
        }
    }

    private fun saveFile(body: ResponseBody, url: String): String? {
        var input: InputStream? = null
        val filename = url.substring(url.lastIndexOf("/", url.length-1)+1)
        Timber.d("Trying to save file $filename")
        val pdfFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(pdfFile)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            Timber.d("File $filename saved to path ${pdfFile.path}")
            return pdfFile.path
        } catch (e: Exception) {
            Timber.e(e.toString())
        } finally {
            input?.close()
        }
        return null
    }
}
