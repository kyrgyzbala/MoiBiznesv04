package com.kyrgyzcoder.moibiznesv04

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.kyrgyzcoder.moibiznesv04.categories.*
import com.kyrgyzcoder.moibiznesv04.sign_in.SignInActivity
import com.kyrgyzcoder.moibiznesv04.utils.CHOOSE_IMAGE_REQUEST
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var profilePhotoImageView: CircleImageView
    private lateinit var textUserName: TextView
    private lateinit var profilePhotoDownloadUrl: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        mAuth = FirebaseAuth.getInstance()
        mProgressBar = findViewById(R.id.progressBarMain)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, BothVseFragment()).commit()

        drawer = findViewById(R.id.drawer_layout_id)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)
        textUserName = headerView.findViewById<TextView>(R.id.textViewUserName)
        profilePhotoImageView = headerView.findViewById(R.id.profilePicture)
        loadUserData()
        profilePhotoImageView.setOnClickListener {
            chooseProfilePhoto()
        }
        val toggle =
            ActionBarDrawerToggle(this, drawer, toolbar_main, R.string.open_nav, R.string.close_nav)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser == null) {
            mAuth.signOut()
            finish()
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_vse_tovary -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, BothVseFragment()).commit()
                title = "Мой бизнес"
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_statistics -> {

            }
            R.id.nav_vesna -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, VesnaFragment()).commit()
                title = resources.getString(R.string.vesna)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_leto -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LetoFragment()).commit()
                title = resources.getString(R.string.leto)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_osen -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, OsenFragment()).commit()
                title = resources.getString(R.string.osen)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_shkolnyi -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ShkolnyiFragment()).commit()
                title = resources.getString(R.string.shkolnyi)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_zima -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ZimaFragment()).commit()
                title = resources.getString(R.string.zima)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_drugie -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DrugieFragment()).commit()
                title = resources.getString(R.string.drugie)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.action_sign_out -> {
                signOut()
            }
        }
        return true
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
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriProfileImage)
                profilePhotoImageView.setImageBitmap(bitmap)
                uploadImageToCloud(uriProfileImage)
            }
        }
    }

    private fun loadUserData() {
        val user = mAuth.currentUser
        if (user != null) {
            val userEmail = user.email.toString()
            textUserName.text = userEmail

            if (user.photoUrl != null) {
                Log.d("PIC", " ->>> ${user.photoUrl.toString()}")
                Glide.with(this).load(user.photoUrl.toString()).into(profilePhotoImageView)
            }
        }
    }


    /**
     * Function to upload profile image to Firebase storage and gets download url
     */
    private fun uploadImageToCloud(uriProfileImage: Uri) {
        mProgressBar.visibility = View.VISIBLE
        val profileImageRef = FirebaseStorage.getInstance()
            .getReference("profilepics/" + System.currentTimeMillis() + ".jpg")
        profileImageRef.putFile(uriProfileImage)
            .addOnCompleteListener { itTask ->
                if (itTask.isSuccessful) {
                    profileImageRef.downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("PIC", "UriProfile imegae: ${uriProfileImage.toString()}")
                            profilePhotoDownloadUrl = task.result.toString()
                            Log.d("PIC", "DownloadLink -> $profilePhotoDownloadUrl")
                            val mUser = mAuth.currentUser
                            if (mUser != null && profilePhotoDownloadUrl.isNotEmpty()) {
                                val profile = UserProfileChangeRequest.Builder()
                                    .setPhotoUri(Uri.parse(profilePhotoDownloadUrl))
                                    .build()
                                mUser.updateProfile(profile).addOnCompleteListener {
                                    mProgressBar.visibility = View.GONE
                                    if (it.isSuccessful) {
                                        Toast.makeText(
                                            this,
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

    /**
     * Function to sign out
     */
    private fun signOut() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogStyle))
        builder.setTitle("Внимание!").setMessage(getString(R.string.signout_message))
            .setPositiveButton("Выйти") { _, _ ->
                mAuth.signOut()
                finish()
                val intent = Intent(this, SignInActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        builder.setNegativeButton("Отменить") { _, _ ->
            Toast.makeText(this, "Отменено!", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    /**
     * Function to change profile picture
     */
    private fun chooseProfilePhoto() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogStyle))
        builder.setTitle("Внимание!").setMessage(getString(R.string.setprofilemessage))
            .setNegativeButton("Отменить") { _, _ ->
                Toast.makeText(this, "Отменено!", Toast.LENGTH_SHORT).show()
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
}
