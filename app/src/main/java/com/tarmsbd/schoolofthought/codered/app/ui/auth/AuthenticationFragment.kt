package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.ui.ques.QuesActivity
import com.tarmsbd.schoolofthought.codered.app.ui.report.ReportActivity
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

private const val REGISTER_USER =
    "com.tarmsbd.schoolofthought.codered.app.ui.auth.USER_REGISTRATION"
private const val REPORT_ACTIVITY = "ReportActivity"
private const val QUES_ACTIVITY = "QuesActivity"

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: RegisterUser
    private val ref = FirebaseDatabase.getInstance().reference

    private lateinit var p1: EditText
    private lateinit var p2: EditText
    private lateinit var p3: EditText
    private lateinit var p4: EditText
    private lateinit var p5: EditText
    private lateinit var p6: EditText

    private var c1 = ""
    private var c2 = ""
    private var c3 = ""
    private var c4 = ""
    private var c5 = ""
    private var c6 = ""

    private var verificationId: String? = ""

    private var mVerificationCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(REGISTER_USER)!!
            startPhoneNumberAuthentication("+88${user.mobile}")
        }
    }

    companion object {
        const val TAG = "AuthenticationFragment"

        @JvmStatic
        fun newInstance(registerUser: RegisterUser) =
            AuthenticationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(REGISTER_USER, registerUser)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        view.findViewById<MaterialButton>(R.id.continue_btn)
            .setOnClickListener {
                if (mVerificationCode.length == 6) verifyPhoneNumber(
                    verificationId,
                    mVerificationCode
                ) else {
                    Logger.getLogger(TAG)
                        .warning("OTP : $mVerificationCode")
                }
            }

        p1 = view.findViewById(R.id.p1)
        p1.addTextChangedListener(GenericTextWatcher(p1))
        p1.requestFocus()

        p2 = view.findViewById(R.id.p2)
        p2.addTextChangedListener(GenericTextWatcher(p2))

        p3 = view.findViewById(R.id.p3)
        p3.addTextChangedListener(GenericTextWatcher(p3))

        p4 = view.findViewById(R.id.p4)
        p4.addTextChangedListener(GenericTextWatcher(p4))

        p5 = view.findViewById(R.id.p5)
        p5.addTextChangedListener(GenericTextWatcher(p5))

        p6 = view.findViewById(R.id.p6)
        p6.addTextChangedListener(GenericTextWatcher(p6))
    }

    private fun startPhoneNumberAuthentication(phoneNumber: String) {
        activity?.let { fragment ->
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                fragment,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        signInWithPhoneAuthCredential(p0)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        p0.printStackTrace()
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                        verificationId = p0

                        Toast.makeText(context, "Code Sent", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun verifyPhoneNumber(verificationId: String?, code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    private fun linkWithEmailAndPassword(credential: AuthCredential) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())
        val intentExtraText = activity!!.intent.getStringExtra(AuthActivity.EXTRA_TEXT)

        auth.currentUser?.let {
            it.linkWithCredential(credential).addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        task.result?.user?.let {
                            val registerRef = ref.child("users").child(it.uid)
                            registerRef.setValue(user)
                        }

                        when (intentExtraText) {
                            QUES_ACTIVITY -> {
                                startActivity(Intent(context, QuesActivity::class.java))
                                activity!!.finish()
                            }
                            REPORT_ACTIVITY -> {
                                startActivity(Intent(context, ReportActivity::class.java))
                                activity!!.finish()
                            }
                        }
                    }
                    task.isCanceled -> {
                        showDialog("Error: ${task.exception?.message?.replace("email", "mobile")}")
                    }
                    else -> {
                        showDialog("Error: ${task.exception?.message?.replace("email", "mobile")}")
                    }
                }

            }
        }
    }


    private fun showDialog(msg: String) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Registration Failed!")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("Retry!") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create()

        dialog.show()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    Logger.getLogger(TAG).warning("signInWithCredential:success")

                    linkWithEmailAndPassword(
                        EmailAuthProvider
                            .getCredential("u${user.mobile}@red.com", user.password)
                    )
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Logger.getLogger(TAG)
                        .warning("signInWithCredential:failure ${task.exception?.message}")

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(context, "Enter a valid code", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
    }

    inner class GenericTextWatcher(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val string = s.toString()

            when (view.id) {
                R.id.p1 -> if (string.length == 1) {
                    p2.requestFocus()
                    c1 = string
                } else if (string.isEmpty()) {
                    c1 = ""
                }

                R.id.p2 -> if (string.length == 1) {
                    p3.requestFocus()
                    c2 = string
                } else if (string.isEmpty()) {
                    p1.requestFocus()
                    c2 = ""
                }

                R.id.p3 -> if (string.length == 1) {
                    p4.requestFocus()
                    c3 = string
                } else if (string.isEmpty()) {
                    p2.requestFocus()
                    c3 = ""
                }

                R.id.p4 -> if (string.length == 1) {
                    p5.requestFocus()
                    c4 = string
                } else if (string.isEmpty()) {
                    p3.requestFocus()
                    c4 = ""
                }

                R.id.p5 -> if (string.length == 1) {
                    p6.requestFocus()
                    c5 = string
                } else if (string.isEmpty()) {
                    p4.requestFocus()
                    c5 = ""
                }

                R.id.p6 -> c6 = if (string.isNotEmpty()) {
                    string
                } else {
                    ""
                }
            }

            mVerificationCode = c1 + c2 + c3 + c4 + c5 + c6
            Logger.getLogger(TAG).warning("Watcher ${s.toString()}")
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }
}
