package com.tarmsbd.schoolofthought.codered.app.data.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tarmsbd.schoolofthought.codered.app.data.models.Report
import com.tarmsbd.schoolofthought.codered.app.data.models.SelfResult
import java.util.logging.Logger

/*all firebase database data will be handled from this object*/
object FirebaseRepo {

    val ref = FirebaseDatabase.getInstance().reference
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    fun isUserAlreadyExist(): Boolean {
        return false
    }

    fun submitResultData(selfResult: SelfResult) {
        val resultRef = ref.child("report_result").child(firebaseUser!!.uid)
        resultRef.updateChildren(selfResult.toMap())
    }

    fun submitReportData(report: Report) {
        val reportRef = ref.child("reports").push()
        reportRef.updateChildren(report.toMap())
        Logger.getLogger("FirebaseRepo").warning("Report: Sent")
    }

    private fun toast(msg: String, context: Context) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}