package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import javax.inject.Inject

class BooksRepository @Inject constructor(private val booksService: BooksService) {

    suspend fun getPopularBooks(): List<Book> {
        val response = booksService.getPopularBooks()
        return if (response.isSuccessful) {
            booksService.getPopularBooks().body()!!
        } else {
            getPopularBooks()
        }
    }

    suspend fun getNewBooks(): List<Book> {
        val response = booksService.getNewBooks()
        return if (response.isSuccessful) {
            booksService.getNewBooks().body()!!
        } else {
            getNewBooks()
        }
    }

    suspend fun getBook(id: String): Book {
        return booksService.getBook(id)
    }
}