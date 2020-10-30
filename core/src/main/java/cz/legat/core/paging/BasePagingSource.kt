package cz.legat.core.paging

import androidx.paging.PagingSource

class BasePagingSource<T : Any>(private val dataProvider: (Int, String?) -> List<T>, private val param: String?) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = dataProvider.invoke(nextPageNumber, param)
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