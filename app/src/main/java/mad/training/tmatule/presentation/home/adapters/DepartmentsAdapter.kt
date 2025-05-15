package mad.training.tmatule.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mad.training.network.model.department.Department
import mad.training.uikit.databinding.ItemDepartmentBinding

class DepartmentsAdapter :
    ListAdapter<Department, DepartmentsAdapter.DepartmentViewHolder>(DepartmentsChecker()) {


    inner class DepartmentViewHolder(private val binding: ItemDepartmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(department: Department) {
            binding.departmentName.text = department.name
        }

    }

    class DepartmentsChecker : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val view = ItemDepartmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}