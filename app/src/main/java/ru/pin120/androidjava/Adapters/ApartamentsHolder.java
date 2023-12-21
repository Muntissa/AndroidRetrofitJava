package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class ApartamentsHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView number, area, tariff;
    CardView card;

    public ApartamentsHolder(@NonNull View itemView) {
        super(itemView);
        number = itemView.findViewById(R.id.apartamentsListItem_Number);
        area = itemView.findViewById(R.id.apartamentsListItem_Area);
        tariff = itemView.findViewById(R.id.apartamentsListItem_Tariff);
        card = itemView.findViewById(R.id.apartamentsItemCard);
        image = itemView.findViewById(R.id.imageApartaments);
    }
}
