package cz.legat.prectito

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cz.legat.core.extensions.ID_KEY
import cz.legat.prectito.databinding.PtDetailTabsFragmentBinding
import cz.legat.core.ui.BindingFragment

abstract class BaseTabsFragment : BindingFragment<PtDetailTabsFragmentBinding>(PtDetailTabsFragmentBinding::inflate) {

    protected var id: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        id = arguments?.getString(ID_KEY) ?: throw IllegalArgumentException()
        binding.ptDetailPager.adapter = DetailAdapter(this)
        TabLayoutMediator(binding.ptDetailTabs, binding.ptDetailPager) { tab, position ->
            tab.text = tabTitles()[position]
        }.attach()
    }

    inner class DetailAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = fragments().size

        override fun createFragment(position: Int): Fragment {
            return fragments()[position]
        }
    }

    abstract fun fragments(): List<Fragment>
    abstract fun tabTitles(): List<String>
}