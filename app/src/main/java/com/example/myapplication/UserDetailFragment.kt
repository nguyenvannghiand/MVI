package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentUserDetailBinding
import com.example.myapplication.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment: Fragment(R.layout.fragment_user_detail) {
    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!! // Getter an toàn

    companion object {
        private const val ARG_USER = "arg_user"

        fun newInstance(user: User) = UserDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getParcelable<User>(ARG_USER)
        user?.let {
            // Show info
            binding.txtUserName.text = it.name
        }
    }
}