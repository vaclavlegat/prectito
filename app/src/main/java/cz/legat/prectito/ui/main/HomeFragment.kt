package cz.legat.prectito.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cz.legat.core.model.Book
import cz.legat.core.ui.BindingFragment
import cz.legat.prectito.navigation.goToBookDetailIntent
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.initLinear
import cz.legat.prectito.databinding.PtHomeFragmentBinding
import cz.legat.prectito.ui.main.books.BooksAdapter
import cz.legat.prectito.ui.main.books.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<PtHomeFragmentBinding>(PtHomeFragmentBinding::inflate) {

    interface TabChangeListener {
        fun onTabChanged(position: Int)
    }

    private val viewModel: BooksViewModel by viewModels()
    private var popularBooksAdapter: BooksAdapter? = null
    private var newBooksAdapter: BooksAdapter? = null

    private var onTabChangeListener: TabChangeListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onTabChangeListener = context as TabChangeListener
    }

    override fun onDetach() {
        super.onDetach()
        onTabChangeListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                startActivity(requireContext().goToBookDetailIntent(item.id))
            }
        })

        binding.ptPopularBooksRv.initLinear(popularBooksAdapter)

        newBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                startActivity(requireContext().goToBookDetailIntent(item.id))
            }
        })

        binding.ptNewBooksRv.initLinear(newBooksAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.popularBooks.observe(viewLifecycleOwner, Observer {
            popularBooksAdapter?.update(it)
        })

        viewModel.newBooks.observe(viewLifecycleOwner, Observer {
            newBooksAdapter?.update(it)
        })
    }
}
