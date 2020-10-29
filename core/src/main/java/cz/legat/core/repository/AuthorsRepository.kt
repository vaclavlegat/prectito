package cz.legat.core.repository

import cz.legat.core.api.BooksService
import cz.legat.core.base.BaseRepository
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepository @Inject constructor(private val booksService: BooksService) :
    BaseRepository() {

    private val authorCache = hashMapOf<String, Author>()
    private val authorsBooksCache = hashMapOf<String, HashMap<Int, List<Book>>>()
    private val authorsCache = hashMapOf<Int, List<Author>>()

    fun getAuthors(page: Int, id: String? = null): List<Author> {
        return runBlocking {
            when (val result = apiCall { booksService.getAuthors(page, id) }) {
                is cz.legat.core.base.Result.Success -> {
                    val authors = PARSER.parseAuthors(result.data)
                    authors
                }
                is cz.legat.core.base.Result.Error -> listOf()
            }
        }
    }

    fun getAuthorBooks(page: Int, id: String): List<Book> {
        return runBlocking {
            when (val result =
                apiCall { booksService.getAuthorBooks(authorId = id, page = page, sort = "rn") }) {
                is cz.legat.core.base.Result.Success -> {
                    val books = PARSER.parseAuthorBooks(result.data, page)
                    books
                }
                is cz.legat.core.base.Result.Error -> listOf()
            }
        }
    }

    suspend fun getAuthor(id: String): Author? {
        if (authorCache.containsKey(id)) {
            return authorCache[id]
        }
        return when (val result = apiCall { booksService.getAuthor(id) }) {
            is cz.legat.core.base.Result.Success -> {
                val author = PARSER.parseAuthorDetail(id, result.data)
                authorCache[id] = author
                author
            }
            is cz.legat.core.base.Result.Error -> null
        }
    }

    suspend fun searchAuthor(query: String): List<Author> {
        return when (val result = apiCall { booksService.searchAuthor(query) }) {
            is cz.legat.core.base.Result.Success -> PARSER.parseAuthors(result.data)
            is cz.legat.core.base.Result.Error -> listOf()
        }
    }
}