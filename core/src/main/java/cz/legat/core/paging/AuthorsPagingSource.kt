package cz.legat.core.paging

import androidx.paging.PagingSource
import cz.legat.core.model.Author
import cz.legat.core.repository.AuthorsRepository

class AuthorsPagingSource(val authorsRepository: AuthorsRepository, val query:String?) : PagingSource<Int, Author>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Author> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = authorsRepository.getAuthors(nextPageNumber, query)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}