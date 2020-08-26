package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.api.GoogleBooksService
import cz.legat.prectito.model.Book
import cz.legat.prectito.model.google.VolumeInfo
import cz.legat.prectito.persistence.SavedBook
import cz.legat.prectito.persistence.SavedBookDao
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksService: BooksService,
    private val googleBooksService: GoogleBooksService,
    private val savedBookDao: SavedBookDao
) {

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

    suspend fun getBook(id: String): Book {
        return booksService.getBook(id)
    }

    suspend fun getBookByISBN(isbn: String): VolumeInfo? {
        return try {
            val response = googleBooksService.getBookByISBN("isbn:$isbn")
            response.body()!!.items[0].volumeInfo.apply {
                id = response.body()!!.items[0].id
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveBook(savedBook: SavedBook) {
        savedBookDao.insertAll(savedBook)
    }

    suspend fun getMyBooks(): List<SavedBook> {
        return savedBookDao.getAll()
    }
}