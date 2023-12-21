package ru.pin120.androidjava.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Adapters.BookingsAdapter;
import ru.pin120.androidjava.Api.BookingsApi;
import ru.pin120.androidjava.Entities.Bookings;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class BookingsList extends AppCompatActivity {
    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Bookings> list;
    public static BookingsAdapter bookingsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookings_list);

        recyclerView = findViewById(R.id.bookingsList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toClientsListBTN = findViewById(R.id.toClientsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toApartamentsBTN = findViewById(R.id.toApartamentsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toTariffsListBTN = findViewById(R.id.toTariffsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toServicesListBTN = findViewById(R.id.toServicesListBTN);
        @SuppressLint("WrongViewCast") ImageButton toFacilitiesListBTN = findViewById(R.id.toFacilitiesListBTN);

        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);
        ImageButton createBTN = findViewById(R.id.addBookingsBTN);

        toClientsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BookingsList.this, ClientsList.class);
                startActivity(intent);
            }
        });

        toFacilitiesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BookingsList.this, FacilitiesList.class);
                startActivity(intent);
            }
        });

        toServicesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BookingsList.this, ServicesList.class);
                startActivity(intent);
            }
        });

        toTariffsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BookingsList.this, TariffsList.class);
                startActivity(intent);
            }
        });

        toApartamentsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(BookingsList.this, ApartamentsList.class);
                startActivity(intent);
            }
        });

        navBar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navBar.getVisibility() == View.INVISIBLE) {
                    navBar.startAnimation(alphaIn);
                    navBar.setVisibility(View.VISIBLE);
                }
                else {
                    navBar.startAnimation(alphaOut);
                    navBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        createBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(BookingsList.this, BookingsCreate.class);
                startActivity(intent);
            }
        });

        loadBookings();
    }

    private void loadBookings() {
        RESTHelper restHelper = new RESTHelper();
        BookingsApi bookingsApi = restHelper.getRetrofit().create(BookingsApi.class);

        bookingsApi.getAllBookings()
                .enqueue(new Callback<List<Bookings>>() {
                    @Override
                    public void onResponse(Call<List<Bookings>> call, Response<List<Bookings>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Bookings>> call, Throwable t) {
                        /*Toast.makeText(BookingsList.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();*/
                        Log.e("APIError", "Ошибка получения данных", t);
                    }
                });
    }

    private void populateListView(List<Bookings> bookingsList) {
        list = bookingsList;
        bookingsAdapter = new BookingsAdapter(bookingsList);
        recyclerView.setAdapter(bookingsAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Bookings deletedBooking = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            BookingsApi bookingsApi = restHelper.getRetrofit().create(BookingsApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedBooking = list.get(position);

                bookingsApi.delete(deletedBooking)
                        .enqueue(new Callback<Bookings>() {
                            @Override
                            public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                                Toast.makeText(BookingsList.this, "Заказ " + deletedBooking.getId() + " удален", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Bookings> call, Throwable t) {
                                Toast.makeText(BookingsList.this, "Не удалось удалить заказ", Toast.LENGTH_SHORT).show();
                            }
                        });

                list.remove(position);
                bookingsAdapter.notifyItemRemoved(position);
            }
            else{
                final int position = viewHolder.getAdapterPosition();
                BookingsEdit.selectedBooking = list.get(position);

                Intent intent = new Intent(BookingsList.this, BookingsEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(BookingsList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(BookingsList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        bookingsAdapter.notifyDataSetChanged();
    }
}