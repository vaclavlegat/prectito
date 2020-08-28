package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.prectito.R
import cz.legat.prectito.model.Book
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val viewModel: BooksViewModel by viewModels()

    lateinit var pager: ViewPager2
    lateinit var tabs: TabLayout

    companion object {
        fun newInstance(book: Book) : DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("book", book)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_detail_tabs_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager = view.findViewById(R.id.pt_detail_pager)
        tabs = view.findViewById(R.id.pt_detail_tabs)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val book = arguments?.getParcelable<Book>("book")
        pager.adapter = DetailAdapter(this, Book(id = "id", title = book?.title!!, imgLink = book.imgLink, description = book.description))
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = "Info"
        }.attach()
    }

    class DetailAdapter(fragment: Fragment, val book: Book) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 1

        override fun createFragment(position: Int): Fragment {
            return BookDetailFragment.newInstance(book)
        }
    }
}
