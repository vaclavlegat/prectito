package cz.legat.prectito.ui.main.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.prectito.R
import cz.legat.prectito.model.Author
import cz.legat.prectito.model.Book
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.BooksFragment
import cz.legat.prectito.ui.main.books.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAuthorResultsFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()
    val args: SearchResultsFragmentArgs by navArgs()

    lateinit var booksRv: RecyclerView
    lateinit var authorsAdapter: SearchAuthorResultsAdapter
    lateinit var progress: ProgressBar
    lateinit var searchET: EditText
    private var handler = Handler()

    companion object {
        fun newInstance() = BooksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_search_results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authorsAdapter =
            SearchAuthorResultsAdapter(object : BaseAdapter.OnItemClickedListener<Author> {
                override fun onItem(item: Author) {
                    hideKeyboard(requireContext())
                    val action =
                        SearchAuthorResultsFragmentDirections.actionSearchAuthorsResultsFragmentToDetailFragment(
                            item.authorId!!
                        )
                    findNavController().navigate(action)
                }
            })
        booksRv = view.findViewById(R.id.pt_books_rw)
        progress = view.findViewById(R.id.pt_progress)
        searchET = view.findViewById(R.id.pt_search_et)
        booksRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = authorsAdapter
            setHasFixedSize(true)
        }

        searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                handler.removeCallbacksAndMessages(null)

                handler.postDelayed(Runnable {
                    query?.let {
                        viewModel.searchAuthor(it.toString())
                    }
                }, 300)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress.visibility = View.VISIBLE
        viewModel.searchAuthor(args.query)
        progress.visibility = View.VISIBLE
        viewModel.searchAuthors.observe(viewLifecycleOwner, Observer<List<Author>> {
            authorsAdapter.update(it)
            progress.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        if (searchET.text.isNullOrEmpty()) {
            searchET.requestFocus()
            showKeyboard(requireContext())
        }
    }

    private fun showKeyboard(context: Context) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    private fun hideKeyboard(context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}