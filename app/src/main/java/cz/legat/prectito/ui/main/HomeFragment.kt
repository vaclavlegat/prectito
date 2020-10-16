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
        binding.ptSearchFab.setOnClickListener {
            startActivityForResult(Intent(requireContext(), SearchResultsActivity::class.java), RESULT_ID)
        }

        popularBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(
                        item.id
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })

        binding.ptPopularBooksRv.initLinear(popularBooksAdapter)

        newBooksAdapter = BooksAdapter(object : BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(
                        item.id
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })

        binding.ptNewBooksRv.initLinear(newBooksAdapter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_ID && resultCode == Activity.RESULT_OK) {
            val id = data?.extras?.getString("SEARCH_RESULT_ID_KEY")?:""
            val isBook = data?.extras?.getBoolean("SEARCH_RESULT_TYPE_KEY")
            findNavController().navigate(if (isBook == true) {
                HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(
                    id
                )
            } else {
                HomeFragmentDirections.actionHomeFragmentToAuthorsFragment(
                    id
                )
            })
        }
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
