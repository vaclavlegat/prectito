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
import cz.legat.prectito.databinding.PtSearchResultsFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.ui.main.BindingFragment
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.BooksFragment
import cz.legat.prectito.ui.main.books.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : BindingFragment<PtSearchResultsFragmentBinding>() {

    private val viewModel: BooksViewModel by viewModels()
    val args: SearchResultsFragmentArgs by navArgs()

    lateinit var booksAdapter: SearchResultsAdapter
    private var handler = Handler()

    companion object {
        fun newInstance() = BooksFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PtSearchResultsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter =
            SearchResultsAdapter(object :
                BaseAdapter.OnItemClickedListener<cz.legat.core.model.Book> {
                override fun onItem(item: cz.legat.core.model.Book) {
                    hideKeyboard(requireContext())
                    val action =
                        SearchResultsFragmentDirections.actionSearchResultsFragmentToBookDetailFragment(
                            item.id
                        )
                    findNavController().navigate(action)
                }
            })

        binding.ptBooksRw.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }

        binding.ptSearchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(Runnable {
                    query?.let {
                        viewModel.searchBook(it.toString())
                    }
                }, 300)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptProgress.visible()
        viewModel.searchBook(args.query)
        viewModel.searchBooks.observe(viewLifecycleOwner, Observer<List<cz.legat.core.model.Book>> {
            booksAdapter.update(it)
            binding.ptProgress.gone()
        })
    }

    override fun onResume() {
        super.onResume()
        if (binding.ptSearchEt.text.isNullOrEmpty()) {
            binding.ptSearchEt.requestFocus()
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
