package cz.legat.prectito.ui.main.authors.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import cz.legat.core.model.Book
import cz.legat.prectito.repository.AuthorsRepository
import cz.legat.prectito.ui.main.paging.BaseDataSourceFactory
import cz.legat.prectito.ui.main.paging.BasePageKeyedDataSourceWithParam
import kotlinx.coroutines.Dispatchers

class AuthorDetailViewModel  @ViewModelInject constructor(
    private val authorsRepository: AuthorsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val author = liveData(Dispatchers.IO) {
        savedStateHandle.get<String>("id")?.let {
            emit(authorsRepository.getAuthor(it))
        }
    }

    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(40)
        .setPageSize(40)
        .setEnablePlaceholders(false)
        .build()
    val books = initializedPagedListBuilder(savedStateHandle.get<String>("id")!!, config).build()


    private fun initializedPagedListBuilder(authorId: String, config: PagedList.Config):
            LivePagedListBuilder<Int, Book> {
        val dataSourceFactory = BaseDataSourceFactory(BasePageKeyedDataSourceWithParam<Book>(authorId, authorsRepository::getAuthorBooks))
        return LivePagedListBuilder(dataSourceFactory, config)
    }

}
