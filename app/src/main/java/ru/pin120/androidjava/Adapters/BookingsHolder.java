package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class BookingsHolder extends RecyclerView.ViewHolder{
    TextView number, client, price, startTime, endTime, apartamentsNumber, services;
    CardView card;
    boolean opened = false;

    public BookingsHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.bookingsListItem_Number);
        client = itemView.findViewById(R.id.bookingsListItem_Client);
        price = itemView.findViewById(R.id.bookingsListItem_Price);
        card = itemView.findViewById(R.id.bookingsItemCard);

        startTime = itemView.findViewById(R.id.bookingsListItem_StartTimeDetail);
        endTime = itemView.findViewById(R.id.bookingsListItem_EndTimeDetail);
        apartamentsNumber = itemView.findViewById(R.id.bookingsListItem_ApatamentDetail);
        services = itemView.findViewById(R.id.bookingsListItem_ServicesDetail);

        card.setOnClickListener(v -> {
            opened = !opened;
            setDetailsVisibility();
        });
    }

    public void setDetailsVisibility() {
        if(opened) {
            startTime.setVisibility(View.VISIBLE);
            endTime.setVisibility(View.VISIBLE);
            apartamentsNumber.setVisibility(View.VISIBLE);
            services.setVisibility(View.VISIBLE);
        }
        else {
            startTime.setVisibility(View.GONE);
            endTime.setVisibility(View.GONE);
            apartamentsNumber.setVisibility(View.GONE);
            services.setVisibility(View.GONE);
        }
    }
}
