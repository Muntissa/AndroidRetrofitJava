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
import ru.pin120.androidjava.Adapters.FacilitiesAdapter;
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class FacilitiesList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Facilities> list;
    public static FacilitiesAdapter facilitiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facilities_list);

        recyclerView = findViewById(R.id.facilitiesList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toClientsListBTN = findViewById(R.id.toClientsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toBookingsListBTN = findViewById(R.id.toBookingsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toApartamentsListBTN = findViewById(R.id.toApartamentsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toTariffsBTN = findViewById(R.id.toTariffsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toServicesBTN = findViewById(R.id.toServicesListBTN);

        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);
        ImageButton createBTN = findViewById(R.id.addFacilitiesBTN);

        toClientsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(FacilitiesList.this, ClientsList.class);
                startActivity(intent);
            }
        });

        toServicesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(FacilitiesList.this, ServicesList.class);
                startActivity(intent);
            }
        });

        toTariffsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(FacilitiesList.this, TariffsList.class);
                startActivity(intent);
            }
        });

        toBookingsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(FacilitiesList.this, BookingsList.class);
                startActivity(intent);
            }
        });

        toApartamentsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(FacilitiesList.this, ApartamentsList.class);
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
                Intent intent = new Intent(FacilitiesList.this, FacilitiesCreate.class);
                startActivity(intent);
            }
        });

        loadFacilities();
    }

    private void loadFacilities() {
        RESTHelper restHelper = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);

        facilitiesApi.getAllFacilities()
                .enqueue(new Callback<List<Facilities>>() {
                    @Override
                    public void onResponse(Call<List<Facilities>> call, Response<List<Facilities>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Facilities>> call, Throwable t) {
                        Toast.makeText(FacilitiesList.this, "Не удалось загрузить услуги", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Facilities> facilitiesList) {
        list = facilitiesList;
        facilitiesAdapter = new FacilitiesAdapter(facilitiesList);
        recyclerView.setAdapter(facilitiesAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Facilities deletedFacility = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedFacility = list.get(position);

                facilitiesApi.delete(deletedFacility)
                        .enqueue(new Callback<Facilities>() {
                            @Override
                            public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                                Toast.makeText(FacilitiesList.this, "Удобство" + deletedFacility.getId() + "удалено", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Facilities> call, Throwable t) {
                                Toast.makeText(FacilitiesList.this, "Не удалось удалить услугу", Toast.LENGTH_SHORT).show();
                            }
                        });

                list.remove(position);
                facilitiesAdapter.notifyItemRemoved(position);
            }
            else{
                final int position = viewHolder.getAdapterPosition();
                FacilitiesEdit.selectedFacility = list.get(position);

                Intent intent = new Intent(FacilitiesList.this, FacilitiesEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(FacilitiesList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(FacilitiesList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        facilitiesAdapter.notifyDataSetChanged();
    }
}