package ru.pin120.androidjava.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.R;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesHolder> {
    private List<Facilities> facilitiesList;

    public FacilitiesAdapter(List<Facilities> facilitiesList) {
        this.facilitiesList = facilitiesList;
    }

    @NonNull
    @Override
    public FacilitiesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.facilities_listitem, parent, false);

        return new FacilitiesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacilitiesHolder holder, int position) {
        Facilities facility = facilitiesList.get(position);

        holder.name.setText(facility.getName());
    }

    @Override
    public int getItemCount() {
        return facilitiesList.size();
    }
}
