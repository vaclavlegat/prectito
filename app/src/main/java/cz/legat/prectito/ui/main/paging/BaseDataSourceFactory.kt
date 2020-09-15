package cz.legat.prectito.ui.main.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class BaseDataSourceFactory<T>(private val dataSource: DataSource<Int, T>) : DataSource.Factory<Int, T>() {
    private val authorsDataSourceLiveData: MutableLiveData<DataSource<Int, T>> = MutableLiveData()

    override fun create(): DataSource<Int, T> {
        authorsDataSourceLiveData.postValue(dataSource)
        return dataSource
    }

    fun getAuthorsDataSource(): DataSource<Int, T> {
        return dataSource
    }
}
