package com.tarmsbd.schoolofthought.codered.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tarmsbd.schoolofthought.codered.app.data.models.Report
import com.tarmsbd.schoolofthought.codered.app.data.models.SelfResult
import java.util.*
import java.util.logging.Logger

/*all firebase database data will be handled from this object*/
object FirebaseRepo {

    val ref = FirebaseDatabase.getInstance().reference
    val firebaseUser = FirebaseAuth.getInstance().currentUser

    // self assistant data
    fun submitResultData(selfResult: SelfResult) {
        val resultRef = ref.child("report_result").child(firebaseUser!!.uid)
        resultRef.updateChildren(selfResult.toMap())
    }

    // other report data
    fun submitReportData(report: Report) {
        val reportRef = ref.child("reports").push()
        reportRef.updateChildren(report.toMap())
        Logger.getLogger("FirebaseRepo").warning("Report: Sent")
    }

    // get report result from storage
    fun getReportResults(onChanged: (Boolean, List<SelfResult>) -> Unit) {
        val query: Query = ref.child("report_result")
//            .orderByChild("timestamp")
//            .equalTo("Red", "result")

        val resultList = mutableListOf<SelfResult>()

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                onChanged(true, resultList)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    Logger.getLogger("Snapshot").warning("${snapshot.childrenCount}")
                    resultList.clear()

                    snapshot.children.forEach { dataSnapshot ->
                        dataSnapshot.getValue(SelfResult::class.java)?.let { result ->
                            Logger.getLogger("SnapshotResult")
                                .warning(
                                    "Result: ${result.result} " +
                                            "\nlatlng: ${result.location}"
                                )

                            val diff: Long = Date().time - result.timestamp
                            val seconds = diff / 1000
                            val minutes = seconds / 60
                            val hours = minutes / 60
                            val days = hours / 24

                            if (days.toInt() == 1)
                                resultList.add(result)
                            else Logger.getLogger("SnapshotResult")
                                .warning("Previous Data: Day diff: $days")
                        }
                    }
                    onChanged(false, resultList)
                } else {
                    Logger.getLogger("Snapshot").warning("Nothing Found!")
                    onChanged(true, resultList)
                }
            }

        })


    }

}