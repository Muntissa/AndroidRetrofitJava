package ru.pin120.androidjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.R;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesHolder>{
    private List<Services> servicesList;

    public ServicesAdapter(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.services_listitem, parent, false);

        return new ServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesHolder holder, int position) {
        Services service = servicesList.get(position);

        holder.name.setText(service.getName());
        holder.price.setText("Цена: " + service.getPrice() + " ₽");
        holder.description.setText(service.getDescription());
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }
}
