package cz.legat.prectito.repository

import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.prectito.api.BooksService
import cz.legat.prectito.ui.main.base.BaseRepository
import cz.legat.prectito.ui.main.base.Result
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepository @Inject constructor(private val booksService: BooksService) :
    BaseRepository() {

    private val authorCache = hashMapOf<String, Author>()
    private val authorsBooksCache = hashMapOf<String, HashMap<Int, List<Book>>>()
    private val authorsCache = hashMapOf<Int, List<Author>>()

    fun getAuthors(page: Int): List<Author> {
        if (authorsCache.containsKey(page)) {
            return authorsCache[page] ?: listOf()
        }
        return runBlocking {
            when (val result = apiCall { booksService.getAuthors(page) }) {
                is Result.Success -> {
                    val authors = PARSER.parseAuthors(result.data)
                    authorsCache[page] = authors
                    authors
                }
                is Result.Error -> listOf()
            }
        }
    }

    fun getAuthorBooks(page: Int, id: String): List<Book> {
        if (authorsBooksCache.containsKey(id) && authorsBooksCache[id]?.containsKey(page) == true) {
            return authorsBooksCache[id]?.get(page) ?: listOf()
        }
        return runBlocking {
            when (val result =
                apiCall { booksService.getAuthorBooks(authorId = id, page = page, sort = null) }) {
                is Result.Success -> {
                    val books = PARSER.parseAuthorBooks(result.data, page)
                    if (authorsBooksCache[id] == null) {
                        authorsBooksCache[id] = hashMapOf()
                    }
                    authorsBooksCache[id]?.put(page, books)
                    books
                }
                is Result.Error -> listOf()
            }
        }
    }

    suspend fun getAuthor(id: String): Author? {
        if (authorCache.containsKey(id)) {
            return authorCache[id]
        }
        return when (val result = apiCall { booksService.getAuthor(id) }) {
            is Result.Success -> {
                val author = PARSER.parseAuthorDetail(id, result.data)
                authorCache[id] = author
                author
            }
            is Result.Error -> null
        }
    }

    suspend fun searchAuthor(query: String): List<Author> {
        return when (val result = apiCall { booksService.searchAuthor(query) }) {
            is Result.Success -> PARSER.parseAuthors(result.data)
            is Result.Error -> listOf()
        }
    }
}