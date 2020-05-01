package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.tarmsbd.schoolofthought.codered.app.data.models.LoginUser
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.AuthViewModel
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.FragmentLoginBinding
import com.tarmsbd.schoolofthought.codered.app.ui.ques.QuesActivity
import com.tarmsbd.schoolofthought.codered.app.ui.report.ReportActivity
import java.util.logging.Logger

private const val REPORT_ACTIVITY = "ReportActivity"
private const val QUES_ACTIVITY = "QuesActivity"

class LoginFragment : Fragment() {
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var mainViewModel: MainViewModel
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        authViewModel =
            ViewModelProvider(this)[AuthViewModel::class.java]
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        fragmentLoginBinding.apply {
            lifecycleOwner = this@LoginFragment
            viewModel = authViewModel
        }

        authViewModel.userCredentialForLogin.observe(viewLifecycleOwner, Observer { user ->
            if (user.mobile.isNotEmpty() && user.password.isNotEmpty()) {
                fragmentLoginBinding.loginBtn.isEnabled = true
                fragmentLoginBinding.loginBtn.setOnClickListener {
                    loginUser(user)
                }
            } else {
                fragmentLoginBinding.loginBtn.isEnabled = false
                fragmentLoginBinding.loginBtn.setOnClickListener(null)
            }
        })


        fragmentLoginBinding.register.setOnClickListener {
            val authActivity: AuthActivity = activity as AuthActivity
            authActivity.switchFragment(RegistrationFragment())
        }
        return fragmentLoginBinding.root
    }

    fun loginUser(user: LoginUser) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())
        val intentExtraText = activity!!.intent.getStringExtra(AuthActivity.EXTRA_TEXT)

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword("u${user.mobile}@red.com", user.password)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        toast("Login Successful", requireContext())
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
                    task.isCanceled -> toast(
                        "Login Error: " + task.exception?.message,
                        requireContext()
                    )
                    else -> toast("error: ${task.exception?.message}", requireContext())
                }
            }
    }

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}