package cz.legat.books.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.books.R
import cz.legat.books.databinding.PtHomeFragmentBinding
import cz.legat.core.ui.BindingFragment
import cz.legat.navigation.BooksNavigator
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : BindingFragment<PtHomeFragmentBinding>(PtHomeFragmentBinding::inflate) {

    @Inject
    lateinit var navigator: BooksNavigator

    private val viewModel: OverviewViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ptAppbar.applyInsetter {
            type(ime = true, statusBars = true, navigationBars = true) {
                padding(left = true, top = true, right = true, bottom = false)
            }
            consume(true)
        }
        val tabsAdapter = TabsAdapter(this)
        binding.pager.adapter = tabsAdapter
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, binding.pager) { tab, position ->
            tab.text = fragmentTitles()[position]
        }.attach()
    }

    private fun fragmentTitles(): List<String> {
        return listOf(
            getString(R.string.pt_popular_books),
            getString(R.string.pt_new_books),
            getString(R.string.pt_ebooks)
        )
    }

    private fun fragments(): List<Fragment> {
        return listOf(BooksFragment().apply {
            arguments = Bundle().apply {
                putString("type", POPULAR)
            }
        }, BooksFragment().apply {
            arguments = Bundle().apply {
                putString("type", NEW)
            }
        }, BooksFragment().apply {
            arguments = Bundle().apply {
                putString("type", EBOOK)
            }
        })
    }

    inner class TabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments().size

        override fun createFragment(position: Int): Fragment {
            return fragments()[position]
        }
    }
}
