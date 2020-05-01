package com.tarmsbd.schoolofthought.codered.app.ui.emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tarmsbd.schoolofthought.codered.app.R;
import com.tarmsbd.schoolofthought.codered.app.adapter.EmergencyAdapter;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity {
    EmergencyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

    }


}
