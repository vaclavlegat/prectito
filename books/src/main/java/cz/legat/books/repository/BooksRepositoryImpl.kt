package cz.legat.books.repository

import cz.legat.books.data.remote.BooksService
import cz.legat.core.HtmlParser
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.model.Book
import cz.legat.core.model.Comment
import cz.legat.core.model.SearchResult
import cz.legat.core.persistence.OverviewDao
import cz.legat.core.persistence.SavedBook
import cz.legat.core.persistence.SavedBookDao
import cz.legat.core.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao,
    private val overviewDao: OverviewDao
) : BooksRepository, BaseRepository() {

    private val lastComments = mutableListOf<Comment>()

    override suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }

    override fun getPopularBooks(): Flow<List<Book>> {
        return flow {
            when (val result = apiCall { booksService.getPopularBooks() }) {
                is NetworkResult.Success -> {
                    val popular = HtmlParser().parseBooks(result.data)
                    emit(popular.dropLast(1))
                }
                is NetworkResult.Error -> emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getNewBooks(): Flow<List<Book>> {
        return flow {
            when (val result = apiCall { booksService.getNewBooks() }) {
                is NetworkResult.Success -> {
                    val new = PARSER.parseBooks(result.data)
                    emit(new.dropLast(1))
                }
                is NetworkResult.Error -> emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getEBooks(): Flow<List<Book>> {
        return flow {
            when (val result = apiCall { booksService.getEBooks() }) {
                is NetworkResult.Success -> {
                    val ebooks = PARSER.parseEBooks(result.data)
                    emit(ebooks.dropLast(2))
                }
                is NetworkResult.Error -> emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getBook(id: String?): Flow<Book?> {
        return flow {
            if (id == null) {
                emit(null)
            } else {
                when (val result = apiCall { booksService.getBook(id) }) {
                    is NetworkResult.Success -> {
                        val book = PARSER.parseBook(id, result.data)
                        val updatedBook = book.copy(
                            description = if (book.description!!.contains("Popis knihy zde zatím bohužel není.") || !book.description!!.contains(
                                    "celý text"
                                )
                            ) book.description else book.description!!.dropLast(12)
                        )
                        emit(updatedBook)
                    }
                    is NetworkResult.Error -> emit(null)
                }
            }
        }
    }

    override fun getBookComments(id: String, page: Int): List<Comment> {
        return runBlocking {
            when (val result = apiCall { booksService.getBookComments(id = id, page = page) }) {
                is NetworkResult.Success -> {
                    val comments = PARSER.parseBookComments(result.data)
                    if (lastComments == comments && page > 1) {
                        listOf()
                    } else {
                        lastComments.clear()
                        lastComments.addAll(comments)
                        comments
                    }
                }
                is NetworkResult.Error -> listOf()
            }
        }
    }

    override fun getBookByISBN(isbn: String): Flow<SavedBook> {
        return flow {
            when (val result = apiCall { booksService.getBookByISBN(isbn) }) {
                is NetworkResult.Success -> {
                    val book = PARSER.parseBook(isbn, result.data)
                    emit(
                        SavedBook(
                            bookId = book.id,
                            title = book.title,
                            author = book.author?.name,
                            isbn = isbn,
                            publishedDate = book.published,
                            pageCount = book.numberOfPages,
                            language = book.language
                        )
                    )
                }
                is NetworkResult.Error -> emit(SavedBook(isbn = isbn))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun searchBook(query: String): Flow<List<SearchResult>> {
        return flow {
            when (val result = apiCall { booksService.searchBook(query) }) {
                is NetworkResult.Success -> {
                    val searchBooks = PARSER.parseBookSearchResults(result.data)
                    emit(searchBooks.filter {
                        it.getResultTitle().toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))
                    })
                }
                is NetworkResult.Error -> emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    override fun removeBook(book: SavedBook) {
        return savedBookDao.delete(book)
    }
}