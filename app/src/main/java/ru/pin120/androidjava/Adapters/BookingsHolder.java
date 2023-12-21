package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class BookingsHolder extends RecyclerView.ViewHolder{
    TextView number, client, price;
    CardView card;

    public BookingsHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.bookingsListItem_Number);
        client = itemView.findViewById(R.id.bookingsListItem_Client);
        price = itemView.findViewById(R.id.bookingsListItem_Price);
        card = itemView.findViewById(R.id.bookingsItemCard);
    }
}
