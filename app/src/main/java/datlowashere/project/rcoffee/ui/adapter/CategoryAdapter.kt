package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.LayoutItemCategoryBinding
import datlowashere.project.rcoffee.data.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            LayoutItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position], position == selectedPosition)
        holder.itemView.setOnClickListener {
            notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            itemClickListener.onItemClick(categories[holder.adapterPosition].category_id)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(private val binding: LayoutItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, isSelected: Boolean) {
            binding.tvCategoryName.text = category.name
            binding.lnContainerCategory.setBackgroundResource(
                if (isSelected) R.drawable.shape_rect_border else R.drawable.shape_rect_filled
            )
        }
    }
}

