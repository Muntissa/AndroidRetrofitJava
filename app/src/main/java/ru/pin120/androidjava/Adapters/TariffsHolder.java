package ru.pin120.androidjava.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.pin120.androidjava.R;

public class TariffsHolder extends RecyclerView.ViewHolder{

    TextView name, price, description;
    CardView card;

    public TariffsHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tariffsListItem_Name);
        price = itemView.findViewById(R.id.tariffsListItem_Price);
        description = itemView.findViewById(R.id.tariffsListItem_Description);
        card = itemView.findViewById(R.id.item_card);
    }
}
