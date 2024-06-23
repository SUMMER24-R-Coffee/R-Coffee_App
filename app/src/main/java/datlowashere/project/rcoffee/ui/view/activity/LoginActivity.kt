package datlowashere.project.rcoffee.ui.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.data.model.response.TokenResponse
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.data.repository.UserRepository
import datlowashere.project.rcoffee.databinding.ActivityLoginBinding
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModel
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModelFactory
import datlowashere.project.rcoffee.utils.Resource
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var tokenFcm: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        getToken()

        binding.btnBackLogin.setOnClickListener {
            finish()
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.tedEmail.text.toString().trim()
            val password = binding.tedPassword.text.toString().trim()
            val temail = binding.tilEmail
            val tpassword = binding.tilPassword
            if (validateEmail(email, temail) && validatePassword(password, tpassword)) {
                authViewModel.login(email, password)
            }
        }
        setUpObserve()
    }

    private fun setUpViewModel() {
        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authFactory).get(AuthViewModel::class.java)

        val userRepository = UserRepository()
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)
    }

    private fun setUpObserve() {
        authViewModel.loginResult.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val response = resource.data
                    if (response != null && response.status == "success") {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        SharedPreferencesHelper.setLoggedIn(this, true)
                        SharedPreferencesHelper.setUserEmail(this, response.users?.email_user ?: "")
                        SharedPreferencesHelper.setUserPhone(this, response.users?.phone ?: " ")
                        SharedPreferencesHelper.setUserName(this, response.users?.name ?: " ")
                        SharedPreferencesHelper.setUserToken(this, response.users?.token ?: " ")

                        val email = response.users?.email_user ?: ""
                        userViewModel.updateTokenUser(email, TokenResponse(tokenFcm))

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            response?.message.toString() ?: "Login failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        resource.message.toString() ?: "Login failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Resource.Loading -> {
                    // Show loading indicator if needed
                }
            }
        }

        userViewModel.updateTokenResult.observe(this) { result ->
            result.onSuccess {
                Log.i("LoginActivity", "FCM token updated successfully")
            }.onFailure { exception ->
                Log.e("LoginActivity", "Failed to update FCM token", exception)
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Failed to get token", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            tokenFcm = token
            Log.i("Token", token)
        })
    }

    private fun validateEmail(email: String, textInputLayout: TextInputLayout): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return if (Pattern.matches(emailPattern, email)) {
            textInputLayout.isErrorEnabled = false
            true
        } else {
            textInputLayout.error = "Invalid email address"
            false
        }
    }

    private fun validatePassword(password: String, textInputLayout: TextInputLayout): Boolean {
        return if (password.isNotEmpty()) {
            textInputLayout.isErrorEnabled = false
            true
        } else {
            textInputLayout.error = "Password cannot be empty"
            false
        }
    }
}
