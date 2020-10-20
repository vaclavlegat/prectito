package cz.legat.prectito.ui.main.authors.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cz.legat.core.model.Book
import cz.legat.prectito.databinding.PtMainFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.navigation.goToBookDetailIntent
import cz.legat.prectito.ui.main.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBooksFragment : BindingFragment<PtMainFragmentBinding>(PtMainFragmentBinding::inflate) {

    private val viewModel: AuthorDetailViewModel by viewModels()
    lateinit var booksAdapter: AuthorBooksAdapter

    companion object {
        fun newInstance(id: String) = AuthorBooksFragment().apply {
            arguments = Bundle().apply {
                putString("id", id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        booksAdapter = AuthorBooksAdapter(object :
            AuthorBooksAdapter.OnBookClickedListener {
            override fun onBook(book: Book) {
                startActivity(requireContext().goToBookDetailIntent(book.id))
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
        viewModel.books.observe(viewLifecycleOwner, Observer<PagedList<Book>> { books ->
            binding.ptProgress.gone()
            booksAdapter.submitList(books)
        })
    }
}
