package com.kyrgyzcoder.moibiznesv04.sign_in

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.kyrgyzcoder.moibiznesv04.MainActivity
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.utils.GOOGLE_SIGN_IN_REC
import de.hdodenhof.circleimageview.CircleImageView

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var passwordTwoEditText: EditText
    private lateinit var registerCardView: CardView
    private lateinit var haveAccountTextView: TextView
    private lateinit var signInWithGoogleImageView: CircleImageView
    private lateinit var signInWithFacebookButton: LoginButton
    private lateinit var progressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mCallbackManager: CallbackManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailEditText = view.findViewById(R.id.editTextEmailRegister)
        passwordEditText = view.findViewById(R.id.editTextPasswordRegisterFirst)
        passwordTwoEditText = view.findViewById(R.id.editTextPasswordRegisterSecond)
        registerCardView = view.findViewById(R.id.cardViewRegister)
        haveAccountTextView = view.findViewById(R.id.textViewAlreadyHave)
        signInWithGoogleImageView = view.findViewById(R.id.signInWithGoogle)
        signInWithFacebookButton = view.findViewById(R.id.login_button_face)
        signInWithFacebookButton.setReadPermissions("email", "public_profile")
        progressBar = view.findViewById(R.id.progressBarRegister)

        firebaseAuth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(activity!!.applicationContext)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity as Activity, gso)
        haveAccountTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_signInFragment)
        }

        showSignInOptions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN_REC && resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignIn(task)
        }
    }

    private fun showSignInOptions() {

        mCallbackManager = CallbackManager.Factory.create()
        signInWithFacebookButton.setOnClickListener {
            signInWithFacebook()
        }


        registerCardView.setOnClickListener {
            registerNewUser()
        }

        signInWithGoogleImageView.setOnClickListener {
            signInWithGoogle()
        }


    }

    private fun signInWithFacebook() {
        LoginManager.getInstance().registerCallback(mCallbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                progressBar.visibility = View.VISIBLE
                Log.d("NUR", "onSuccess -> $result")
                if (result != null) {
                    handleFacebookAccessToken(result.accessToken)
                }
            }
            override fun onCancel() {
                Log.d("NUR", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("NUR", "onError -> ${error!!.message}")
            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        progressBar.visibility = View.VISIBLE
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                Log.d("NUR", "handleAccessToken() --> ")
                //Start Activity
                activity!!.finish()
                val intent = Intent(this.requireContext(), MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            } else {
                Log.d("NUR", "Failed to sign in with facebook!")
            }
        }
    }

    private fun handleGoogleSignIn(task: Task<GoogleSignInAccount>?) {
        try {
            progressBar.visibility = View.VISIBLE
            val acc = task!!.getResult(ApiException::class.java)
            Toast.makeText(this.requireContext(), "Успешно зарегистрировано!", Toast.LENGTH_LONG)
                .show()
            firebaseGoogleAuth(acc)
        } catch (e: ApiException) {
            Toast.makeText(this.requireContext(), "Регистрация провалено", Toast.LENGTH_LONG).show()
            firebaseGoogleAuth(null)
        }
    }

    private fun firebaseGoogleAuth(acc: GoogleSignInAccount?) {
        if (acc != null) {
            val authCredential = GoogleAuthProvider.getCredential(acc.idToken, null)
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {
                if (it.isSuccessful) {
                    //Update user info by getting info from google account
                    val account = GoogleSignIn.getLastSignedInAccount(activity!!.applicationContext)
                    val user = firebaseAuth.currentUser
                    if (account != null && user != null) {
                        val profileChangeRequest = UserProfileChangeRequest.Builder()
                            .setPhotoUri(account.photoUrl)
                            .build()
                        user.updateProfile(profileChangeRequest).addOnCompleteListener { task ->
                            progressBar.visibility = View.GONE
                            if (task.isSuccessful) {
                                Log.d("NUR", "User updated Successfully")
                            } else {
                                Log.d("NUR", "User not updated!")
                            }
                        }
                    }
                    activity!!.finish()
                    val intent = Intent(this.requireContext(), MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    }
                    startActivity(intent)
                }
            }
        }

    }


    /**
     * Function that checks out Email and Passwords and registers it
     */
    private fun registerNewUser() {
        passwordTwoEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
        val email = emailEditText.text.toString().trim()
        val pOne = passwordEditText.text.toString().trim()
        val pTwo = passwordTwoEditText.text.toString().trim()
        if (validateLoginInput(email, pOne, pTwo)) {
            progressBar.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(email, pOne).addOnCompleteListener {
                progressBar.visibility = View.GONE
                if (it.isSuccessful) {
                    Toast.makeText(
                        this.requireContext(),
                        "Ваша почта успешно зарегистрирована!",
                        Toast.LENGTH_LONG
                    ).show()
                    firebaseAuth.currentUser!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this.requireContext(),
                                    "Пожалуйста проверьте свою почту Email и подтвердите свою почту по ссылке!",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.action_registerFragment_to_signInFragment)
                            } else {
                                Toast.makeText(
                                    this.requireContext(),
                                    "Пожалуйста проверьте свою почту Email и нажмите на ссылку там!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "Ваш email уже зарегистрирован. Попробуйте с другой почты или войдите!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Sign in with google
     */
    private fun signInWithGoogle() {
        val intent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent, GOOGLE_SIGN_IN_REC)
    }

    /**
     * Util functions
     */
    private fun validateLoginInput(email: String, pOne: String, pTwo: String): Boolean {

        var ret = true

        if (email.isEmpty() || !(email.contains("@"))) {
            emailEditText.error = getString(R.string.emailMustContain)
            ret = false
        }
        if (pOne.isEmpty()) {
            passwordEditText.error = "Придумайте пароль!"
            ret = false
        }

        if (pTwo.isEmpty()) {
            passwordTwoEditText.error = "Повторите пароль еще раз!"
            ret = false
        }

        if (pOne != pTwo) {
            passwordTwoEditText.error = getString(R.string.passwordsMustMatch)
            ret = false
        }

        if (pOne.length < 6) {
            passwordEditText.error = getString(R.string.passwordLength)
            ret = false
        }
        return ret
    }

}
