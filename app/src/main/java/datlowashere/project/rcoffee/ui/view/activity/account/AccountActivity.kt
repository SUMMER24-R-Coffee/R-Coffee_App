package datlowashere.project.rcoffee.ui.view.activity.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.constant.AppConstant
import datlowashere.project.rcoffee.data.model.Users
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.data.repository.UserRepository
import datlowashere.project.rcoffee.databinding.ActivityAccountBinding
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModel
import datlowashere.project.rcoffee.ui.viewmodel.UserViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper
import datlowashere.project.rcoffee.ui.component.ProgressDialogCustom
import java.util.*

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var userViewModel: UserViewModel
    private var selectedImageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialogCustom

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialogCustom(this)

        val authRepository = AuthRepository()
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelFactory)[AuthViewModel::class.java]

        val userRepository = UserRepository()
        val userViewModelFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userViewModelFactory)[UserViewModel::class.java]

        setupViews()
        observeUserData()
        authViewModel.getUserData(getEmailUser())
    }

    private fun setupViews() {
        binding.btnBackAccount.setOnClickListener { finish() }
        binding.btnSaveAccount.setOnClickListener {
            updateUser()
        }
        binding.fabImg.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, AppConstant.REQUEST_IMAGE_PICK)
    }

    private fun updateUser() {
        val email = getEmailUser()
        val name = binding.tedNameAccount.text.toString().trim()
        val phone = binding.tedPhoneAccount.text.toString().trim()
        val gender = if (binding.rdoMale.isChecked) "Male" else "Female"

        selectedImageUri?.let { uri ->
            progressDialog.show()
            uploadImageToFirebase(uri) { imageUrl ->
                progressDialog.dismiss()
                val user = Users(email, "", gender, phone, imageUrl, name, "")
                userViewModel.updateUser(user)
            }
        } ?: run {
            val user = Users(email, "", gender, phone, null, name, "")
            userViewModel.updateUser(user)
        }
    }

    private fun observeUserData() {
        authViewModel.userResponse.observe(this, Observer { authResponse ->
            if (authResponse != null) {
                authResponse.users?.let { users ->
                    if (users.isNotEmpty()) {
                        val user = users[0]
                        fillUserData(user)
                    }
                }
            } else {
                Log.e("AccountActivity", "User response is null")
                showToast("Failed to fetch user data")
            }
        })

        userViewModel.updateUserResponse.observe(this, Observer { updateResponse ->
            if (updateResponse != null) {
                if (updateResponse.status == "success") {
                    showToast("User updated successfully")
                    finish()
                } else {
                    showToast("Failed to update user")
                }
            } else {
                showToast("Failed to update user")
            }
        })
    }

    private fun fillUserData(user: Users) {
        binding.apply {
            Glide.with(this@AccountActivity)
                .load(user.user_img ?: R.drawable.img_default)
                .placeholder(R.drawable.img_default)
                .centerCrop()
                .into(imgUserAccount)

            tedEmailAccount.setText(user.email_user ?: "")
            tedNameAccount.setText(user.name ?: "")
            tedPhoneAccount.setText(user.phone ?: "")

            if (user.gender.equals("Male", ignoreCase = true)) {
                rdoMale.isChecked = true
            } else if (user.gender.equals("Female", ignoreCase = true)) {
                rdoFemale.isChecked = true
            }
        }
    }

    private fun getEmailUser(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            imageUri?.let {
                selectedImageUri = it
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.img_default)
                    .centerCrop()
                    .into(binding.imgUserAccount)
            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${getEmailUser()}/${UUID.randomUUID()}")

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                onSuccess(imageUrl)
            }.addOnFailureListener { exception ->
                Log.e("AccountActivity", "Failed to get download URL", exception)
                showToast("Failed to upload image")
                progressDialog.dismiss()
            }
        }.addOnFailureListener { exception ->
            Log.e("AccountActivity", "Failed to upload image", exception)
            showToast("Failed to upload image")
            progressDialog.dismiss()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
    }
}
