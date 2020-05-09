package com.tarmsbd.schoolofthought.codered.app.ui.emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tarmsbd.schoolofthought.codered.app.R;
import com.tarmsbd.schoolofthought.codered.app.adapter.EmergencyAdapter;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity  implements EmergencyAdapter.ItemClickListener{
    Button organization;
    Button individual;
    EmergencyAdapter adapter;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        organization = (Button)findViewById(R.id.organization);
        individual = (Button)findViewById(R.id.individual);
        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Professor Md. Fazlul Kadir");
        animalNames.add("Dr. Md. Khaled Mohosin");
        animalNames.add("Professor Dr. SM Mostofa Kamal");
        animalNames.add("Dr. Md. Ferdousur Rahman");
        animalNames.add("Dr. Md. Jakir Hossen");
        animalNames.add("Dr. Maruf Bin Habib");
        animalNames.add("Dr. CM Shahin Kobir");
        animalNames.add("Dr. Azfar Hossen");

        ArrayList<String> phoneNumber = new ArrayList<>();
        phoneNumber.add("01407059563");
        phoneNumber.add("01407059564");
        phoneNumber.add("01407059566");
        phoneNumber.add("01407059567");
        phoneNumber.add("01407059568");
        phoneNumber.add("01407059569");
        phoneNumber.add("01407059570");
        phoneNumber.add("01407059571");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmergencyAdapter(this, animalNames,phoneNumber);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        organization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> animalNames = new ArrayList<>();
                animalNames.add("Professor Md. Fazlul Kadir");
                animalNames.add("Dr. Md. Khaled Mohosin");
                animalNames.add("Professor Dr. SM Mostofa Kamal");
                animalNames.add("Dr. Md. Ferdousur Rahman");
                animalNames.add("Dr. Md. Jakir Hossen");
                animalNames.add("Dr. Maruf Bin Habib");
                animalNames.add("Dr. CM Shahin Kobir");
                animalNames.add("Dr. Azfar Hossen");

                ArrayList<String> phoneNumber = new ArrayList<>();
                phoneNumber.add("01407059563");
                phoneNumber.add("01407059564");
                phoneNumber.add("01407059566");
                phoneNumber.add("01407059567");
                phoneNumber.add("01407059568");
                phoneNumber.add("01407059569");
                phoneNumber.add("01407059570");
                phoneNumber.add("01407059571");

                // set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rvAnimals);
                recyclerView.setLayoutManager(new LinearLayoutManager(EmergencyActivity.this));
                adapter = new EmergencyAdapter(EmergencyActivity.this, animalNames,phoneNumber);
                adapter.setClickListener(EmergencyActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> animalNames = new ArrayList<>();
                animalNames.add("Professor Md. Kamal Kadir");
                animalNames.add("Dr. Md. Khaled Mohosin");
                animalNames.add("Professor Dr. SM Mostofa Kamal");
                animalNames.add("Dr. Md. Ferdousur Rahman");
                animalNames.add("Dr. Md. Jakir Hossen");
                animalNames.add("Dr. Maruf Bin Habib");
                animalNames.add("Dr. CM Shahin Kobir");
                animalNames.add("Dr. Azfar Hossen");

                ArrayList<String> phoneNumber = new ArrayList<>();
                phoneNumber.add("01407059563");
                phoneNumber.add("01407059564");
                phoneNumber.add("01407059566");
                phoneNumber.add("01407059567");
                phoneNumber.add("01407059568");
                phoneNumber.add("01407059569");
                phoneNumber.add("01407059570");
                phoneNumber.add("01407059571");

                // set up the RecyclerView
                RecyclerView recyclerView = findViewById(R.id.rvAnimals);
                recyclerView.setLayoutManager(new LinearLayoutManager(EmergencyActivity.this));
                adapter = new EmergencyAdapter(EmergencyActivity.this, animalNames,phoneNumber);
                adapter.setClickListener(EmergencyActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
