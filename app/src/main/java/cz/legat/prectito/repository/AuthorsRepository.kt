package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Author
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepository @Inject constructor(private val booksService: BooksService) {

    fun getAuthors(page: Int): List<Author> {
        return runBlocking {
            val response = booksService.getAuthors(page)
            response.body()!!
        }
    }

    suspend fun getAuthor(id: String?): Author? {
        id?.let {
            val response = booksService.getAuthor(id)
            return response.body()!!
        }
        return null
    }
}