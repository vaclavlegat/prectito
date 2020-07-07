package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Book
import javax.inject.Inject

class BooksRepository @Inject constructor(private val booksService: BooksService){

    suspend fun getPopularBooks(): List<Book>{
        return booksService.getPopularBooks()
    }
}