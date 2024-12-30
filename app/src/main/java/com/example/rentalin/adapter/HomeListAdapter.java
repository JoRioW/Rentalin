package com.example.rentalin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rentalin.R;
import com.example.rentalin.model.Car;

import java.text.DecimalFormat;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>{
    private List<Car> carList;
    private OnCarClickListener listener;

    public interface OnCarClickListener{
        void onCarClick(Car car);
    }

    public HomeListAdapter(List<Car> carList, OnCarClickListener listener) {
        this.carList = carList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homerv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {

        Car car = carList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(car.getCar_image())
                .placeholder(R.drawable.baseline_account_circle_24)
                .into(holder.car_image);
        holder.car_name.setText(car.getCar_name());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        holder.price_per_day.setText("Rp" + decimalFormat.format(car.getPrice_per_day()) + "/day");
        holder.itemView.setOnClickListener(v -> listener.onCarClick(car));
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView car_image;
        TextView car_name, price_per_day;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            car_image = itemView.findViewById(R.id.imageItemRV);
            car_name = itemView.findViewById(R.id.nameItemRV);
            price_per_day = itemView.findViewById(R.id.priceItemRV);
        }
    }
}
