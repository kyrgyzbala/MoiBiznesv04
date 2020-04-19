package com.kyrgyzcoder.moibiznesv04.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.sign_in.SignInActivity
import com.kyrgyzcoder.moibiznesv04.utils.CHOOSE_IMAGE_REQUEST
import de.hdodenhof.circleimageview.CircleImageView

/**
 * A simple [Fragment] subclass Handles Settings of user info
 */
class SettingsFragment : Fragment() {

    private lateinit var profileImg: CircleImageView
    private lateinit var emailTextView: TextView
    private lateinit var displayNameTextView: TextView
    private lateinit var changePwdTextView: TextView
    private lateinit var deleteAccountTextView: TextView
    private lateinit var profilePhotoDownloadUrl: String
    private lateinit var mProgressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        profileImg = view.findViewById(R.id.profileImgSettings)
        emailTextView = view.findViewById(R.id.emailTextViewSettings)
        displayNameTextView = view.findViewById(R.id.displayNameTextView)
        changePwdTextView = view.findViewById(R.id.changePwdTextViewSettings)
        deleteAccountTextView = view.findViewById(R.id.deleteAccountTextViewSettings)
        mProgressBar = view.findViewById(R.id.progressBarSettings)
        activity!!.title = "Профиль"

        mAuth = FirebaseAuth.getInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        profileImg.setOnClickListener {
            chooseProfilePhoto()
        }

        changePwdTextView.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_reAuthFragment)
        }

        deleteAccountTextView.setOnClickListener {
            //deleteAccount()
        }
    }

    /**
     * Function to load initial/current data
     */
    private fun loadData() {
        val user = mAuth.currentUser
        if (user != null) {
            emailTextView.text = user.email
            if (user.photoUrl != null) {
                Log.d("PIC", " ->>> ${user.photoUrl.toString()}")
                Glide.with(this).load(user.photoUrl).into(profileImg)
            }
            displayNameTextView.text = user.displayName
        }
    }


    /**
     * Function to change profile picture
     */
    private fun chooseProfilePhoto() {
        val builder = AlertDialog.Builder(
            ContextThemeWrapper(
                this.requireContext(),
                R.style.AlertDialogStyle
            )
        )
        builder.setTitle("Внимание!").setMessage(getString(R.string.setprofilemessage))
            .setNegativeButton("Отменить") { _, _ ->
                Toast.makeText(this.requireContext(), "Отменено!", Toast.LENGTH_SHORT).show()
            }
        builder.setPositiveButton("Изменить") { _, _ ->
            showImageChooser()
        }
        builder.create().show()
    }

    /**
     * function to select image from gallery or camera
     */
    private fun showImageChooser() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выберите фото профила!"),
            CHOOSE_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CHOOSE_IMAGE_REQUEST &&
            resultCode == Activity.RESULT_OK &&
            data != null &&
            data.data != null
        ) {
            val uriProfileImage = data.data
            if (uriProfileImage != null) {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uriProfileImage)
                profileImg.setImageBitmap(bitmap)
                uploadImageToCloud(uriProfileImage)
            }
        }
    }

    /**
     * Function to upload profile image to Firebase storage and gets download url
     */
    private fun uploadImageToCloud(uriProfileImage: Uri) {
        mProgressBar.visibility = View.VISIBLE
        val user = mAuth.currentUser
        if (user != null) {
            val profileImageRef = FirebaseStorage.getInstance()
                .getReference("profilepics/" + user.uid + ".jpg")
            profileImageRef.putFile(uriProfileImage)
                .addOnCompleteListener { itTask ->
                    if (itTask.isSuccessful) {
                        profileImageRef.downloadUrl.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("PIC", "UriProfile imegae: ${uriProfileImage.toString()}")
                                profilePhotoDownloadUrl = task.result.toString()
                                Log.d("PIC", "DownloadLink -> $profilePhotoDownloadUrl")
                                if (profilePhotoDownloadUrl.isNotEmpty()) {
                                    val profile = UserProfileChangeRequest.Builder()
                                        .setPhotoUri(Uri.parse(profilePhotoDownloadUrl))
                                        .build()
                                    user.updateProfile(profile).addOnCompleteListener {
                                        mProgressBar.visibility = View.GONE
                                        if (it.isSuccessful) {
                                            Toast.makeText(
                                                this.requireContext(),
                                                "Фото профиля изменено!",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    /**
     * Delete User with confirmation
     */

    private fun deleteAccount() {
        val builder = AlertDialog.Builder(
            ContextThemeWrapper(
                this.requireContext(),
                R.style.AlertDialogStyle
            )
        )
        builder.setTitle("Внимание!").setMessage(getString(R.string.deleteUserMessage))
            .setNegativeButton("Отменить") { _, _ ->
                Toast.makeText(this.requireContext(), "Отменено!", Toast.LENGTH_SHORT).show()
            }
        builder.setPositiveButton("Удалить") { _, _ ->
            deleteIfConfirmed()
        }
        builder.create().show()
    }

    private fun deleteIfConfirmed() {
        val user = mAuth.currentUser

        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this.requireContext(),
                    "Аккунт успешно удален!",
                    Toast.LENGTH_LONG
                ).show()
                mAuth.signOut()
                activity!!.finish()
                val intent = Intent(this.requireContext(), SignInActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Error: -> ${task.exception!!.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}
