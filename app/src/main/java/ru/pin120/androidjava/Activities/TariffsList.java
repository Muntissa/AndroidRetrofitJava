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
import ru.pin120.androidjava.Adapters.TariffsAdapter;
import ru.pin120.androidjava.Api.TariffsApi;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class TariffsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Tariffs> list;
    public static TariffsAdapter tariffsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariffs_list);

        recyclerView = findViewById(R.id.tariffsList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toClientsListBTN = findViewById(R.id.toClientsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toBookingsListBTN = findViewById(R.id.toBookingsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toApartamentsListBTN = findViewById(R.id.toApartamentsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toServicesListBTN = findViewById(R.id.toServicesListBTN);
        @SuppressLint("WrongViewCast") ImageButton toFacilitiesListBTN = findViewById(R.id.toFacilitiesListBTN);


        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);
        ImageButton createBTN = findViewById(R.id.addTariffsBTN);

        toClientsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TariffsList.this, ClientsList.class);
                startActivity(intent);
            }
        });

        toFacilitiesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TariffsList.this, FacilitiesList.class);
                startActivity(intent);
            }
        });

        toServicesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TariffsList.this, ServicesList.class);
                startActivity(intent);
            }
        });

        toBookingsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TariffsList.this, BookingsList.class);
                startActivity(intent);
            }
        });

        toApartamentsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(TariffsList.this, ApartamentsList.class);
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
                Intent intent = new Intent(TariffsList.this, TariffsCreate.class);
                startActivity(intent);
            }
        });

        loadTariffs();
    }

    private void loadTariffs() {
        RESTHelper restHelper = new RESTHelper();
        TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);

        tariffsApi.getAllTariffs()
                .enqueue(new Callback<List<Tariffs>>() {
                    @Override
                    public void onResponse(Call<List<Tariffs>> call, Response<List<Tariffs>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Tariffs>> call, Throwable t) {
                        Toast.makeText(TariffsList.this, "Тариф удален", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Tariffs> tariffsList) {
        list = tariffsList;
        tariffsAdapter = new TariffsAdapter(tariffsList);
        recyclerView.setAdapter(tariffsAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    Tariffs deletedTariff = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedTariff = list.get(position);

                tariffsApi.delete(deletedTariff)
                        .enqueue(new Callback<Tariffs>() {
                            @Override
                            public void onResponse(Call<Tariffs> call, Response<Tariffs> response) {
                                Toast.makeText(TariffsList.this, "Тариф" + deletedTariff.getId() + "удален", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Tariffs> call, Throwable t) {
                                Toast.makeText(TariffsList.this, "Не удалось удалить тариф", Toast.LENGTH_SHORT).show();
                            }
                        });

                list.remove(position);
                tariffsAdapter.notifyItemRemoved(position);
            }
            else{
                final int position = viewHolder.getAdapterPosition();
                TariffsEdit.selectedTariff = list.get(position);

                Intent intent = new Intent(TariffsList.this, TariffsEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(TariffsList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(TariffsList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        tariffsAdapter.notifyDataSetChanged();
    }
}