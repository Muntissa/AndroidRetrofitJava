package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class FacilitiesHolder extends RecyclerView.ViewHolder {
    TextView name;
    CardView card;

    public FacilitiesHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.facilitiesListItem_Name);
        card = itemView.findViewById(R.id.item_card);
    }
}
