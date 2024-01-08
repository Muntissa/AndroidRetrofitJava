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
import ru.pin120.androidjava.Adapters.ServicesAdapter;
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ServicesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Services> list;
    public static ServicesAdapter servicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_list);

        recyclerView = findViewById(R.id.servicesList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toClientsListBTN = findViewById(R.id.toClientsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toBookingsListBTN = findViewById(R.id.toBookingsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toApartamentsListBTN = findViewById(R.id.toApartamentsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toTariffsBTN = findViewById(R.id.toTariffsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toFacilitiesListBTN = findViewById(R.id.toFacilitiesListBTN);


        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);
        ImageButton createBTN = findViewById(R.id.addServicesBTN);

        toClientsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ServicesList.this, ClientsList.class);
                startActivity(intent);
            }
        });

        toFacilitiesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ServicesList.this, FacilitiesList.class);
                startActivity(intent);
            }
        });

        toTariffsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ServicesList.this, TariffsList.class);
                startActivity(intent);
            }
        });

        toBookingsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ServicesList.this, BookingsList.class);
                startActivity(intent);
            }
        });

        toApartamentsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ServicesList.this, ApartamentsList.class);
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
                Intent intent = new Intent(ServicesList.this, ServicesCreate.class);
                startActivity(intent);
            }
        });

        loadServices();
    }

    private void loadServices() {
        RESTHelper restHelper = new RESTHelper();
        ServicesApi servicesApi = restHelper.getRetrofit().create(ServicesApi.class);

        servicesApi.getAllServices()
                .enqueue(new Callback<List<Services>>() {
                    @Override
                    public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Services>> call, Throwable t) {
                        Toast.makeText(ServicesList.this, "Услуга удалена", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Services> servicesList) {
        list = servicesList;
        servicesAdapter = new ServicesAdapter(servicesList);
        recyclerView.setAdapter(servicesAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Services deletedService = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            ServicesApi servicesApi = restHelper.getRetrofit().create(ServicesApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedService = list.get(position);

                servicesApi.delete(deletedService)
                        .enqueue(new Callback<Services>() {
                            @Override
                            public void onResponse(Call<Services> call, Response<Services> response) {
                                Toast.makeText(ServicesList.this, "Услуга" + deletedService.getId() + "удалена", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Services> call, Throwable t) {
                                Toast.makeText(ServicesList.this, "Не удалось удалить услугу", Toast.LENGTH_SHORT).show();
                            }
                        });

                list.remove(position);
                servicesAdapter.notifyItemRemoved(position);
            }
            else{
                final int position = viewHolder.getAdapterPosition();
                ServicesEdit.selectedService = list.get(position);

                Intent intent = new Intent(ServicesList.this, ServicesEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ServicesList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ServicesList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        servicesAdapter.notifyDataSetChanged();
    }
}