package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datlowashere.project.rcoffee.data.model.Address
import datlowashere.project.rcoffee.databinding.LayoutItemSelectAddressBinding

class AddressAdapter(
    private var addresses: List<Address>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = LayoutItemSelectAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentAddress = addresses[position]
        holder.bind(currentAddress)
    }

    override fun getItemCount() = addresses.size

    fun updateAddresses(newAddresses: List<Address>) {
        addresses = newAddresses
        notifyDataSetChanged()
    }

    inner class AddressViewHolder(private val binding: LayoutItemSelectAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.lnAddressSelected.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(addresses[position])
                }
            }
        }

        fun bind(address: Address) {
            binding.tvItemAdress.text = address.location
        }
    }

    interface OnItemClickListener {
        fun onItemClick(address: Address)
    }
}
