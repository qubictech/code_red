package com.tarmsbd.schoolofthought.codered.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tarmsbd.schoolofthought.codered.app.R;

import java.util.List;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<String> Name;
    private List<String> Phone;
    private LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public EmergencyAdapter(Context context, List<String> Name, List<String> Phone) {
        this.mInflater = LayoutInflater.from(context);
        this.Name = Name;
        this.Phone = Phone;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.emergency_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = Name.get(position);
        holder.nameTextView.setText(animal);
        String phone = Phone.get(position);
        holder.phoneTextView.setText(phone);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return Name.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView phoneTextView;
        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            phoneTextView = itemView.findViewById(R.id.textViewPhone);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return Name.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
