package com.example.finalproject

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.DividerItemDecoration
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



// "Generic" - can pass in any data type
typealias TapAction<T> = (T, Int) -> Unit

class RecyclerViewAdapter<T> (
    private var itemList: List<T>,
    private var layout: Int,
    private var tapListener: TapAction<T>
): RecyclerView.Adapter<RecyclerViewAdapter.ItemHolder<T>>() {

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        holder.bind(itemList[position], tapListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )

        return ItemHolder(binding)
    }

    fun updateData(newData: List<T>) {
        itemList = newData
        notifyDataSetChanged()
    }

    class ItemHolder<T>(
        private var dataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(item: T, tapAction: TapAction<T>) {
            dataBinding.root.setOnClickListener {
                tapAction(item, layoutPosition)
            }
            // TODO: Create setVariable
            dataBinding.setVariable(BR.viewModel, item)
            dataBinding.executePendingBindings()
        }
    }
}