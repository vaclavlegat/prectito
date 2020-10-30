package cz.legat.core.repository

import cz.legat.core.HtmlParser
import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.api.BooksService
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.persistence.HomeBooks
import cz.legat.core.persistence.HomeBooksDao
import cz.legat.core.persistence.NEW
import cz.legat.core.persistence.POPULAR
import cz.legat.core.persistence.SavedBook
import cz.legat.core.persistence.SavedBookDao
import java.util.*
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao,
    private val homeBooksDao: HomeBooksDao
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

        val dbBooks = homeBooksDao.getByType(POPULAR)
        if (dbBooks != null && Date().before(Date(dbBooks.timestamp.time + 60 * 60 * 1000))) {
            return dbBooks.books
        } else if (dbBooks != null) {
            homeBooksDao.delete(dbBooks)
        }

        return when (val result = apiCall { booksService.getPopularBooks() }) {
            is NetworkResult.Success -> {
                val popular = HtmlParser().parseBooksPopular(result.data)
                popularCache.clear()
                popularCache.addAll(popular)
                val homeBooks = HomeBooks(timestamp = Date(), type = POPULAR, books = popular)
                homeBooksDao.insert(homeBooks)
                popular
            }
            is NetworkResult.Error -> listOf()
        }
    }

    suspend fun getNewBooks(): List<Book> {
        if (newCache.isNotEmpty()) {
            return newCache
        }

        val dbBooks = homeBooksDao.getByType(NEW)
        if (dbBooks != null && Date().before(Date(dbBooks.timestamp.time + 60 * 60 * 1000))) {
            return dbBooks.books
        } else if (dbBooks != null) {
            homeBooksDao.delete(dbBooks)
        }

        return when (val result = apiCall { booksService.getNewBooks() }) {
            is NetworkResult.Success -> {
                val new = PARSER.parseBooksPopular(result.data)
                newCache.clear()
                newCache.addAll(new)
                val homeBooks = HomeBooks(timestamp = Date(), type = NEW, books = new)
                homeBooksDao.insert(homeBooks)
                new
            }
            is NetworkResult.Error -> listOf()
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
            is NetworkResult.Success -> {
                val book = PARSER.parseBook(id, result.data)
                val updatedBook = book.copy(
                    description = if (book.description.contains("Popis knihy zde zatím bohužel není.") || !book.description.contains(
                            "celý text"
                        )
                    ) book.description else book.description.dropLast(12)
                )
                booksCache[id] = updatedBook
                updatedBook
            }
            is NetworkResult.Error -> null
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
            is NetworkResult.Success -> {
                val comments = PARSER.parseBookComments(result.data)
                commentsCache[id] = comments
                comments
            }
            is NetworkResult.Error -> listOf()
        }
    }

    suspend fun getBookByISBN(isbn: String): SavedBook? {
        return when (val result = apiCall { booksService.getBookByISBN(isbn) }) {
            is NetworkResult.Success -> {
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
            is NetworkResult.Error -> SavedBook(isbn = isbn)
        }
    }

    suspend fun searchBook(query: String): List<Book> {
        if (searchedBooksCache.containsKey(query)) {
            return searchedBooksCache[query] ?: listOf()
        }
        return when (val result = apiCall { booksService.searchBook(query) }) {
            is NetworkResult.Success -> {
                val searchBooks = PARSER.parseBookSearchResults(result.data)
                searchedBooksCache[query] = searchBooks
                searchBooks
            }
            is NetworkResult.Error -> listOf()
        }
    }

    fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    fun removeBook(book: SavedBook) {
        return savedBookDao.delete(book)
    }
}