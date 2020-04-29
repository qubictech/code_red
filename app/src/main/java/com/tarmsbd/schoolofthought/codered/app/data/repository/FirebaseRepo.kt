package com.tarmsbd.schoolofthought.codered.app.data.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.User

/*all firebase database data will be handled from this object*/
object FirebaseRepo {
    val ref = FirebaseDatabase.getInstance().reference
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    fun registerForSelf(user: User, context: Context) {
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

    fun registerForOthers(user: User) {

    }
}