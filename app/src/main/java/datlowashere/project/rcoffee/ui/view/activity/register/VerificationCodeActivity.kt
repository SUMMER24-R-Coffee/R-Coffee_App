package datlowashere.project.rcoffee.ui.view.activity.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.ActivityVerificationCodeBinding
import datlowashere.project.rcoffee.ui.component.ProgressDialogCustom
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class VerificationCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationCodeBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var progressDialogHelper: ProgressDialogCustom
    private var isCountingDown = false
    private var countDownTimer: CountDownTimer? = null
    private val COUNTDOWN_TIME_MILLIS = 120000

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository()
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        progressDialogHelper = ProgressDialogCustom(this)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        binding.btnBackVertification.setOnClickListener { finish() }
        binding.tvTitleVertification.text =
            "We have already sent verification code to your email: $email"

        binding.tvResend.setOnClickListener {
            if (!isCountingDown) {
                startCountdown()
                progressDialogHelper.show()
                authViewModel.requestCode(email ?: "")
            }
        }

        binding.btnRequestVertification.setOnClickListener {
            val code = binding.otpView.otp
            if (code?.let { it1 -> validateInputs(it1) } == true) {
                progressDialogHelper.show()
                authViewModel.registerUser(email ?: "", password ?: "", code)

            }
        }
        startCountdown()
        observeViewModel()
    }

    private fun validateInputs(code: String): Boolean {
        if (code.isBlank()) {
            Toast.makeText(this, "Please enter verification code", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun startCountdown() {
        binding.tvResend.isClickable = false
        isCountingDown = true
        countDownTimer = object : CountDownTimer(COUNTDOWN_TIME_MILLIS.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.tvResend.text = "Resend in ${secondsLeft}s"
            }

            override fun onFinish() {
                binding.tvResend.isClickable = true
                binding.tvResend.text = "Resend"
                isCountingDown = false
            }
        }.start()
    }

    private fun observeViewModel() {
        authViewModel.registerResponse.observe(this) { response ->
            progressDialogHelper.dismiss()
            if (response.isSuccessful) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody.isNullOrEmpty()) {
                    "Registration failed: Unknown error"
                } else {
                    try {
                        val jsonObject = JSONObject(errorBody)
                        jsonObject.getString("message")
                    } catch (e: JSONException) {
                        "Registration failed: Unknown error"
                    }
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        authViewModel.requestCodeResponse.observe(this) { response ->
            progressDialogHelper.dismiss()
            if (response.isSuccessful) {
                Toast.makeText(this, "Verification code sent to your email", Toast.LENGTH_SHORT)
                    .show()
                startCountdown()
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody.isNullOrEmpty()) {
                    "Failed to send verification code: Unknown error"
                } else {
                    try {
                        val jsonObject = JSONObject(errorBody)
                        jsonObject.getString("message")
                    } catch (e: JSONException) {
                        "Failed to send verification code: Unknown error"
                    }
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.requestCodeError.observe(this) { error ->
            progressDialogHelper.dismiss()
            Toast.makeText(this, "An error occurred: $error", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
