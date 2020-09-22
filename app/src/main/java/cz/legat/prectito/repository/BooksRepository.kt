package cz.legat.prectito.repository

import cz.legat.booksdp.parser.HtmlParser
import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.prectito.api.BooksService
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.persistence.SavedBookDao
import cz.legat.prectito.ui.main.base.BaseRepository
import cz.legat.prectito.ui.main.base.Result
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao
) : BaseRepository() {

    private val popularCache = mutableListOf<Book>()
    private val newCache = mutableListOf<Book>()
    private val commentsCache = hashMapOf<String, List<Comment>>()
    private val searchedBooksCache = hashMapOf<String, List<Book>>()
    private val booksCache = hashMapOf<String, Book>()

    suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }

    suspend fun getPopularBooks(): List<Book> {
        if (popularCache.isNotEmpty()) {
            return popularCache
        }

        return when (val result = apiCall { booksService.getPopularBooks() }) {
            is Result.Success -> {
                val popular = HtmlParser().parseBooksPopular(result.data)
                popularCache.clear()
                popularCache.addAll(popular)
                popular
            }
            is Result.Error -> listOf()
        }
    }

    suspend fun getNewBooks(): List<Book> {
        if (newCache.isNotEmpty()) {
            return newCache
        }
        return when (val result = apiCall { booksService.getNewBooks() }) {
            is Result.Success -> {
                val new = PARSER.parseBooksPopular(result.data)
                newCache.clear()
                newCache.addAll(new)
                new
            }
            is Result.Error -> listOf()
        }
    }

    suspend fun getBook(id: String?): Book? {
        if (id == null) {
            return null
        }
        if (booksCache.containsKey(id)) {
            return booksCache[id]
        }

        return when (val result = apiCall { booksService.getBook(id) }) {
            is Result.Success -> {
                val book = PARSER.parseBook(id, result.data)
                book.copy(
                    description = if (book.description.contains("Popis knihy zde zatím bohužel není.") || !book.description.contains(
                            "celý text"
                        )
                    ) book.description else book.description.dropLast(12)
                )
                booksCache[id] = book
                book
            }
            is Result.Error -> null
        }
    }

    suspend fun getBookComments(id: String?): List<Comment> {
        if (id == null) {
            return listOf()
        }

        if (commentsCache.containsKey(id)) {
            return commentsCache[id] ?: listOf()
        }

        return when (val result = apiCall { booksService.getBookComments(id) }) {
            is Result.Success -> {
                val comments = PARSER.parseBookComments(result.data)
                commentsCache[id] = comments
                comments
            }
            is Result.Error -> listOf()
        }
    }

    suspend fun getBookByISBN(isbn: String): SavedBook? {
        return when (val result = apiCall { booksService.getBookByISBN(isbn) }) {
            is Result.Success -> {
                val book = PARSER.parseBook(isbn, result.data)
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
        if (searchedBooksCache.containsKey(query)) {
            return searchedBooksCache[query] ?: listOf()
        }
        return when (val result = apiCall { booksService.searchBook(query) }) {
            is Result.Success -> {
                val searchBooks = PARSER.parseBookSearchResults(result.data)
                searchedBooksCache[query] = searchBooks
                searchBooks
            }
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