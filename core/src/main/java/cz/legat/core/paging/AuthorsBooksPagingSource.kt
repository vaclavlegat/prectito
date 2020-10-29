package cz.legat.core.paging

import androidx.paging.PagingSource
import cz.legat.core.model.Author
import cz.legat.core.model.Book
import cz.legat.core.repository.AuthorsRepository
import cz.legat.core.repository.BooksRepository

class AuthorsBooksPagingSource(val authorsRepository: AuthorsRepository, val query:String) : PagingSource<Int, Book>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = authorsRepository.getAuthorBooks(nextPageNumber, query)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = if(response.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}