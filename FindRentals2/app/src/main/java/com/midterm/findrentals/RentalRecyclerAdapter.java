package com.midterm.findrentals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RentalRecyclerAdapter extends RecyclerView.Adapter<RentalRecyclerAdapter.RentalViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Rental> mRentals;
    private List<Homeowner> mHomeowners;
    private Context context;

    public RentalRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    void setRental(List<Rental> rentals) {
        mRentals = rentals;
        notifyDataSetChanged();
    }

    void setHomeowner(List<Homeowner> homeowners) {
        mHomeowners = homeowners;
        notifyDataSetChanged();
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
        Rental currentRental = mRentals.get(position);
        int homeownerID = currentRental.getHomeownerID();
        for (Homeowner homeowner : mHomeowners) {
            if (homeowner.homeowner_id == homeownerID) {
                holder.setPhone(homeowner.telephoneNumber);
                break;
            }
        }
        holder.setAddress(currentRental.getAddress());
        holder.setPrice(currentRental.getCost());
    }

    public void filterList(List<Rental> filteredlist) {
        mRentals = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRentals != null) return mRentals.size();
        else return 0;
    }

    public class RentalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceView;
        private TextView addressView;
        private TextView phoneView;
        private ImageView imgView;
        private RentalRecyclerAdapter adapter;

        public RentalViewHolder(@NonNull View itemView, RentalRecyclerAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.priceView = itemView.findViewById(R.id.rentalItemPrice);
            this.addressView = itemView.findViewById(R.id.rentalItemAddress);
            this.phoneView = itemView.findViewById(R.id.rentalItemPhone);
            this.imgView = itemView.findViewById(R.id.rentalItemImg);
            itemView.setOnClickListener(this);
        }

        public void setAddress(String address) {
            addressView.setText(address);
        }

        public void setPhone(String phone) {
            phoneView.setText(String.valueOf(phone));
        }

        public void setPrice(int price) {
            priceView.setText(String.valueOf(price) + " VND");
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, RentalSpecific.class);
            Rental currentItem = mRentals.get(position);
            intent.putExtra("apartmentId", currentItem.getApartment_id());
            intent.putExtra("address", currentItem.getAddress());
            intent.putExtra("cost", currentItem.getCost());
            intent.putExtra("homeOwner", currentItem.getHomeownerID());
            intent.putExtra("capacity", currentItem.getCapacity());
            intent.putExtra("latitude", String.valueOf(currentItem.getLatitude()));
            intent.putExtra("longitude", String.valueOf(currentItem.getLongitude()));
            intent.putExtra("picNum", currentItem.getPicsNum());
            ((Activity) context).startActivity(intent);
        }


    }
}
