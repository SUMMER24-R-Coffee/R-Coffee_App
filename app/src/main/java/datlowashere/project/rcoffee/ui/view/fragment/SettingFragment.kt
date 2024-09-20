package datlowashere.project.rcoffee.ui.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.FragmentSettingsBinding
import datlowashere.project.rcoffee.ui.component.DialogCustom
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity
import datlowashere.project.rcoffee.ui.view.activity.account.AccountActivity
import datlowashere.project.rcoffee.ui.view.activity.account.ChangePasswordActivity
import datlowashere.project.rcoffee.ui.view.activity.address.SelectAddressActivity
import datlowashere.project.rcoffee.ui.view.activity.basket.BasketActivity
import datlowashere.project.rcoffee.ui.view.activity.notification.NotificationActivity
import datlowashere.project.rcoffee.ui.view.activity.other.AboutActivity
import datlowashere.project.rcoffee.ui.view.activity.other.HelpCenterActivity
import datlowashere.project.rcoffee.ui.view.activity.register.RegisterActivity
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authRepository = AuthRepository()
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        authViewModel.getUserData(getEmailUser())
        checkUser()
        setUpObserves()
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObserves() {
        authViewModel.userResponse.observe(viewLifecycleOwner, Observer { authResponse ->
            if (authResponse != null) {
                authResponse.users?.let { users ->
                    if (users.isNotEmpty()) {
                        val user = users[0]
                        Glide.with(binding.imgUserSetting)
                            .load(user.user_img)
                            .centerCrop()
                            .placeholder(R.drawable.img_default)
                            .into(binding.imgUserSetting)

                        binding.tvNameUserSetting.text = "${user.name ?: "Account"}!"
                    }
                }
            } else {
                Log.e("SettingFragment", "User response is null")
            }
        })
    }

    private fun checkUser() {
        binding.apply {
            val isUserLoggedIn = getEmailUser().isNotEmpty()

            if (isUserLoggedIn) {
                btnLogout.visibility = View.VISIBLE
                lnOption.visibility = View.GONE
            } else {
                btnLogout.visibility = View.GONE
                lnOption.visibility = View.VISIBLE
            }

            imgUserSetting.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), AccountActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }

            lnAccount.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), AccountActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }

            lnBasket.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), BasketActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }

            lnChangePassword.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }

            lnNotification.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), NotificationActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }

            lnAddress.setOnClickListener {
                if (isUserLoggedIn) {
                    startActivity(Intent(requireContext(), SelectAddressActivity::class.java))
                } else {
                    DialogCustom.showLoginDialog(requireContext())
                }
            }
            lnAbout.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        AboutActivity::class.java
                    )
                )
            }
            lnHelpCentre.setOnClickListener {
                startActivity(
                    Intent(
                        requireContext(),
                        HelpCenterActivity::class.java
                    )
                )
            }
            btnGetLogin.setOnClickListener {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            btnGetRegister.setOnClickListener {
                startActivity(Intent(requireContext(), RegisterActivity::class.java))
            }
            btnLogout.setOnClickListener {
                DialogCustom.showLogoutDialog(requireContext())
                val nonNullContext = requireNotNull(context)
                SharedPreferencesHelper.clear(nonNullContext)
            }
        }
    }

    private fun getEmailUser(): String {
        return SharedPreferencesHelper.getUserEmail(requireContext()) ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
