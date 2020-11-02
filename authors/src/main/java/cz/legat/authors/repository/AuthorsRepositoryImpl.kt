package cz.legat.authors.repository

import cz.legat.authors.data.remote.AuthorsService
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.repository.AuthorsRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class AuthorsRepositoryImpl @Inject constructor(private val authorsService: AuthorsService) :
    AuthorsRepository, BaseRepository() {

    private val authorCache = hashMapOf<String, Author>()

    override fun getAuthors(page: Int, id: String?): List<Author> {
        return runBlocking {
            when (val result = apiCall { authorsService.getAuthors(page, id) }) {
                is NetworkResult.Success -> {
                    val authors = PARSER.parseAuthors(result.data)
                    authors
                }
                is NetworkResult.Error -> listOf()
            }
        }
    }

    override fun getAuthorBooks(page: Int, id: String?): List<Book> {
        if (id == null) {
            return listOf()
        }
        return runBlocking {
            when (val result =
                apiCall { authorsService.getAuthorBooks(authorId = id, page = page, sort = "rn") }) {
                is NetworkResult.Success -> {
                    val books = PARSER.parseAuthorBooks(result.data, page)
                    books
                }
                is NetworkResult.Error -> listOf()
            }
        }
    }

    override suspend fun getAuthor(id: String): Author? {
        if (authorCache.containsKey(id)) {
            return authorCache[id]
        }
        return when (val result = apiCall { authorsService.getAuthor(id) }) {
            is NetworkResult.Success -> {
                val author = PARSER.parseAuthorDetail(id, result.data)
                authorCache[id] = author
                author
            }
            is NetworkResult.Error -> null
        }
    }

    override suspend fun searchAuthor(query: String): List<Author> {
        return when (val result = apiCall { authorsService.searchAuthor(query) }) {
            is NetworkResult.Success -> PARSER.parseAuthors(result.data)
            is NetworkResult.Error -> listOf()
        }
    }
}