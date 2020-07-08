package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import javax.inject.Inject

class BooksRepository @Inject constructor(private val booksService: BooksService) {

    suspend fun getPopularBooks(): List<Book> {
        return booksService.getPopularBooks()
    }

    suspend fun getNewBooks(): List<Book> {
        return booksService.getNewBooks()
    }

    suspend fun getBook(id: String): Book {
        return booksService.getBook(id)
    }
}