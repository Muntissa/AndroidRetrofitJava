package ru.pin120.androidjava.Adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Bookings;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.Entities.Services;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsHolder>{
    private List<Bookings> bookingsList;

    public BookingsAdapter(List<Bookings> bookingsList) {
        this.bookingsList = bookingsList;
    }

    @NonNull
    @Override
    public BookingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookings_listitem, parent, false);

        return new BookingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsHolder holder, int position) {
        Bookings booking = bookingsList.get(position);

        holder.number.setText("Номер: " + booking.getId());
        holder.client.setText(booking.getClient().getSecondName() + " " + booking.getClient().getFirstName() + " " + booking.getClient().getLastName());
        holder.price.setText((ChronoUnit.DAYS.between(LocalDate.parse(booking.getStartTime()), LocalDate.parse(booking.getEndTime())) *
                (booking.getApartament().getTariff().getPrice() + booking.getApartament().getArea() * 50) +
                booking.getServices().stream().mapToInt(Services::getPrice).sum() * 0.5) + " ₽");

    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }
}
