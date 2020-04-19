package com.kyrgyzcoder.moibiznesv04.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kyrgyzcoder.moibiznesv04.R

/**
 * A simple [Fragment] subclass.
 */
class ReAuthFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var pBar: ProgressBar
    private lateinit var reAuth: CardView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_re_auth, container, false)
        emailEditText = view.findViewById(R.id.editTextEmailRe)
        passwordEditText = view.findViewById(R.id.editTextPasswordRe)
        pBar = view.findViewById(R.id.progress_bar_reAuth)
        reAuth = view.findViewById(R.id.cardViewReAuth)

        mAuth = FirebaseAuth.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSignInOptions()
    }

    private fun showSignInOptions() {
        reAuth.setOnClickListener {
            passwordEditText.onEditorAction(EditorInfo.IME_ACTION_DONE)
            mAuth = FirebaseAuth.getInstance()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (validateLoginInput(email, password)) {
                pBar.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    pBar.visibility = View.GONE
                    if (it.isSuccessful) {
                        val user = mAuth.currentUser
                        if (user != null && user.isEmailVerified) {
                            findNavController().navigate(R.id.action_reAuthFragment_to_changePasswordFragment)
                        } else {
                            Toast.makeText(
                                this.requireContext(),
                                "Пожалуйста подтвердите свой email!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
