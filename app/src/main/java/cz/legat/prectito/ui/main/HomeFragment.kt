package cz.legat.prectito.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import cz.legat.core.model.Book
import cz.legat.prectito.MainActivity
import cz.legat.prectito.RESULT_ID
import cz.legat.prectito.SEARCH_RESULT_ID_KEY
import cz.legat.prectito.SEARCH_RESULT_TYPE_KEY
import cz.legat.prectito.databinding.PtHomeFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.initLinear
import cz.legat.prectito.extensions.initLinearPaged
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.ui.main.authors.AuthorsAdapter
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.BooksAdapter
import cz.legat.prectito.ui.main.books.BooksViewModel
import cz.legat.prectito.ui.main.search.SearchResultsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<PtHomeFragmentBinding>() {

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PtHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popularBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(SEARCH_RESULT_ID_KEY, item.id)
                intent.putExtra(SEARCH_RESULT_TYPE_KEY, true)
                startActivity(intent)
            }
        })

        binding.ptPopularBooksRv.initLinear(popularBooksAdapter)

        newBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(SEARCH_RESULT_ID_KEY, item.id)
                intent.putExtra(SEARCH_RESULT_TYPE_KEY, true)
                startActivity(intent)
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
