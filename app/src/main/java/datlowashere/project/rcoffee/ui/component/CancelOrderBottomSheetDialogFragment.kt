package datlowashere.project.rcoffee.ui.component



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import datlowashere.project.rcoffee.databinding.LayoutCancelReasonBinding

class CancelOrderBottomSheetDialogFragment : BottomSheetDialogFragment() {

    interface OnReasonSelectedListener {
        fun onReasonSelected(reason: String)
    }

    private var listener: OnReasonSelectedListener? = null
    private var _binding: LayoutCancelReasonBinding? = null
    private val binding get() = _binding!!

    fun setOnReasonSelectedListener(listener: OnReasonSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutCancelReasonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvOk.setOnClickListener {
            val selectedRadioButtonId = binding.radioGroupReasons.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedRadioButtonId)
                listener?.onReasonSelected(selectedRadioButton.text.toString())
                dismiss()
            } else {
                Toast.makeText(context, "Please select a reason", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvDismiss.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}