package com.example.a35b_crud.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.a35b_crud.R
import com.example.a35b_crud.databinding.FragmentProfileBinding
import com.example.a35b_crud.repository.UserRepositoryImpl
import com.example.a35b_crud.ui.activity.EditProfileActivity
import com.example.a35b_crud.ui.activity.LoginActivity
import com.example.a35b_crud.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl(FirebaseAuth.getInstance())
        userViewModel = UserViewModel(repo)

        // Load the current user data from Firebase
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid)
        }

        // Observe user data and update UI
        userViewModel.userData.observe(requireActivity()) { user ->
            binding.profileEmail.text = user?.email
            binding.profileName.text = "${user?.firstName} ${user?.lastName}"
        }

        // Set click listener for editing profile
        binding.editProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))

        }

        // Set click listener for logout functionality
        binding.logout.setOnClickListener {
            userViewModel.logout { success, message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                if (success) {
                    // Redirect to LoginActivity (or your login screen) and finish current activity
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}
