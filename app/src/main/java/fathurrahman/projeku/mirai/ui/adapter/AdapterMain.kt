package fathurrahman.projeku.mirai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fathurrahman.projeku.mirai.databinding.DateItemBinding
import fathurrahman.projeku.mirai.databinding.HeaderItemBinding
import fathurrahman.projeku.mirai.services.entity.DateItem
import fathurrahman.projeku.mirai.services.entity.GeneralItem
import fathurrahman.projeku.mirai.services.entity.ListItem

class AdapterMain (
//    private val items: List<ListItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<ListItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ListItem.TYPE_DATE ->
                DateViewHolder(DateItemBinding.inflate(layoutInflater))
            else ->
                GeneralViewHolder(HeaderItemBinding.inflate(layoutInflater))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ListItem.TYPE_DATE -> (holder as DateViewHolder).bind(
                item = items[position] as DateItem,
            )
            ListItem.TYPE_GENERAL -> {
                (holder as GeneralViewHolder).bind(
                    item = items[position] as GeneralItem
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class DateViewHolder(val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DateItem) {
            binding.txtDate.text = item.date
        }
    }

    inner class GeneralViewHolder(val binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GeneralItem) {

            binding.rvSub.adapter = AdapterItemMain(item.listItem)
        }
    }

}