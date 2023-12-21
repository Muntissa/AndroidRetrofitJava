package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class ServicesHolder extends RecyclerView.ViewHolder{
    TextView name, price, description;
    CardView card;

    public ServicesHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.servicesListItem_Name);
        price = itemView.findViewById(R.id.servicesListItem_Price);
        description = itemView.findViewById(R.id.servicesListItem_Description);
        card = itemView.findViewById(R.id.item_card);
    }
}
