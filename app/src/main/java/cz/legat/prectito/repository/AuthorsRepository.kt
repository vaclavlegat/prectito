package cz.legat.prectito.repository

import cz.legat.prectito.api.BooksService
import cz.legat.prectito.model.Author
import cz.legat.prectito.ui.main.base.BaseRepository
import cz.legat.prectito.ui.main.base.Result
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepository @Inject constructor(private val booksService: BooksService) :
    BaseRepository() {

    fun getAuthors(page: Int): List<Author> {
        return runBlocking {
            when (val result = apiCall { booksService.getAuthors(page) }) {
                is Result.Success -> result.data
                is Result.Error -> listOf()
            }
        }
    }

    suspend fun getAuthor(id: String): Author? {
        return when (val result = apiCall { booksService.getAuthor(id) }) {
            is Result.Success -> result.data
            is Result.Error -> null
        }
    }

    suspend fun searchAuthor(query: String): List<Author> {
        return when (val result = apiCall { booksService.searchAuthor(query) }) {
            is Result.Success -> result.data
            is Result.Error -> listOf()
        }
    }
}