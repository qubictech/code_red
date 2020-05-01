package com.tarmsbd.schoolofthought.codered.app.data.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.LoginUser
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.data.models.Report
import java.util.logging.Logger

/*all firebase database data will be handled from this object*/
object FirebaseRepo {

    val ref = FirebaseDatabase.getInstance().reference
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    fun isUserAlreadyExist(): Boolean {
        return false
    }

    fun loginUser(user: LoginUser, context: Context) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword("u${user.mobile}@red.com", user.password)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> {
                        toast("Login Successful", context)
                    }
                    task.isCanceled -> toast(
                        "Login Error: " + task.exception?.message,
                        context
                    )
                    else -> toast("error: ${task.exception?.message}", context)
                }
            }
    }

    fun registerUser(user: RegisterUser, context: Context) {
        Logger.getLogger("FirebaseRepo").warning(user.toString())
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

                        toast("Registration Complete", context)
                    }
                    task.isCanceled -> toast(
                        "Registration Canceled: " + task.exception?.message,
                        context
                    )
                    else -> toast("error: ${task.exception?.message}", context)
                }
            }
    }

    fun submitReportData(report: Report) {
        val reportRef = ref.child("reports").push()
        reportRef.setValue(report)
        Logger.getLogger("FirebaseRepo").warning("Report: Sent")
    }

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}