package cz.legat.prectito.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import cz.legat.core.model.Book
import cz.legat.prectito.databinding.PtHomeFragmentBinding
import cz.legat.prectito.extensions.initLinear
import cz.legat.prectito.extensions.initLinearPaged
import cz.legat.prectito.ui.main.authors.AuthorsAdapter
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.BooksAdapter
import cz.legat.prectito.ui.main.books.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BindingFragment<PtHomeFragmentBinding>() {

    interface TabChangeListener {
        fun onTabChanged(position: Int)
    }

    private val viewModel: BooksViewModel by viewModels()
    private var popularBooksAdapter: BooksAdapter? = null
    private var newBooksAdapter: BooksAdapter? = null
    private var authorsAdapter: AuthorsAdapter? = null

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

        authorsAdapter = AuthorsAdapter(object : AuthorsAdapter.OnAuthorClickedListener {
            override fun onAuthor(author: cz.legat.core.model.Author) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAuthorsFragment(
                        author.authorId!!
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })


        binding.ptAuthorsRv.initLinearPaged(authorsAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.popularBooks.observe(viewLifecycleOwner, Observer {
            popularBooksAdapter?.update(it)
        })

        viewModel.newBooks.observe(viewLifecycleOwner, Observer {
            newBooksAdapter?.update(it)
        })

        viewModel.authors.observe(viewLifecycleOwner, Observer {
            authorsAdapter?.submitList(it)
        })
    }
}
