package datlowashere.project.rcoffee.ui.view.activity.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.ActivityRegisterBinding
import datlowashere.project.rcoffee.ui.component.ProgressDialogCustom
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progressDialogHelper: ProgressDialogCustom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        progressDialogHelper = ProgressDialogCustom(this)

        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegis.setOnClickListener {
            performRegistration()
        }

        authViewModel.requestCodeResponse.observe(this, Observer { response ->
            progressDialogHelper.dismiss()
            if (response.isSuccessful) {
                Toast.makeText(this, "Verification code sent to your email", Toast.LENGTH_SHORT)
                    .show()
                Log.d("RegisterActivity", "Verification code sent successfully")

                // Proceed to VerificationActivity
                val intent = Intent(this, VerificationCodeActivity::class.java)
                intent.putExtra("email", binding.tedEmailRegis.text.toString())
                intent.putExtra("password", binding.tedPasswordRegis.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Failed to send verification code: ${response.message()}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("RegisterActivity", "Failed to send verification code: ${response.message()}")
            }
        })

        authViewModel.requestCodeError.observe(this, Observer { error ->
            progressDialogHelper.dismiss()
            Toast.makeText(this, "An error occurred: $error", Toast.LENGTH_SHORT).show()
            Log.e("RegisterActivity", "Error occurred: $error")
        })
    }

    private fun performRegistration() {
        val email = binding.tedEmailRegis.text.toString().trim()
        val password = binding.tedPasswordRegis.text.toString().trim()
        val confirmPassword = binding.ReTedPasswordRegis.text.toString().trim()

        if (!validateFields(email, password, confirmPassword)) {
            return
        }

        progressDialogHelper.show()
        authViewModel.requestCode(email)
    }

    private fun validateFields(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
