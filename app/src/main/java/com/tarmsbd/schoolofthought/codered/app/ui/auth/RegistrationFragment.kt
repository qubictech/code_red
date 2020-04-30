package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.RegistrationViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.FragmentRegistrationBinding

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {
    private lateinit var fragmentRegistrationBinding: FragmentRegistrationBinding
    private lateinit var registrationViewModel: RegistrationViewModel

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

        return fragmentRegistrationBinding.root
    }

}
