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

        fragmentLoginBinding.register.setOnClickListener {
            val authActivity: AuthActivity = activity as AuthActivity
            authActivity.switchFragment(RegistrationFragment())
        }
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val map = hashMapOf<String, String>()
        map["Question1_answer"] = "No"
        map["Question2_answer"] = "No"
        map["Question3_answer"] = "Yes"
        map["Question4_answer"] = "No"
        map["Question5_answer"] = "No"

        mainViewModel.getResponse(map).observe(viewLifecycleOwner, Observer {
            Logger.getLogger("Response").warning(it.toString())
        })
    }
}