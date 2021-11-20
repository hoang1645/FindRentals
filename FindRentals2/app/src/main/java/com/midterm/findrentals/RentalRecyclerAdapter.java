package com.midterm.findrentals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RentalRecyclerAdapter extends RecyclerView.Adapter<RentalRecyclerAdapter.RentalViewHolder> {
    private LayoutInflater layoutInflater;
    private ArrayList<Rental> items;
    private Context context;


    public RentalRecyclerAdapter(Context context, ArrayList<Rental> rentals) {
        layoutInflater = LayoutInflater.from(context);
        this.items = rentals;
        this.context = context;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.rental_item, parent, false);
        RentalViewHolder holder = new RentalViewHolder(itemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental currentRental = items.get(position);
        holder.setAddress(currentRental.getAddress());
        holder.setId(currentRental.getApartment_id());
        holder.setPrice(currentRental.getCost());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceView;
        private TextView addressView;
        private TextView idView;
        private ImageView imgView;
        private RentalRecyclerAdapter adapter;

        public RentalViewHolder(@NonNull View itemView, RentalRecyclerAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.priceView = itemView.findViewById(R.id.rentalItemPrice);
            this.addressView = itemView.findViewById(R.id.rentalItemAddress);
            this.idView = itemView.findViewById(R.id.rentalItemId);
            this.imgView = itemView.findViewById(R.id.rentalItemImg);
            itemView.setOnClickListener(this);
        }

        public void setAddress(String address) {
            addressView.setText(address);
        }

        public void setId(int id) {
            idView.setText(String.valueOf(id));
        }

        public void setPrice(int price) {
            priceView.setText(String.valueOf(price) + " VND");
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, RentalSpecific.class);
            Rental currentItem = items.get(position);
            intent.putExtra("apartmentId", currentItem.getApartment_id());
            intent.putExtra("address", currentItem.getAddress());
            intent.putExtra("cost",currentItem.getCost());
            intent.putExtra("homeOwner", currentItem.getHomeownerID());
            intent.putExtra("capacity", currentItem.getCapacity());
            intent.putExtra("latitude", String.valueOf(currentItem.getLatitude()));
            intent.putExtra("longitude", String.valueOf(currentItem.getLongitude()));
            intent.putExtra("picNum", currentItem.getPicsNum());
            ((Activity) context).startActivity(intent);
        }


    }
}
