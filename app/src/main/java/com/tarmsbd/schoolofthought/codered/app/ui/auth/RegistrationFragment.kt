package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.RegistrationViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.FragmentRegistrationBinding
import com.tarmsbd.schoolofthought.codered.app.utils.MyPatterns
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

        // initially set male clicked!
        fragmentRegistrationBinding.genderMale.background.setColorFilter(
            Color.parseColor("#EE2B60"),
            PorterDuff.Mode.SRC_ATOP
        )

        fragmentRegistrationBinding.genderMale.setOnClickListener {
            registrationViewModel.setGender("M")
            fragmentRegistrationBinding.genderMale.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

        }

        fragmentRegistrationBinding.genderFemale.setOnClickListener {
            registrationViewModel.setGender("F")

            fragmentRegistrationBinding.genderMale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        fragmentRegistrationBinding.genderOthers.setOnClickListener {
            registrationViewModel.setGender("O")

            fragmentRegistrationBinding.genderMale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderFemale.background.setColorFilter(
                Color.parseColor("#CCCCCC"),
                PorterDuff.Mode.SRC_ATOP
            )

            fragmentRegistrationBinding.genderOthers.background.setColorFilter(
                Color.parseColor("#EE2B60"),
                PorterDuff.Mode.SRC_ATOP
            )
        }

        registrationViewModel.userDataObserver.observe(viewLifecycleOwner, Observer { user ->
            if (MyPatterns.NUMBER_PATTERN.matches(user.mobile) && user.password.length >= 6 && user.fullName.isNotEmpty() &&
                user.gender.isNotEmpty()
            ) {
                if (user.dateOfBirth.isNotEmpty()) {
                    fragmentRegistrationBinding.signupBtn.isEnabled = true
                    fragmentRegistrationBinding.signupBtn.setOnClickListener {
                        if (registrationViewModel.confirmPassword != user.password) {
                            fragmentRegistrationBinding.confirmPassword.error =
                                "Password Not Matched!"
                        } else {
                            fragmentRegistrationBinding.confirmPassword.error = null
                            registerUser(user)
                        }
                    }
                } else {
//                    toast("Birth Date is required")
                    Log.d("Registration", "Birthdate missing")
                }
            } else {
                fragmentRegistrationBinding.signupBtn.isEnabled = false
            }
        })

        return fragmentRegistrationBinding.root
    }

    private fun registerUser(user: RegisterUser) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())
        val intentExtraText = activity!!.intent.getStringExtra(AuthActivity.EXTRA_TEXT)

        if (firebaseUser != null)
            FirebaseAuth.getInstance().signOut()

        val authActivity: AuthActivity = activity as AuthActivity
        authActivity.switchFragment(AuthenticationFragment.newInstance(user))
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
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

    private fun showProgressDialog(): ProgressDialog {
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Creating new account. Please Wait!")
        progressDialog.setCancelable(false)

        return progressDialog
    }
}
