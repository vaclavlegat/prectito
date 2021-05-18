package cz.legat.authors.repository

import cz.legat.authors.data.remote.AuthorsService
import cz.legat.core.base.BaseRepository
import cz.legat.core.base.NetworkResult
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.model.SearchResult
import cz.legat.core.repository.AuthorsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

class AuthorsRepositoryImpl @Inject constructor(private val authorsService: AuthorsService) :
    AuthorsRepository, BaseRepository() {

    private val authorCache = hashMapOf<String, Author>()

    override fun getAuthors(page: Int, id: String?): List<Author> {
        return runBlocking {
            when (val result = apiCall { authorsService.getAuthors(pageNumber = page, nid = id) }) {
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
                apiCall {
                    authorsService.getAuthorBooks(
                        authorId = id,
                        page = page,
                        sort = "rn"
                    )
                }) {
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

    override fun searchAuthor(query: String): Flow<List<SearchResult>> {
        return flow {
            when (val result = apiCall { authorsService.searchAuthor(query) }) {
                is NetworkResult.Success -> emit(PARSER.parseAuthors(result.data).filter {
                    it.getResultTitle().toLowerCase(Locale.getDefault())
                        .contains(query.toLowerCase(Locale.getDefault()))
                })
                is NetworkResult.Error -> emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }
}