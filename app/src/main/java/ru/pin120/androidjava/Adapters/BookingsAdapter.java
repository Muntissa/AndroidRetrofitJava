package ru.pin120.androidjava.Adapters;

import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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

        String fullTextStart = "Нач. дата: " + booking.getStartTime();
        SpannableString sStringStart = new SpannableString(fullTextStart);
        int startIndex = 0;
        int endIndex = "Нач. дата:".length();
        sStringStart.setSpan(new StyleSpan(Typeface.BOLD), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.startTime.setText(sStringStart);

        String fullTextEnd = "Нач. дата: " + booking.getEndTime();
        SpannableString sStringEnd = new SpannableString(fullTextEnd);
        int startIndexEnd = 0;
        int endIndexEnd = "Кон. дата:".length();
        sStringEnd.setSpan(new StyleSpan(Typeface.BOLD), startIndexEnd, endIndexEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.endTime.setText(sStringEnd);

        String fullTextApart = "Номер апартаментов: " + booking.getApartament().getNumber();
        SpannableString sStringApart = new SpannableString(fullTextApart);
        int startIndexApart = 0;
        int endIndexApart = "Номер апартаментов: ".length();
        sStringApart.setSpan(new StyleSpan(Typeface.BOLD), startIndexApart, endIndexApart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.apartamentsNumber.setText(sStringApart);

        holder.services.setText("Услуги: \n" + String.join("\n", booking.getServices()
                .stream()
                .map(Services::getName)
                .collect(Collectors.toList())));

        holder.setDetailsVisibility();
    }

    @Override
    public int getItemCount() {
        return bookingsList.size();
    }
}
