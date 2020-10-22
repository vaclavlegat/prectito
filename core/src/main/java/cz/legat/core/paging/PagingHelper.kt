package cz.legat.core.paging

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

val config = PagedList.Config.Builder()
    .setInitialLoadSizeHint(40)
    .setPageSize(40)
    .setEnablePlaceholders(false)
    .build()

fun <T> preparePagingLiveData(param: String, dataProvider: (Int, String) -> List<T>): LiveData<PagedList<T>> {
    val dataSourceFactory = BaseDataSourceFactory(BasePageKeyedDataSourceWithParam<T>(param, dataProvider))
    return LivePagedListBuilder(dataSourceFactory, config).build()
}

fun <T> preparePagingLiveData(dataProvider: (Int) -> List<T>): LiveData<PagedList<T>> {
    val dataSourceFactory = BaseDataSourceFactory(BasePageKeyedDataSource<T>(dataProvider))
    return LivePagedListBuilder(dataSourceFactory, config).build()
}