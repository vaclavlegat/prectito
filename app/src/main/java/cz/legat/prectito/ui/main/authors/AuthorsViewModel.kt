package cz.legat.prectito.ui.main.authors

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.legat.core.model.Author
import cz.legat.prectito.repository.AuthorsRepository
import cz.legat.prectito.ui.main.paging.BaseDataSourceFactory
import cz.legat.prectito.ui.main.paging.BasePageKeyedDataSource

class AuthorsViewModel @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository
) : ViewModel() {

    private val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(40)
        .setPageSize(40)
        .setEnablePlaceholders(false)
        .build()
    val authors: LiveData<PagedList<Author>> = initializedPagedListBuilder(config).build()

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Author> {
        val dataSourceFactory =
            BaseDataSourceFactory(BasePageKeyedDataSource<Author>(authorsRepository::getAuthors))
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}
