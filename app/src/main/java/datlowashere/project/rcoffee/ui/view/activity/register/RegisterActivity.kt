package datlowashere.project.rcoffee.ui.view.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.ActivityRegisterBinding
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.tvBackToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegis.setOnClickListener {
            val email = binding.tedEmailRegis.text.toString()
            if (email.isNotEmpty()) {
                authViewModel.requestCode(email)
            } else {
                Toast.makeText(this, "Please enter an email address", Toast.LENGTH_SHORT).show()
            }
        }
        authViewModel.requestCodeResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Toast.makeText(this, "Verification code sent to your email", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Verification code sent successfully")
            } else {
                Toast.makeText(this, "Failed to send verification code: ${response.message()}", Toast.LENGTH_SHORT).show()
                Log.e("RegisterActivity", "Failed to send verification code: ${response.message()}")
            }
        })

        authViewModel.requestCodeError.observe(this, Observer { error ->
            Toast.makeText(this, "An error occurred: $error", Toast.LENGTH_SHORT).show()
            Log.e("RegisterActivity", "Error occurred: $error")
        })
    }


}