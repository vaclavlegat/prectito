package cz.legat.books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cz.legat.books.databinding.PtHomeFragmentBinding
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.simpleGrid
import cz.legat.core.model.Book
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BindingFragment<PtHomeFragmentBinding>(PtHomeFragmentBinding::inflate) {

    @Inject lateinit var navigator: BooksNavigator

    private val viewModel: BooksViewModel by viewModels()
    private var popularBooksAdapter: BooksAdapter? = null
    private var newBooksAdapter: BooksAdapter? = null
    private var eBooksAdapter: BooksAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookListener = object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                startActivity(navigator.getOpenDetailIntent(requireContext(), item.id))
            }
        }

        popularBooksAdapter = BooksAdapter(bookListener)
        newBooksAdapter = BooksAdapter(bookListener)
        eBooksAdapter = BooksAdapter(bookListener)

        binding.ptPopularBooksRv.simpleGrid(popularBooksAdapter)
        binding.ptNewBooksRv.simpleGrid(newBooksAdapter)
        binding.ptEBooksRv.simpleGrid(eBooksAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.overview.observe(viewLifecycleOwner, Observer {
            popularBooksAdapter?.update(it.popularBooks)
            newBooksAdapter?.update(it.newBooks)
            eBooksAdapter?.update(it.eBooks)
        })
    }
}
