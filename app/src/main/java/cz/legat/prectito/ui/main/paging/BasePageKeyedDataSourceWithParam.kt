package cz.legat.prectito.ui.main.paging

import androidx.paging.PageKeyedDataSource

class BasePageKeyedDataSourceWithParam<T>(val param: String, val call: (page: Int, param: String) -> List<T>?) : PageKeyedDataSource<Int, T>() {

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {

        val currentPage = 1
        val nextPage = currentPage + 1

        val data = call.invoke(currentPage, param)
        data?.let {
            callback.onResult(data, null, nextPage)
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, T>
    ) {

        val currentPage = params.key
        val nextPage = currentPage + 1

        val data = call.invoke(currentPage, param)
        data?.let {
            callback.onResult(data, nextPage)
        }
    }
}
