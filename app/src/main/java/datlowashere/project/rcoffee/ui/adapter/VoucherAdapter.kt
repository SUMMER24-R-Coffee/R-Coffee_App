package datlowashere.project.rcoffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import datlowashere.project.rcoffee.data.model.Voucher
import datlowashere.project.rcoffee.databinding.LayoutItemVoucherBinding
import datlowashere.project.rcoffee.utils.FormatterHelper
import java.util.Date

class VoucherAdapter(
    private val vouchers: List<Voucher>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val binding =
            LayoutItemVoucherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        val currentVoucher = vouchers[position]
        holder.bind(currentVoucher)
    }

    override fun getItemCount() = vouchers.size

    inner class VoucherViewHolder(private val binding: LayoutItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardViewItemVoucher.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(vouchers[position])
                }
            }
        }

        fun bind(voucher: Voucher) {
            binding.tvVoucherCode.text = voucher.voucher_code
            binding.tvVoucherPercent.text = "${voucher.percent}%"
            binding.tvVoucherValidDate.text =
                "Valid " + FormatterHelper.formatDateTime(voucher.valid_date)

            val remainingTime = calculateRemainingTime(voucher.exp_date)
            binding.tvVoucherExpDate.text = remainingTime
        }
    }

    private fun calculateRemainingTime(expirationDate: Date): String {
        val currentTime = System.currentTimeMillis()
        val timeDiff = expirationDate.time - currentTime

        val days = timeDiff / (1000 * 60 * 60 * 24)
        val hours = (timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
        val minutes = (timeDiff % (1000 * 60 * 60)) / (1000 * 60)

        return "Expires in $days days, $hours hours, $minutes minutes"
    }

    interface OnItemClickListener {
        fun onItemClick(voucher: Voucher)
    }
}
