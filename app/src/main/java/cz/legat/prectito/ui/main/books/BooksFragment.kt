package cz.legat.prectito.ui.main.books

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import cz.legat.core.model.Book
import cz.legat.prectito.databinding.PtMainFragmentBinding
import cz.legat.prectito.extensions.gone
import cz.legat.prectito.extensions.visible
import cz.legat.prectito.navigation.goToBookDetailIntent
import cz.legat.prectito.ui.main.BindingFragment
import cz.legat.prectito.ui.main.DetailActivity
import cz.legat.prectito.ui.main.base.BaseAdapter
import dagger.hilt.android.AndroidEntryPoint

const val BOOKS_TYPE_KEY = "BOOKS_TYPE_KEY"

@AndroidEntryPoint
class BooksFragment : BindingFragment<PtMainFragmentBinding>() {

    private val viewModel: BooksViewModel by viewModels()
    lateinit var booksAdapter: BooksAdapter

    companion object {
        fun newInstance(booksType: Int) = BooksFragment()
            .apply {
                arguments = Bundle().apply { putInt(BOOKS_TYPE_KEY, booksType) }
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
        booksAdapter = BooksAdapter(object :
            BaseAdapter.OnItemClickedListener<Book> {
            override fun onItem(item: Book) {
               startActivity(requireContext().goToBookDetailIntent(item.id))
            }
        })

        binding.ptBooksRw.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = booksAdapter
            setHasFixedSize(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.ptProgress.visible()
        arguments?.takeIf { it.containsKey(BOOKS_TYPE_KEY) }?.let {
            if (it[BOOKS_TYPE_KEY] as Int == 0) {
                viewModel.popularBooks.observe(viewLifecycleOwner, Observer { books ->
                    binding.ptProgress.gone()
                    booksAdapter.update(books)
                })
            } else {
                viewModel.newBooks.observe(viewLifecycleOwner, Observer { books ->
                    binding.ptProgress.gone()
                    booksAdapter.update(books)
                })
            }
        }
    }
}
