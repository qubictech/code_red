package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.AuthViewModel
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.FragmentLoginBinding
import java.util.logging.Logger

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

        val intentExtraText = activity!!.intent.getStringExtra(AuthActivity.EXTRA_TEXT)
        intentExtraText?.let { authViewModel.setIntentName = it }

        fragmentLoginBinding.register.setOnClickListener {
            val authActivity: AuthActivity = activity as AuthActivity
            authActivity.switchFragment(RegistrationFragment())
        }
        return fragmentLoginBinding.root
    }
}