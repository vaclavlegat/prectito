package cz.legat.prectito.ui.main.authors

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.legat.prectito.model.Author
import cz.legat.prectito.repository.BooksRepository
import cz.legat.prectito.ui.main.paging.BaseDataSourceFactory
import cz.legat.prectito.ui.main.paging.BasePageKeyedDataSource

class AuthorsViewModel @ViewModelInject constructor(
    private val booksRepository: BooksRepository
) : ViewModel() {

    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(40)
        .setPageSize(40)
        .setEnablePlaceholders(false)
        .build()
    val authorsLiveData: LiveData<PagedList<Author>> = initializedPagedListBuilder(config).build()

    fun getAuthors(): LiveData<PagedList<Author>> = authorsLiveData

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Author> {
        val dataSourceFactory =
            BaseDataSourceFactory(BasePageKeyedDataSource<Author>(booksRepository::getAuthors))
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}
