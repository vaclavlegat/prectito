package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.Comment
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.persistence.SavedBookDao
import cz.legat.prectito.ui.main.base.BaseRepository
import cz.legat.prectito.ui.main.base.Result
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao
) : BaseRepository() {

    suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }

    suspend fun getPopularBooks(): List<Book> {
        return when (val result = apiCall { booksService.getPopularBooks() }) {
            is Result.Success -> result.data
            is Result.Error -> listOf()
        }
    }

    suspend fun getNewBooks(): List<Book> {
        return when (val result = apiCall { booksService.getNewBooks() }) {
            is Result.Success -> result.data
            is Result.Error -> listOf()
        }
    }

    suspend fun getBook(id: String?): Book? {
        if (id == null) {
            return null
        }
        return when (val result = apiCall { booksService.getBook(id) }) {
            is Result.Success -> {
                val book = result.data
                book.copy(
                    description = if (book.description.contains("Popis knihy zde zatím bohužel není.") || !book.description.contains(
                            "celý text"
                        )
                    ) book.description else book.description.dropLast(12)
                )
            }
            is Result.Error -> null
        }
    }

    suspend fun getBookComments(id: String?): List<Comment> {
        if (id == null) {
            return listOf()
        }
        return when (val result = apiCall { booksService.getBookComments(id) }) {
            is Result.Success -> result.data
            is Result.Error -> listOf()
        }
    }

    suspend fun getBookByISBN(isbn: String): SavedBook? {
        return when (val result = apiCall { booksService.getBookByISBN(isbn) }) {
            is Result.Success -> {
                val book = result.data
                SavedBook(
                    title = book.title,
                    author = book.author?.name,
                    isbn = isbn,
                    publishedDate = book.published,
                    pageCount = book.numberOfPages,
                    language = book.language
                )
            }
            is Result.Error -> SavedBook(isbn = isbn)
        }
    }

    suspend fun searchBook(query: String): List<Book> {
        return when (val result = apiCall { booksService.searchBook(query) }) {
            is Result.Success -> result.data
            is Result.Error -> listOf()
        }
    }

    fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    fun removeBook(book: SavedBook) {
        return savedBookDao.delete(book)
    }
}