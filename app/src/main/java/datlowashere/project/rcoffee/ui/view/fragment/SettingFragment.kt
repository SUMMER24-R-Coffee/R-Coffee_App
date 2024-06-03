package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentSettingsBinding
import datlowashere.project.rcoffee.ui.viewmodel.SettingViewModel


class SettingFragment : Fragment() {

    private var _binding :FragmentSettingsBinding? = null

    private val binding get() =_binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View =binding.root;
        return root;

    }

}