package datlowashere.project.rcoffee.ui.component

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity

object DialogCustom {

    fun showLoginDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_custom_dialog, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnGoToLogin = dialogView.findViewById<Button>(R.id.btnGoToLogin)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        btnGoToLogin.setOnClickListener {
            alertDialog.dismiss()
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}