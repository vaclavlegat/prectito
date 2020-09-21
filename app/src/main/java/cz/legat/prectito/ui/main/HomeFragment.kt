package cz.legat.prectito.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.core.model.Book
import cz.legat.prectito.R
import cz.legat.prectito.barcode.LiveBarcodeScanningActivity
import cz.legat.prectito.barcode.Utils
import cz.legat.prectito.ui.main.authors.AuthorsAdapter
import cz.legat.prectito.ui.main.authors.AuthorsFragment
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.AUTHORS
import cz.legat.prectito.ui.main.books.BooksAdapter
import cz.legat.prectito.ui.main.books.BooksFragment
import cz.legat.prectito.ui.main.books.BooksViewModel
import cz.legat.prectito.ui.main.books.NEW_BOOKS
import cz.legat.prectito.ui.main.books.POPULAR_BOOKS
import cz.legat.prectito.ui.main.my.MyBooksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    interface TabChangeListener {
        fun onTabChanged(position: Int)
    }

    private val viewModel: BooksViewModel by viewModels()
    lateinit var popularBooksRV: RecyclerView
    private var popularBooksAdapter: BooksAdapter? = null
    lateinit var newBooksRV: RecyclerView
    private var newBooksAdapter: BooksAdapter? = null
    lateinit var authorsRV: RecyclerView
    private var authorsAdapter: AuthorsAdapter? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

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
        return inflater.inflate(R.layout.pt_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularBooksRV = view.findViewById(R.id.pt_popular_books_rv)
        newBooksRV = view.findViewById(R.id.pt_new_books_rv)
        authorsRV = view.findViewById(R.id.pt_authors_rv)

        popularBooksAdapter = BooksAdapter(object :BaseAdapter.OnItemClickedListener<Book>{
            override fun onItem(item: Book) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(
                        item.id
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })

        popularBooksRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularBooksAdapter
        }

        newBooksAdapter = BooksAdapter(object :BaseAdapter.OnItemClickedListener<Book>{
            override fun onItem(item: Book) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(
                        item.id
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })

        newBooksRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = newBooksAdapter
        }

        authorsAdapter = AuthorsAdapter(object:AuthorsAdapter.OnAuthorClickedListener{
            override fun onAuthor(author: cz.legat.core.model.Author) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAuthorsFragment(
                        author.authorId!!
                    )
                Navigation.findNavController(view).navigate(action)
            }
        })


        authorsRV.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = authorsAdapter
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

        viewModel.authors.observe(viewLifecycleOwner, Observer {
            authorsAdapter?.submitList(it)
        })
    }
}
