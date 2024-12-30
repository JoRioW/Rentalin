package com.example.rentalin.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.example.rentalin.model.Booking;
import com.example.rentalin.ui.PaymentActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<Booking> bookList;

    public HistoryAdapter(List<Booking> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historyrv_item, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        Booking booking = bookList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(booking.getCar_image())
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(holder.historyBookIV);
        holder.historyBookNameTV.setText(booking.getCar_name());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        holder.historyTotalTV.setText("Rp" + decimalFormat.format(booking.getTotal_price()));
        holder.historyBookStartTV.setText("Start date: " + booking.getStart_date());
        holder.historyBookEndTV.setText("End date: " + booking.getEnd_date());
        holder.historyBookMethodTV.setText(booking.getPayment_method());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView historyBookIV;
        TextView historyBookNameTV, historyTotalTV, historyBookStartTV, historyBookEndTV, historyBookMethodTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyBookIV = itemView.findViewById(R.id.historyBookIV);
            historyBookNameTV = itemView.findViewById(R.id.historyBookNameTV);
            historyBookMethodTV = itemView.findViewById(R.id.historyBookMethodTV);
            historyBookStartTV = itemView.findViewById(R.id.historyBookStartTV);
            historyBookEndTV = itemView.findViewById(R.id.historyBookEndTV);
            historyTotalTV = itemView.findViewById(R.id.historyTotalTV);
        }
    }
}
