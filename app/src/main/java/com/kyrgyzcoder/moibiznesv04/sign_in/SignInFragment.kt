package com.kyrgyzcoder.moibiznesv04.sign_in

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
import com.google.firebase.auth.FirebaseAuth
import com.kyrgyzcoder.moibiznesv04.MainActivity
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.utils.EXTRA_USERNAME

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginView: CardView
    private lateinit var registerHereTextView: TextView
    private lateinit var mProgressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        emailEditText = view.findViewById(R.id.editTextEmailSignIn)
        passwordEditText = view.findViewById(R.id.editTextPasswordSignIn)
        loginView = view.findViewById(R.id.cardViewSignIn)
        registerHereTextView = view.findViewById(R.id.textViewRegisterHere)
        mProgressBar = view.findViewById(R.id.progressBarSignIn)
        mAuth = FirebaseAuth.getInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerHereTextView.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }

        showSignInOptions()
    }

    private fun showSignInOptions() {
        loginView.setOnClickListener {
            passwordEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (validateLoginInput(email, password)) {
                mProgressBar.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    mProgressBar.visibility = View.GONE
                    if (it.isSuccessful) {
                        val intent = Intent(this.requireContext(), MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            putExtra(EXTRA_USERNAME, email)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            getString(R.string.signInFail),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    /**
     * Util functions
     */

    private fun validateLoginInput(email: String, password: String): Boolean {

        var ret = true
        if (email.isEmpty() || !email.contains("@")) {
            ret = false
            emailEditText.error = getString(R.string.emailMustContain)
        } else if (password.isEmpty()) {
            ret = false
            passwordEditText.error = getString(R.string.passwordLength)
        }

        return ret
    }
}
