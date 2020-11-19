package cz.legat.books.repository

import cz.legat.books.data.remote.BooksService
import cz.legat.core.HtmlParser
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.model.Overview
import cz.legat.core.persistence.LocalOverview
import cz.legat.core.persistence.OverviewDao
import cz.legat.core.persistence.SavedBook
import cz.legat.core.persistence.SavedBookDao
import cz.legat.core.repository.BooksRepository
import timber.log.Timber
import java.util.*
import javax.inject.Inject

const val TAG = "BOOKS REPO"

class BooksRepositoryImpl @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao,
    private val overviewDao: OverviewDao
) : BooksRepository, BaseRepository() {

    private val popularCache = mutableListOf<Book>()
    private val newCache = mutableListOf<Book>()
    private val eBookCache = mutableListOf<Book>()
    private val commentsCache = hashMapOf<String, List<Comment>>()
    private val searchedBooksCache = hashMapOf<String, List<Book>>()
    private val booksCache = hashMapOf<String, Book>()

    override suspend fun getOverview(): Overview {
        val dbBooks = overviewDao.getOverview()
        if (dbBooks != null && Date().before(Date(dbBooks.timestamp.time + 60 * 60 * 1000))) {
            Timber.d("returning db overview")
            return Overview(dbBooks.popularBooks, dbBooks.newBooks, dbBooks.eBooks)
        } else if (dbBooks != null) {
            overviewDao.delete(dbBooks)
        }

        return when (val result = apiCall { booksService.getOverview() }) {
            is NetworkResult.Success -> {
                val overview = HtmlParser().parseOverview(result.data)
                overviewDao.insert(
                    LocalOverview(
                        popularBooks = overview.popularBooks,
                        newBooks = overview.newBooks,
                        eBooks = overview.eBooks
                    )
                )
                val saved = overviewDao.getOverview()
                Timber.d("returning remote overview")
                Overview(saved!!.popularBooks, saved.newBooks, saved.eBooks)
            }
            is NetworkResult.Error -> Overview(listOf(), listOf(), listOf())
        }
    }

    override suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }

    override suspend fun getPopularBooks(): List<Book> {
        if (popularCache.isNotEmpty()) {
            return popularCache
        }

        return when (val result = apiCall { booksService.getPopularBooks() }) {
            is NetworkResult.Success -> {
                val popular = HtmlParser().parseBooks(result.data)
                popularCache.clear()
                popularCache.addAll(popular)
                popular
            }
            is NetworkResult.Error -> listOf()
        }
    }

    override suspend fun getNewBooks(): List<Book> {
        if (newCache.isNotEmpty()) {
            return newCache
        }

        return when (val result = apiCall { booksService.getNewBooks() }) {
            is NetworkResult.Success -> {
                val new = PARSER.parseBooks(result.data)
                newCache.clear()
                newCache.addAll(new)
                new
            }
            is NetworkResult.Error -> listOf()
        }
    }

    override suspend fun getEBooks(): List<Book> {
        if (eBookCache.isNotEmpty()) {
            return eBookCache
        }

        return when (val result = apiCall { booksService.getEBooks() }) {
            is NetworkResult.Success -> {
                val ebooks = PARSER.parseEBooks(result.data)
                eBookCache.clear()
                eBookCache.addAll(ebooks)
                ebooks
            }
            is NetworkResult.Error -> listOf()
        }
    }

    override suspend fun getBook(id: String?): Book? {
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
                    description = if (book.description!!.contains("Popis knihy zde zatím bohužel není.") || !book.description!!.contains(
                            "celý text"
                        )
                    ) book.description else book.description!!.dropLast(12)
                )
                booksCache[id] = updatedBook
                updatedBook
            }
            is NetworkResult.Error -> null
        }
    }

    override suspend fun getBookComments(id: String?): List<Comment> {
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

    override fun getBookCommentsByPage(page: Int, param: String?): List<Comment> {
        val comments = commentsCache[param]
        val toIndex = (page - 1) * 10 + 10
        return if (toIndex < comments?.size ?: 0) {
            comments?.subList((page - 1) * 10, toIndex) ?: listOf()
        } else {
            comments?.subList((page - 1) * 10, comments.size) ?: listOf()
        }
    }

    override suspend fun getBookByISBN(isbn: String): SavedBook? {
        return when (val result = apiCall { booksService.getBookByISBN(isbn) }) {
            is NetworkResult.Success -> {
                val book = PARSER.parseBook(isbn, result.data)
                SavedBook(
                    bookId = book.id,
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

    override suspend fun searchBook(query: String): List<Book> {
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

    override fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    override fun removeBook(book: SavedBook) {
        return savedBookDao.delete(book)
    }
}