package com.midterm.findrentals;

import android.content.Context;
import android.media.Image;
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

    public RentalRecyclerAdapter(Context context, ArrayList<Rental> rentals) {
        layoutInflater = LayoutInflater.from(context);
        this.items = rentals;
    }

    @NonNull
    @Override
    public RentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.rental_item, parent,false);
        return new RentalViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RentalViewHolder holder, int position) {
        Rental currentRental =items.get(position);
        holder.setAddress(currentRental.getAddress());
        holder.setId(currentRental.getId());
        holder.setPrice(currentRental.getPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder {
        private TextView priceView;
        private TextView addressView;
        private TextView idView;
        private ImageView imgView;
        private RentalRecyclerAdapter adapter;

        public RentalViewHolder(@NonNull View itemView, RentalRecyclerAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.priceView = itemView.findViewById(R.id.rentalItemPrice);
            this.addressView=itemView.findViewById(R.id.rentalItemAddress);
            this.idView=itemView.findViewById(R.id.rentalItemId);
            this.imgView=itemView.findViewById(R.id.rentalItemImg);
        }

        public void setAddress(Address address) {
            String addressString = address.getNumber()+" "+address.getStreetName()+","+address.getDistrict()+","+address.getWard()+","+address.getCity();
            addressView.setText(addressString);
        }

        public void setId(int id) {
            idView.setText(String.valueOf(id));
        }

        public void setPrice(int price) {
            priceView.setText(String.valueOf(price));
        }
    }
}
