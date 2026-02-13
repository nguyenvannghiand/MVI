package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.databinding.FragmentUserDetailBinding
import com.example.myapplication.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment: Fragment(R.layout.fragment_user_detail) {
    private val binding by viewBinding(FragmentUserDetailBinding::bind)

    companion object {
        private const val ARG_USER = "arg_user"

        fun newInstance(user: User) = UserDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
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