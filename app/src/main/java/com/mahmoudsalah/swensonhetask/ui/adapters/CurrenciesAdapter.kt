package com.mahmoudsalah.swansontask.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.mahmoudsalah.swansontask.R
import com.mahmoudsalah.swansontask.entities.response.Rate
import com.mahmoudsalah.swansontask.listeners.CurrencyAdapterListener
import java.util.*
import javax.inject.Inject


class CurrenciesAdapter @Inject constructor(context: Context /*, @NonNull HomeFragment _view*/) :
    RecyclerView.Adapter<CurrenciesAdapter.CountryViewHolder>() {
    var selectedIndex = -1
    private var collection: List<Rate>? = null
    private val layoutInflater: LayoutInflater
    private val context: Context? = context
    var row_index = -1
    private var onItemClickListener: CurrencyAdapterListener? = null
    override fun getItemCount(): Int {
        return if (collection != null) collection!!.size else 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CountryViewHolder {
        val view: View = layoutInflater.inflate(R.layout.row_currency, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int
    ) {
        val currentModel = collection!![position]

        holder.tv_name!!.text = currentModel.country
        holder.tv_value!!.text = currentModel.value.toString()

        holder.itemView!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (onItemClickListener != null) {
                    notifyDataSetChanged()
                    onItemClickListener!!.onCurrencyClicked(currentModel)
                }
            }
        })
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setTabCollection(_tabCollection: Collection<Rate>?) {
        collection = _tabCollection as List<Rate>?
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: CurrencyAdapterListener) {
        this.onItemClickListener = onItemClickListener
    }

    class CountryViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        @BindView(R.id.tv_name)
        lateinit var tv_name: TextView
        @BindView(R.id.tv_value)
        lateinit var tv_value: TextView
        @BindView(R.id.iv_logo)
        lateinit var ivFlag: ImageView

        init {
            if (itemView != null) {
                ButterKnife.bind(this, itemView)
            }
        }
    }

    init {
        layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        collection = Collections.emptyList()
    }
}