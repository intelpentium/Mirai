package fathurrahman.projeku.mirai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fathurrahman.projeku.mirai.databinding.GeneralItemBinding
import fathurrahman.projeku.mirai.services.entity.GeneralItem
import fathurrahman.projeku.mirai.services.entity.ResponseGalleryModel

class AdapterItemMain(
    private val subItemData: List<ResponseGalleryModel>,
) : RecyclerView.Adapter<AdapterItemMain.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            GeneralItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(subItemData[position])
    }

    override fun getItemCount(): Int {
        return subItemData.size
    }

    inner class ViewHolder(private val binding: GeneralItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(item: ResponseGalleryModel) {
            binding.imageView.setImageBitmap(item.bitmap)
        }
    }
}