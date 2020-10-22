package cz.legat.core.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList

val config = PagedList.Config.Builder()
    .setInitialLoadSizeHint(40)
    .setPageSize(40)
    .setEnablePlaceholders(false)
    .build()

fun <T> ViewModel.preparePagingLiveData(param: String, dataProvider: (Int, String) -> List<T>): LiveData<PagedList<T>> {
    val dataSourceFactory = BaseDataSourceFactory(BasePageKeyedDataSourceWithParam<T>(param, dataProvider))
    return LivePagedListBuilder(dataSourceFactory, config).build()
}

fun <T> ViewModel.preparePagingLiveData(dataProvider: (Int) -> List<T>): LiveData<PagedList<T>> {
    val dataSourceFactory = BaseDataSourceFactory(BasePageKeyedDataSource<T>(dataProvider))
    return LivePagedListBuilder(dataSourceFactory, config).build()
}