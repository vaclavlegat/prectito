package cz.legat.core.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class BaseDataSourceFactory<T>(private val dataSource: DataSource<Int, T>) : DataSource.Factory<Int, T>() {
    private val dataSourceLiveData: MutableLiveData<DataSource<Int, T>> = MutableLiveData()

    override fun create(): DataSource<Int, T> {
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun getDataSource(): DataSource<Int, T> {
        return dataSource
    }
}
