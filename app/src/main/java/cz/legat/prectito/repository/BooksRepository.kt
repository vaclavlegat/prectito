package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.Comment
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.persistence.SavedBookDao
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val savedBookDao: SavedBookDao
) {

    suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }

    suspend fun getPopularBooks(): List<Book> {
        return try {
            val response = booksService.getPopularBooks()
            response.body()!!
        } catch (e: Exception) {
            getPopularBooks()
        }
    }

    suspend fun getNewBooks(): List<Book> {
        return try {
            val response = booksService.getNewBooks()
            response.body()!!
        } catch (e: Exception) {
            getNewBooks()
        }
    }

    suspend fun getBook(id: String?): Book? {
        if (id == null) {
            return null
        }

        val book = booksService.getBook(id)

        return book.copy(
            description = book.description.dropLast(12)
        )
    }

    suspend fun getBookComments(id: String?): List<Comment> {
        if (id == null) {
            return listOf()
        }
        return booksService.getBookComments(id)
    }

    suspend fun getBookByISBN(isbn: String): SavedBook? {
        return try {
            val book = booksService.getBookByISBN(isbn)
            return SavedBook(
                title = book.title,
                author = book.author?.name,
                isbn = isbn,
                publishedDate = book.published,
                pageCount = book.numberOfPages,
                language = book.language
            )
        } catch (e: Exception) {
            SavedBook(isbn = isbn)
        }
    }

    suspend fun searchBook(query: String): List<Book> {
        return try {
            booksService.searchBook(query).take(10)
        } catch (e: Exception) {
            listOf()
        }
    }

    suspend fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    suspend fun removeBook(book: SavedBook) {
        return savedBookDao.delete(book)
    }
}