package datlowashere.project.rcoffee.ui.view.activity.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.ActivityVerificationCodeBinding

class VerificationCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}