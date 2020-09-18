package cz.legat.prectito.ui.main.authors.detail

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import cz.legat.prectito.ui.main.base.BaseTabsFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AuthorDetailFragment : BaseTabsFragment() {

    private val args: AuthorDetailFragmentArgs by navArgs()

    companion object {
        fun newInstance(): AuthorDetailFragment {
            return AuthorDetailFragment()
        }
    }

    override fun fragments(): List<Fragment> {
        return listOf(AuthorBioFragment.newInstance(args.id), AuthorBooksFragment.newInstance(args.id))
    }

    override fun tabTitles(): List<String> {
        return listOf("Info", "Books")
    }
}
