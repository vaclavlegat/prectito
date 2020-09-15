package cz.legat.prectito.ui.main.authors.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.prectito.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthorDetailFragment : Fragment() {

    lateinit var pager: ViewPager2
    lateinit var tabs: TabLayout

    val args: AuthorDetailFragmentArgs by navArgs()

    companion object {
        fun newInstance(): AuthorDetailFragment {
            return AuthorDetailFragment()
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
        pager.adapter =
            DetailAdapter(
                this,
                args.id
            )
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = "Info"
        }.attach()
    }

    class DetailAdapter(fragment: Fragment, val id: String) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 1

        override fun createFragment(position: Int): Fragment {
            return AuthorBioFragment.newInstance(id)
        }
    }
}
