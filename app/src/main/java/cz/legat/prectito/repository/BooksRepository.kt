package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import javax.inject.Inject

class BooksRepository @Inject constructor(private val booksService: BooksService) {

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
}