package com.tarmsbd.schoolofthought.codered.app.ui.auth

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tarmsbd.schoolofthought.codered.app.R
import com.tarmsbd.schoolofthought.codered.app.data.viewmodel.MainViewModel
import com.tarmsbd.schoolofthought.codered.app.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TEXT = "com.tarmsbd.schoolofthought.codered.app.ui.auth.EXTRA_TEXT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val authBinding: ActivityAuthBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_auth)

        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        authBinding.apply {
            lifecycleOwner = this@AuthActivity
            viewModel = mainViewModel
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, LoginFragment()).commit()
        }

    }

    fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("LoginFragment")
            .replace(R.id.fragment_container, fragment).commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) super.onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}
