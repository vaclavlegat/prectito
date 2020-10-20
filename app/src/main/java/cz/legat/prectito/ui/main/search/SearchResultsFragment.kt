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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.core.model.SearchResult
import cz.legat.prectito.databinding.PtSearchResultsFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visibleIf
import cz.legat.prectito.ui.main.BindingFragment
import cz.legat.prectito.ui.main.base.BaseAdapter
import cz.legat.prectito.ui.main.books.BooksFragment
import cz.legat.prectito.ui.main.books.BooksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultsFragment : BindingFragment<PtSearchResultsFragmentBinding>(PtSearchResultsFragmentBinding::inflate) {

    private val viewModel: BooksViewModel by viewModels()

    lateinit var booksAdapter: SearchResultsAdapter
    private var handler = Handler()
    private var callback: OnResultCallback? = null

    interface OnResultCallback {
        fun onResult(id: String, isBook: Boolean)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as SearchResultsActivity
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        fun newInstance() = BooksFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter =
            SearchResultsAdapter(object :
                BaseAdapter.OnItemClickedListener<SearchResult> {
                override fun onItem(item: SearchResult) {
                    hideKeyboard(requireContext())
                    callback?.onResult(item.getResultId(), item.isBook())
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
        binding.ptProgress.visibleIf(!binding.ptSearchEt.text.isNullOrEmpty())
        viewModel.searchBook(binding.ptSearchEt.text.toString())
        viewModel.searchBooks.observe(viewLifecycleOwner, Observer<List<SearchResult>> {
            booksAdapter.update(it)
            binding.ptProgress.gone()
        })
        binding.ptSearchEt.requestFocus()
        showKeyboard(requireContext())
    }

    override fun onResume() {
        super.onResume()
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
