package cz.legat.prectito.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.prectito.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject lateinit var viewModel: BooksViewModel

    lateinit var pager: ViewPager2
    lateinit var tabs: TabLayout

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.pt_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager = view.findViewById(R.id.pt_home_pager)
        tabs = view.findViewById(R.id.pt_home_tabs)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = HomeAdapter(this)
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text =
                getString(when(position) {
                    POPULAR_BOOKS -> R.string.pt_tab_title_popular
                    NEW_BOOKS -> R.string.pt_tab_title_new
                    SCAN_BOOKS -> R.string.pt_tab_title_scan
                    else -> R.string.pt_tab_title_my_books})
        }.attach()
    }

    class HomeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            if(position == 2) {
                return ISBNScannerFragment.newInstance()
            } else if (position == 3) {
                return MyBooksFragment.newInstance()
            }
            return BooksFragment.newInstance(position)
        }
    }
}
