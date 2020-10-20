package cz.legat.prectito.ui.main.authors.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cz.legat.core.model.Book
import cz.legat.prectito.R
import cz.legat.prectito.databinding.PtMainFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.navigation.goToBookDetailIntent
import cz.legat.prectito.ui.main.BindingFragment
import cz.legat.prectito.ui.main.DetailActivity
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorBooksFragment : BindingFragment<PtMainFragmentBinding>() {

    private val viewModel: AuthorDetailViewModel by viewModels()
    lateinit var booksAdapter: AuthorBooksAdapter

    companion object {
        fun newInstance(id: String) = AuthorBooksFragment().apply {
            arguments = Bundle().apply {
                putString("id", id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PtMainFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
