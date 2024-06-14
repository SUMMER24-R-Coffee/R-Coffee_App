package datlowashere.project.rcoffee.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.ActivityLoginBinding
import datlowashere.project.rcoffee.network.ApiClient
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.utils.Resource

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        binding.btnBackLogin.setOnClickListener {
            finish()
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tedEmail.text.toString().trim()
            val password = binding.tedPassword.text.toString().trim()
            authViewModel.login(email, password)
        }

        authViewModel.loginResult.observe(this, { resource ->
            when (resource) {
                is Resource.Success -> {
                    val response = resource.data
                    if (response != null && response.status == "success") {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        SharedPreferencesHelper.setLoggedIn(this, true)
                        SharedPreferencesHelper.setUserEmail(this, response.users?.email_user ?: "")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, response?.message.toString() ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Error -> {
                    Toast.makeText(this, resource.message.toString() ?: "Login failed", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                }
            }
        })
    }
}
