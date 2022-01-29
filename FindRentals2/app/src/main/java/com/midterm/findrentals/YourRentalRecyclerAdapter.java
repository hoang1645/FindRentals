package com.midterm.findrentals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class YourRentalRecyclerAdapter extends RecyclerView.Adapter<YourRentalRecyclerAdapter.YourRentalViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Rental> mRentals;
    private Context context;

    public YourRentalRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    void setRental(List<Rental> rentals) {
        mRentals = rentals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public YourRentalRecyclerAdapter.YourRentalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.rental_item, parent, false);
        YourRentalRecyclerAdapter.YourRentalViewHolder holder
                = new YourRentalRecyclerAdapter.YourRentalViewHolder(itemView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull YourRentalRecyclerAdapter.YourRentalViewHolder holder, int position) {
        Rental currentRental = mRentals.get(position);
        holder.setCapacity(currentRental.getCapacity() + "m2");
        holder.setAddress(currentRental.getAddress());
        holder.setPrice(currentRental.getCost());
        holder.setImage("house_" + currentRental.getApartment_id() + "_1");
    }

    public void filterList(List<Rental> filteredList) {
        mRentals = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mRentals != null) return mRentals.size();
        else return 0;
    }

    public class YourRentalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceView;
        private TextView addressView;
        private TextView capacityView;
        private ImageView imgView;
        private YourRentalRecyclerAdapter adapter;

        public YourRentalViewHolder(@NonNull View itemView, YourRentalRecyclerAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            this.priceView = itemView.findViewById(R.id.rentalItemPrice);
            this.addressView = itemView.findViewById(R.id.rentalItemAddress);
            this.capacityView = itemView.findViewById(R.id.rentalItemCapacity);
            this.imgView = itemView.findViewById(R.id.rentalItemImg);
            itemView.setOnClickListener(this);
        }

        public Drawable getImage(Context context, String name) {
            try {
                Drawable rs = context.getResources().getDrawable(context.getResources()
                        .getIdentifier(name, "drawable", context.getPackageName()));
                return rs;
            } catch (Exception e) {
                return null;
            }
        }

        public void setImage(String imageName) {
            Drawable rs = getImage(context, imageName);
            if (rs != null)
                imgView.setImageDrawable(rs);
            else {
                imgView.setImageDrawable(getImage(context,"house"));
            }
        }

        public void setAddress(String address) {
            addressView.setText(address);
        }

        public void setCapacity(String capacity) {
            capacityView.setText(capacity);
        }

        public void setPrice(int price) {
            priceView.setText(String.valueOf(price) + " VND");
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Intent intent = new Intent(context, UpdateRentalActivity.class);
            Rental currentItem = mRentals.get(position);
            intent.putExtra("apartment_id", currentItem.getApartment_id());
            ((Activity) context).startActivity(intent);
        }
    }
}
