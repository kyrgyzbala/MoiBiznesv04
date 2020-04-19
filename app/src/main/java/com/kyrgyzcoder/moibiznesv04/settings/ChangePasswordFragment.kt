package com.kyrgyzcoder.moibiznesv04.settings

import android.content.Intent
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.kyrgyzcoder.moibiznesv04.R
import com.kyrgyzcoder.moibiznesv04.sign_in.SignInActivity

/**
 * A simple [Fragment] subclass Handles changing user's password
 */
class ChangePasswordFragment : Fragment() {

    private lateinit var pwdOne: EditText
    private lateinit var pwdTwo: EditText
    private lateinit var changeCardView: CardView
    private lateinit var dialog: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        pwdOne = view.findViewById(R.id.editTextPasswordChangeFirst)
        pwdTwo = view.findViewById(R.id.editTextPasswordChangeSecond)
        changeCardView = view.findViewById(R.id.cardViewChange)
        mAuth = FirebaseAuth.getInstance()

        dialog = view.findViewById(R.id.progress_bar_changepwd)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        changeCardView.setOnClickListener {
            showChangePwd()
        }
    }

    private fun showChangePwd() {
        pwdTwo.onEditorAction(EditorInfo.IME_ACTION_DONE)
        val p1 = pwdOne.text.toString().trim()
        val p2 = pwdTwo.text.toString().trim()
        if (validatePwds(p1, p2)) {
            val builder = AlertDialog.Builder(
                ContextThemeWrapper(
                    this.requireContext(),
                    R.style.AlertDialogStyle
                )
            )
            builder.setTitle("Внимание!").setMessage(getString(R.string.changepwdmessage))
                .setNegativeButton("Отменить") { _, _ ->
                    Toast.makeText(this.requireContext(), "Отменено!", Toast.LENGTH_SHORT).show()
                }
            builder.setPositiveButton("Сменить") { _, _ ->
                changePassword(p1)
            }
            builder.create().show()
        }
    }

    private fun changePassword(pwd: String) {
        val user = mAuth.currentUser
        if (user != null) {
            dialog.visibility = View.VISIBLE
            user.updatePassword(pwd).addOnCompleteListener { task ->
                dialog.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        this.requireContext(),
                        "Пароль успешьно изменен!",
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
                        "Error! -> ${task.exception!!.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun validatePwds(p1: String, p2: String): Boolean {
        var ret = true
        if (p1.isEmpty()) {
            pwdOne.error = "Вводиите пароль"
            ret = false
        }
        if (p2.isEmpty()) {
            pwdTwo.error = "Вводиите пароль"
            ret = false
        }
        if (p1 != p2) {
            pwdTwo.error = getString(R.string.passwordsMustMatch)
            ret = false
        }
        return ret
    }
}
