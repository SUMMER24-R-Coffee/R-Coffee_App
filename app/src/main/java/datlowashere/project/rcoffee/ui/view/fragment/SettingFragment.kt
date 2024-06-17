package datlowashere.project.rcoffee.ui.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.MainActivity
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentSettingsBinding
import datlowashere.project.rcoffee.ui.view.activity.LoginActivity
import datlowashere.project.rcoffee.ui.viewmodel.SettingViewModel
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper


class SettingFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLogout()
        setUpGetLogin()
        checkUser()

    }

    fun checkUser(){
        if(getEmailUser().isNotEmpty()){
            binding.btnLogout.visibility=View.VISIBLE
            binding.btnGetLogin.visibility=View.GONE

        }else{
            binding.btnLogout.visibility=View.GONE
            binding.btnGetLogin.visibility=View.VISIBLE

        }
    }

    fun setUpGetLogin(){
        binding.btnGetLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))

        }
    }
    fun setUpLogout(){
        binding.btnLogout.setOnClickListener {
            val nonNullContext = requireNotNull(context)
            SharedPreferencesHelper.clear(nonNullContext)

            startActivity(Intent(requireContext(), MainActivity::class.java))
        }
    }

    fun getEmailUser(): String {
        return SharedPreferencesHelper.getUserEmail(requireContext()) ?: ""
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
