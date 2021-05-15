package cz.legat.scanner.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import cz.legat.core.extensions.gone
import cz.legat.core.extensions.visible
import cz.legat.core.extensions.visibleIf
import cz.legat.core.persistence.SavedBook
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import cz.legat.navigation.MyBooksNavigator
import cz.legat.scanner.databinding.PtMyBooksFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import javax.inject.Inject


@AndroidEntryPoint
class MyBooksFragment :
    BindingFragment<PtMyBooksFragmentBinding>(PtMyBooksFragmentBinding::inflate) {

    private val viewModel: ISBNViewModel by viewModels()
    lateinit var booksAdapter: MyBooksAdapter

    @Inject
    lateinit var navigator: MyBooksNavigator

    @Inject
    lateinit var booksNavigator: BooksNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptAppbar.applyInsetter {
            type(ime = true, statusBars = true, navigationBars = true) {
                padding(left = true, top = true, right = true, bottom = false)
            }
            consume(true)
        }
        booksAdapter = MyBooksAdapter(object :
            MyBooksAdapter.OnBookClickedListener {
            override fun onBook(book: SavedBook) {
                book.bookId?.let {
                    startActivity(booksNavigator.getOpenDetailIntent(requireContext(), it))
                }
            }

            override fun onBookRemoved(book: SavedBook) {
                viewModel.removeBook(book)
            }
        })
        binding.ptBooksRw.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = booksAdapter
            setHasFixedSize(true)
        }

        binding.addBtn.setOnClickListener {
            addBook()
        }

        binding.ptAddBookBtn.setOnClickListener {
            addBook()
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                booksAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.ptBooksRw)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.ptProgress.visible()

        viewModel.myBooks.observe(viewLifecycleOwner, Observer { books ->
            binding.ptProgress.gone()
            var count = 0
            for (book in books) {
                count++
                println("${book.title} - ${book.author}, ${book.publishedDate?.take(4)} ISBN: ${book.isbn}")
            }
            println("Books Count $count")
            booksAdapter.update(books)
            binding.ptEmptyContainer.visibleIf(books.isEmpty())
        })
    }

    private fun addBook() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    startActivity(navigator.getOpenScannerIntent(requireContext()))
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {

                }
            }).check()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyBooks()
    }
}
