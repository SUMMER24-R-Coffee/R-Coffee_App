package datlowashere.project.rcoffee.ui.component

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.model.PriceRange
import datlowashere.project.rcoffee.databinding.LayoutBottomSheetFilterBinding

class FilterBottomSheetFragment : BottomSheetDialogFragment() {

    interface FilterListener {
        fun onFilterSelected(priceRange: PriceRange)
    }

    private var listener: FilterListener? = null
    private var _binding: LayoutBottomSheetFilterBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FilterListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement FilterListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvOk.setOnClickListener {
            val selectedPriceRange = when (binding.radioGroupPrice.checkedRadioButtonId) {
                R.id.radio_less_than_20000 -> PriceRange.LESS_THAN_20000
                R.id.radio_20000_to_40000 -> PriceRange.BETWEEN_20000_AND_40000
                R.id.radio_greater_than_40000 -> PriceRange.GREATER_THAN_40000
                else -> PriceRange.ALL
            }
            listener?.onFilterSelected(selectedPriceRange)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


