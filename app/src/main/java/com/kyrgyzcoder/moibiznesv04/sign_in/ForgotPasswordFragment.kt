package com.kyrgyzcoder.moibiznesv04.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kyrgyzcoder.moibiznesv04.R

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment() {

    private lateinit var editTextEmailForgotPwd: EditText
    private lateinit var getLinkCardView: CardView
    private lateinit var mProgressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        editTextEmailForgotPwd = view.findViewById(R.id.editTextEmailForgotPwd)
        getLinkCardView = view.findViewById(R.id.cardViewForgetPwd)
        mProgressBar = view.findViewById(R.id.progressBarForgotPwd)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        activity!!.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity!!.window.statusBarColor =
            ContextCompat.getColor(activity!!, R.color.backColorLoginToolM)
        firebaseAuth = FirebaseAuth.getInstance()

        getLinkCardView.setOnClickListener {
            recoverPassword()
        }
    }

    private fun recoverPassword() {
        val email = editTextEmailForgotPwd.text.toString().trim()

        if (email.isEmpty()) {
            editTextEmailForgotPwd.error = "Вводите email"
            return
        }
        if (email.contains("@")) {
            mProgressBar.visibility = View.VISIBLE
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                mProgressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        this.requireContext(),
                        "Ссылка на востановлени пароля отправлено на вашу почту!",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_signInFragment)
                } else {
                    Toast.makeText(
                        this.requireContext(),
                        "Ошибка: ${task.exception!!.message} Пожалуйста вводите правильный email",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            editTextEmailForgotPwd.error = "Вводите правильный email"
        }
    }

}
