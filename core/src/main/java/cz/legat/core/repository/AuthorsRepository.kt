package cz.legat.core.repository

import cz.legat.core.api.BooksService
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepository @Inject constructor(private val booksService: BooksService) :
    BaseRepository() {

    private val authorCache = hashMapOf<String, Author>()

    fun getAuthors(page: Int, id: String? = null): List<Author> {
        return runBlocking {
            when (val result = apiCall { booksService.getAuthors(page, id) }) {
                is NetworkResult.Success -> {
                    val authors = PARSER.parseAuthors(result.data)
                    authors
                }
                is NetworkResult.Error -> listOf()
            }
        }
    }

    fun getAuthorBooks(page: Int, id: String? = null): List<Book> {
        if (id == null) {
            return listOf()
        }
        return runBlocking {
            when (val result =
                apiCall { booksService.getAuthorBooks(authorId = id, page = page, sort = "rn") }) {
                is NetworkResult.Success -> {
                    val books = PARSER.parseAuthorBooks(result.data, page)
                    books
                }
                is NetworkResult.Error -> listOf()
            }
        }
    }

    suspend fun getAuthor(id: String): Author? {
        if (authorCache.containsKey(id)) {
            return authorCache[id]
        }
        return when (val result = apiCall { booksService.getAuthor(id) }) {
            is NetworkResult.Success -> {
                val author = PARSER.parseAuthorDetail(id, result.data)
                authorCache[id] = author
                author
            }
            is NetworkResult.Error -> null
        }
    }

    suspend fun searchAuthor(query: String): List<Author> {
        return when (val result = apiCall { booksService.searchAuthor(query) }) {
            is NetworkResult.Success -> PARSER.parseAuthors(result.data)
            is NetworkResult.Error -> listOf()
        }
    }
}