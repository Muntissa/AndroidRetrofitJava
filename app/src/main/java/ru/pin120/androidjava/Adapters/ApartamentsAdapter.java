package ru.pin120.androidjava.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.ResourceHelper;

public class ApartamentsAdapter extends RecyclerView.Adapter<ApartamentsHolder>{
    private List<Apartaments> apartamentsList;

    public ApartamentsAdapter(List<Apartaments> apartamentsList) {
        this.apartamentsList = apartamentsList;
    }

    @NonNull
    @Override
    public ApartamentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.apartaments_listitem, parent, false);

        return new ApartamentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartamentsHolder holder, int position) {
        Apartaments apartament = apartamentsList.get(position);

        holder.number.setText("Номер: " + apartament.getNumber());
        holder.area.setText("Площадь: " + apartament.getArea());
        holder.tariff.setText("Тариф: " + apartament.getTariff().getName());

        holder.image.setImageResource(R.drawable.testimage);
/*        String photo = apartament.getPhotoPath().toLowerCase();

        int resouceId = ResourceHelper.getResourceIdByName(holder.image.getContext(), photo);

        if(resouceId != 0)
            holder.image.setImageResource(resouceId);
        else
            holder.image.setImageResource(R.drawable.defaultimage);*/
    }

    @Override
    public int getItemCount() {
        return apartamentsList.size();
    }
}
