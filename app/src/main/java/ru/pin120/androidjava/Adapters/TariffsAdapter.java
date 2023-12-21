package ru.pin120.androidjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;

public class TariffsAdapter extends RecyclerView.Adapter<TariffsHolder> {
    private List<Tariffs> tariffsList;

    public TariffsAdapter(List<Tariffs> tariffsList) {
        this.tariffsList = tariffsList;
    }

    @NonNull
    @Override
    public TariffsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tariffs_listitem, parent, false);

        return new TariffsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffsHolder holder, int position) {
        Tariffs tariff = tariffsList.get(position);

        holder.name.setText(tariff.getName());
        holder.description.setText(tariff.getDescription());
        holder.price.setText("Цена: " + tariff.getPrice() + " ₽");
    }

    @Override
    public int getItemCount() {
        return tariffsList.size();
    }
}
