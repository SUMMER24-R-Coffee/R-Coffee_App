package datlowashere.project.rcoffee.ui.component

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import datlowashere.project.rcoffee.databinding.LayoutProgressDialogBinding

class ProgressDialogCustom(context: Context) {

    private val progressDialog: Dialog

    init {
        val binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context))
        progressDialog = Dialog(context).apply {
            setContentView(binding.root)
            setCancelable(false)
        }
    }

    fun show() {
        progressDialog.show()
    }

    fun dismiss() {
        progressDialog.dismiss()
    }
}
