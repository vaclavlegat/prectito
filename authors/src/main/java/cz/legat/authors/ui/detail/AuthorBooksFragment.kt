package cz.legat.authors.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.authors.databinding.PtMainFragmentBinding
import cz.legat.core.model.Book
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.extensions.withId
//import cz.legat.prectito.navigation.goToBookDetailIntent
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthorBooksFragment : BindingFragment<PtMainFragmentBinding>(PtMainFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()
    lateinit var booksAdapter: AuthorBooksAdapter
    @Inject lateinit var booksNavigator: BooksNavigator

    companion object {
        fun newInstance(id: String) = AuthorBooksFragment().withId(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = AuthorBooksAdapter(object :
            AuthorBooksAdapter.OnBookClickedListener {
            override fun onBook(book: Book) {
                startActivity(booksNavigator.getOpenDetailIntent(requireContext(), book.id))
            }
        })

        binding.ptBooksRw.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptProgress.visible()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.booksFlow.collectLatest { pagingData ->
                binding.ptProgress.gone()
                booksAdapter.submitData(pagingData)
            }
        }
    }
}