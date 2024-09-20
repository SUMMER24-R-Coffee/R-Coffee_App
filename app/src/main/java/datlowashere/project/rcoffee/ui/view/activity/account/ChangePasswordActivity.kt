package datlowashere.project.rcoffee.ui.view.activity.account

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.data.repository.UserRepository
import datlowashere.project.rcoffee.databinding.ActivityChangePasswordBinding
import datlowashere.project.rcoffee.ui.component.ProgressDialogCustom
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModel
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var progressDialog: ProgressDialogCustom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackChangePass.setOnClickListener { finish() }

        progressDialog = ProgressDialogCustom(this)

        val userRepository = UserRepository()
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        binding.btnSaveChangePass.setOnClickListener {
            validateAndChangePassword()
        }
    }


    private fun validateAndChangePassword() {
        val oldPassword = binding.tedOldPass.text.toString().trim()
        val newPassword = binding.tedNewPass.text.toString().trim()
        val retypePassword = binding.tedReType.text.toString().trim()

        if (oldPassword.isEmpty() || newPassword.isEmpty() || retypePassword.isEmpty()) {
            showToast("Please fill in all fields")
            return
        }
        if (oldPassword != getOldPass()) {
            showToast("Old password is incorrect")
            return
        }

        if (newPassword.length < 6) {
            showToast("New password must be at least 6 characters")
            return
        }

        if (newPassword != retypePassword) {
            showToast("Passwords do not match")
            return
        }

        progressDialog.show()
        userViewModel.updatePassword(getEmailUser(), newPassword)
        observePasswordUpdate()
    }

    private fun getEmailUser(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }

    private fun getOldPass(): String {
        return SharedPreferencesHelper.getUserPassword(this) ?: ""
    }

    private fun observePasswordUpdate() {
        userViewModel.updatePasswordResponse.observe(this, Observer { response ->
            progressDialog.dismiss()
            if (response != null) {
                if (response.status == "success") {
                    showToast("Password updated successfully")
                    val newPassword = binding.tedNewPass.text.toString().trim()
                    SharedPreferencesHelper.setUserPassword(this, newPassword)
                    finish()
                } else {
                    showToast("Failed to update password")
                }
            } else {
                showToast("Failed to update password")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
