package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.RegistrationViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.FragmentRegistrationBinding
import com.tarmsbd.schoolofthought.codered.app.ui.ques.QuesActivity
import com.tarmsbd.schoolofthought.codered.app.ui.report.ReportActivity
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
private const val TAG = "RegistrationFragment"
private const val REPORT_ACTIVITY = "ReportActivity"
private const val QUES_ACTIVITY = "QuesActivity"

class RegistrationFragment : Fragment() {
    private lateinit var fragmentRegistrationBinding: FragmentRegistrationBinding
    private lateinit var registrationViewModel: RegistrationViewModel

    private val ref = FirebaseDatabase.getInstance().reference
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        registrationViewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]

        fragmentRegistrationBinding = FragmentRegistrationBinding
            .inflate(inflater, container, false).apply {
                viewModel = registrationViewModel
                lifecycleOwner = this@RegistrationFragment
            }

        fragmentRegistrationBinding.genderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> registrationViewModel.setGender("NONE")
                        1 -> registrationViewModel.setGender("M")
                        2 -> registrationViewModel.setGender("F")
                    }
                }

            }

        registrationViewModel.userDataObserver.observe(viewLifecycleOwner, Observer { user ->
            if (validate(user)) {
                fragmentRegistrationBinding.signupBtn.isEnabled = true
                fragmentRegistrationBinding.signupBtn.setOnClickListener {
                    registerUser(user)
                }
            } else {
                fragmentRegistrationBinding.signupBtn.isEnabled = false
            }
        })



        return fragmentRegistrationBinding.root
    }

    fun registerUser(user: RegisterUser) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())
        val intentExtraText = activity!!.intent.getStringExtra(AuthActivity.EXTRA_TEXT)

        if (firebaseUser != null)
            FirebaseAuth.getInstance().signOut()

        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword("u${user.mobile}@red.com", user.password)
            .addOnCompleteListener { task ->
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

                        toast("Registration Complete")
                    }
                    task.isCanceled -> toast(
                        "Registration Canceled: " + task.exception?.message
                    )
                    else -> toast("error: ${task.exception?.message}")
                }
            }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun validate(user: RegisterUser): Boolean {
        Logger.getLogger(TAG).warning(user.toString())
        val error = mutableListOf<String>()

        var valid = true
        if (user.gender == "NONE") {
            error.add("Gender")
            valid = false
        }

        if (user.dateOfBirth.isEmpty()) {
            error.add("Birth Date")
            valid = false
        }

        if (user.fullName.isEmpty()) {
            error.add("Name")
            valid = false
        }

        if (user.mobile.isEmpty()) {
            error.add("Mobile Number")
            valid = false
        }

        if (user.password.isEmpty()) {
            error.add("Password")
            valid = false
        }

        if (user.password.isNotEmpty() && user.password.length < 6) {
            error.add("Password should at least 6+ chars")
            valid = false
        }

        return valid
    }
}
