package com.kyrgyzcoder.moibiznesv04.sign_in

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kyrgyzcoder.moibiznesv04.MainActivity
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_USERNAME
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
    private lateinit var signInWithFacebookImageView: CircleImageView
    private lateinit var progressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient


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
        signInWithFacebookImageView = view.findViewById(R.id.signInWithFacebook)
        progressBar = view.findViewById(R.id.progressBarRegister)

        firebaseAuth = FirebaseAuth.getInstance()
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

    private fun showSignInOptions() {

        registerCardView.setOnClickListener {
            registerNewUser()
        }

        signInWithGoogleImageView.setOnClickListener {
            signInWithGoogle()
        }

        signInWithFacebookImageView.setOnClickListener {
            signInWithFacebook()
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
                    val intent = Intent(this.requireContext(), MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra(EXTRA_USERNAME, email)
                    }
                    startActivity(intent)
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

    }

    /**
     * Sign in with Facebook
     */
    private fun signInWithFacebook() {

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
