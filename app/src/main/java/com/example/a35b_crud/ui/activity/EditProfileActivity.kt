package com.example.a35b_crud.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a35b_crud.databinding.ActivityEditProfileBinding
import com.example.a35b_crud.repository.UserRepositoryImpl
import com.example.a35b_crud.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repo = UserRepositoryImpl(FirebaseAuth.getInstance())
        userViewModel = UserViewModel(repo)

        // Load current user data
        val currentUser = userViewModel.getCurrentUser()
        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid)
        }

        // Observe and prefill user details
        userViewModel.userData.observe(this) { user ->
            user?.let {
                binding.editFirstName.setText(it.firstName)
                binding.editLastName.setText(it.lastName)
                binding.editEmail.setText(it.email)
            }
        }

        // Save changes when button is clicked
        binding.btnSaveProfile.setOnClickListener {
            val firstName = binding.editFirstName.text.toString().trim()
            val lastName = binding.editLastName.text.toString().trim()
            val email = binding.editEmail.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a map of updated fields
            val updateMap = mutableMapOf<String, Any>()
            updateMap["firstName"] = firstName
            updateMap["lastName"] = lastName
            updateMap["email"] = email

            currentUser?.let { user ->
                userViewModel.editProfile(user.uid, updateMap) { success, message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        finish()  // Close activity and return to ProfileFragment
                    }
                }
            }
        }
    }
}
