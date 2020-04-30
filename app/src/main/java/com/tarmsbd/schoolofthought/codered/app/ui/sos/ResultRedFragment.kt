package com.tarmsbd.schoolofthought.codered.app.ui.sos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tarmsbd.schoolofthought.codered.app.R
import kotlinx.android.synthetic.main.fragment_result_red.*

/**
 * A simple [Fragment] subclass.
 */
class ResultRedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_red, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        return_to_home.setOnClickListener {
            activity!!.finish()
        }
    }

}
