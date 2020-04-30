package com.tarmsbd.schoolofthought.codered.app.data.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.LoginUser
import com.tarmsbd.schoolofthought.codered.app.data.models.RegisterUser
import com.tarmsbd.schoolofthought.codered.app.data.models.RegistrationForOther
import com.tarmsbd.schoolofthought.codered.app.data.models.RegistrationForSelf
import java.util.logging.Logger

/*all firebase database data will be handled from this object*/
object FirebaseRepo {
    const val REPORT_ACTIVITY = "ReportActivity"
    const val QUES_ACTIVITY = "QuesActivity"

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
                    task.isSuccessful -> toast("Registration Complete", context)
                    task.isCanceled -> toast(
                        "Registration Canceled: " + task.exception?.message,
                        context
                    )
                    else -> toast("error: ${task.exception?.message}", context)
                }
            }
    }

    fun registerForSelf(user: RegistrationForSelf, context: Context) {
        if (firebaseUser == null) {
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val mUser: FirebaseUser? = task.result?.user
                    val reg = mUser?.uid?.let { ref.child("self_registration").child(it) }
                    reg?.setValue(user)

                    Toast.makeText(context, "Registration Complete", Toast.LENGTH_LONG).show()
                } else Toast.makeText(context, "Failed to Registration", Toast.LENGTH_LONG).show()
            }
        } else {
            val reg = firebaseUser.uid.let { ref.child("self_registration").child(it) }
            reg.setValue(user)

            Toast.makeText(context, "Registration Complete", Toast.LENGTH_LONG).show()
        }
    }

    fun registerForOthers(user: RegistrationForOther, context: Context) {
        if (firebaseUser == null) {
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val mUser: FirebaseUser? = task.result?.user
                    val reg = mUser?.uid?.let { ref.child("other_registration").child(it).push() }
                    reg?.setValue(user)

                    Toast.makeText(context, "Registration Complete", Toast.LENGTH_LONG).show()
                } else Toast.makeText(context, "Failed to Registration", Toast.LENGTH_LONG).show()
            }
        } else {
            val reg = firebaseUser.uid.let { ref.child("other_registration").child(it).push() }
            reg.setValue(user)

            Toast.makeText(context, "Registration Complete", Toast.LENGTH_LONG).show()
        }
    }

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}