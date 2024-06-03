package datlowashere.project.rcoffee.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.FragmentFavoriteBinding
import datlowashere.project.rcoffee.ui.viewmodel.FavoriteViewModel


class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null

    private val binding get() = _binding !!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel  ::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater,container, false)

        val root : View = binding.root

        return root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}