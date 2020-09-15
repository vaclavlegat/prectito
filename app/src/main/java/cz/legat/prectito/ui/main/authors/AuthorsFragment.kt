package cz.legat.prectito.ui.main.authors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.model.Author
import cz.legat.prectito.ui.main.HomeFragmentDirections
import cz.legat.prectito.ui.main.books.BOOKS_TYPE_KEY
import cz.legat.prectito.ui.main.books.BooksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorsFragment : Fragment() {

    companion object {
        private const val GRID_COLUMNS = 3
        fun newInstance() = AuthorsFragment()
    }

    private val viewModel: AuthorsViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AuthorsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.pt_fragment_authors, container, false)
        recyclerView = rootView.findViewById(R.id.rv_authors)
        viewManager = GridLayoutManager(activity, GRID_COLUMNS)
        viewAdapter = AuthorsAdapter(object:AuthorsAdapter.OnAuthorClickedListener{
            override fun onAuthor(author: Author) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAuthorsFragment(
                        author.authorId!!
                    )
                findNavController(rootView).navigate(action)
            }
        })

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getAuthors().observe(viewLifecycleOwner, Observer<PagedList<Author>> {
            viewAdapter.submitList(it)
        })
    }
}
