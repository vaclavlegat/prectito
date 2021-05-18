package cz.legat.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.core.base.BaseAdapter
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visibleIf
import cz.legat.core.model.SearchResult
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.AuthorsNavigator
import cz.legat.navigation.BooksNavigator
import cz.legat.search.databinding.PtSearchResultsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchResultsFragment :
    BindingFragment<PtSearchResultsFragmentBinding>(PtSearchResultsFragmentBinding::inflate) {

    private val viewModel: SearchResultsViewModel by viewModels()

    @Inject
    lateinit var booksNavigator: BooksNavigator
    @Inject
    lateinit var authorsNavigator: AuthorsNavigator

    lateinit var booksAdapter: SearchResultsAdapter

    interface OnResultCallback {
        fun onResult(id: String, isBook: Boolean)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter =
            SearchResultsAdapter(object :
                BaseAdapter.OnItemClickedListener<SearchResult> {
                override fun onItem(item: SearchResult) {
                    hideKeyboard(requireContext())
                    val intent = if (item.isBook()) {
                        booksNavigator.getOpenDetailIntent(requireContext(), item.getResultId())
                    } else {
                        authorsNavigator.getOpenDetailIntent(requireContext(), item.getResultId())
                    }
                    startActivity(intent)
                }
            })

        binding.ptBooksRw.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }

        setupSearchInput()

        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            booksAdapter.update(it)
            binding.ptProgress.gone()
        })
    }

    private fun setupSearchInput() = with(binding) {
        searchInput.editText?.apply {
            setText("")

            doAfterTextChanged {
                lifecycleScope.launch {
                    viewModel.queryChannel.send(it.toString())
                }
            }
        }

        searchInput.setEndIconOnClickListener {
            searchInput.editText?.apply {
                setText("")
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptProgress.visibleIf(!binding.searchInput.editText.toString().isNullOrEmpty())
        binding.searchInput.requestFocus()
        showKeyboard(requireContext())
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
