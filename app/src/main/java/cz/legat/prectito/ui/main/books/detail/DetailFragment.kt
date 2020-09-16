package cz.legat.prectito.ui.main.books.detail

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.legat.prectito.ui.main.base.BaseTabsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseTabsFragment() {

    private val args: DetailFragmentArgs by navArgs()

    companion object {
        fun newInstance(): DetailFragment {
            return DetailFragment()
        }
    }

    override fun fragments(): List<Fragment> {
        return listOf(
            BookDetailFragment.newInstance(args.id),
            BookCommentsFragment.newInstance(args.id)
        )
    }

    override fun tabTitles(): List<String> {
        return listOf("Info", "Comments")
    }
}
