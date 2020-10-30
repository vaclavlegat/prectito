package cz.legat.authors

import android.view.View
import android.widget.TextView
import cz.legat.core.base.BaseAdapter
import cz.legat.core.model.Countries

class FilterAdapter(private val onItemClickedListener: OnItemClickedListener<Countries>) :
    BaseAdapter<Countries>(onItemClickedListener) {

    override fun layout(): Int {
        return R.layout.pt_item_country
    }

    override fun viewHolder(view: View): BaseViewHolder {
        return CountryViewHolder(view)
    }

    inner class CountryViewHolder(view: View) : BaseAdapter<Countries>.BaseViewHolder(view) {

        private val countryTv = view.findViewById<TextView>(R.id.pt_country_name)

        override fun bind(item: Countries, position: Int) {
            view.setBackgroundResource(if (position % 2 == 0) R.drawable.pt_item_ripple else R.drawable.pt_item_light_ripple)
            with(item) {
                countryTv.text = item.country
            }
        }
    }
}