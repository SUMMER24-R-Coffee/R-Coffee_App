package datlowashere.project.rcoffee.ui.component

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import datlowashere.project.rcoffee.MainActivity
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

    @SuppressLint("MissingInflatedId")
    fun showLogoutDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_custom_dialog, null)
        val alertDialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnGoToLogin = dialogView.findViewById<Button>(R.id.btnGoToLogin)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val img = dialogView.findViewById<ImageView>(R.id.imgDialogcustom)
        val title =dialogView.findViewById<TextView>(R.id.tvTitleDialog)
        val message =dialogView.findViewById<TextView>(R.id.tvMessageDialog)
        btnGoToLogin.text="Log out"
        img.setImageResource(R.drawable.logomini)
        title.text="Comfirm"
        message.text="Are you sure to log out?"


        btnGoToLogin.setOnClickListener {
            alertDialog.dismiss()
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    fun showDeleteConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setMessage("Are you sure you want to delete this item?")
            setPositiveButton("Yes") { _, _ ->
                onDeleteConfirmed()
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}