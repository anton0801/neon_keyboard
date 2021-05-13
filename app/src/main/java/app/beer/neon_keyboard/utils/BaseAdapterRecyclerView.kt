package app.beer.neon_keyboard.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class BaseAdapterRecyclerView<T>(@LayoutRes val resId: Int) :
    RecyclerView.Adapter<BaseAdapterRecyclerView<T>.BaseViewHolder>() {

    private var items: List<T> = arrayListOf()
    private var listener: OnItemClickListener<T>? = null

    interface OnItemClickListener<L> {
        fun onClick(item: L)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        listener = onItemClickListener
    }

    fun setItems(items: List<T>) {
        with(this.items as ArrayList<T>) {
            addAll(items)
        }
        notifyDataSetChanged()
    }

    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: T) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(resId, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

}