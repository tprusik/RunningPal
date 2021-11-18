package com.example.runningpal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningpal.CalendarViewHolder;
import com.example.runningpal.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    public final ArrayList<String> daysofMonth;
    public final OnItemListener onItemListener;

    public CalendarAdapter(ArrayList<String> daysofMonth, OnItemListener onItemListener) {
        this.daysofMonth = daysofMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166);

        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
    holder.dayofMonth.setText(daysofMonth.get(position));
    }

    @Override
    public int getItemCount() {
        return daysofMonth.size();
    }

    public interface OnItemListener{

        void onItemClick(int position,String dayText);
    }
}
