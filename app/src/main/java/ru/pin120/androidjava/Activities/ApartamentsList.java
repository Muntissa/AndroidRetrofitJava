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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Adapters.ApartamentsAdapter;
import ru.pin120.androidjava.Api.ApartamentsApi;
import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ApartamentsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Apartaments> list;
    public static ApartamentsAdapter apartamentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartaments_list);

        recyclerView = findViewById(R.id.apartamentsList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toClientsListBTN = findViewById(R.id.toClientsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toBookingsListBTN = findViewById(R.id.toBookingsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toTariffsListBTN = findViewById(R.id.toTariffsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toServicesListBTN = findViewById(R.id.toServicesListBTN);
        @SuppressLint("WrongViewCast") ImageButton toFacilitiesListBTN = findViewById(R.id.toFacilitiesListBTN);

        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);
        ImageButton createBTN = findViewById(R.id.addApartamentBTN);

        toClientsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ApartamentsList.this, ClientsList.class);
                startActivity(intent);
            }
        });

        toFacilitiesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ApartamentsList.this, FacilitiesList.class);
                startActivity(intent);
            }
        });

        toServicesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ApartamentsList.this, ServicesList.class);
                startActivity(intent);
            }
        });

        toTariffsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ApartamentsList.this, TariffsList.class);
                startActivity(intent);
            }
        });

        toBookingsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ApartamentsList.this, BookingsList.class);
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
                Intent intent = new Intent(ApartamentsList.this, ApartamentsCreate.class);
                startActivity(intent);
            }
        });

        loadApartaments();
    }

    private void loadApartaments() {
        RESTHelper restHelper = new RESTHelper();
        ApartamentsApi apartamentsApi = restHelper.getRetrofit().create(ApartamentsApi.class);

        apartamentsApi.getAllApartaments()
                .enqueue(new Callback<List<Apartaments>>() {
                    @Override
                    public void onResponse(Call<List<Apartaments>> call, Response<List<Apartaments>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Apartaments>> call, Throwable t) {
                        Toast.makeText(ApartamentsList.this, "Не удалось загрузить апартаменты", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Apartaments> apartamentsList) {
        list = apartamentsList;
        apartamentsAdapter = new ApartamentsAdapter(apartamentsList);
        recyclerView.setAdapter(apartamentsAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    Apartaments deletedApartament = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            ApartamentsApi apartamentsApi = restHelper.getRetrofit().create(ApartamentsApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedApartament = list.get(position);

                apartamentsApi.delete(deletedApartament)
                        .enqueue(new Callback<Apartaments>() {
                            @Override
                            public void onResponse(Call<Apartaments> call, Response<Apartaments> response) {
                                Toast.makeText(ApartamentsList.this, "Апартаменты" + deletedApartament.getId() + "удалены", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Apartaments> call, Throwable t) {
                                Toast.makeText(ApartamentsList.this, "Апартаменты удалены", Toast.LENGTH_SHORT).show();
                            }
                        });

                list.remove(position);
                apartamentsAdapter.notifyItemRemoved(position);
            }
            else{
                final int position = viewHolder.getAdapterPosition();
                ApartamentsEdit.selectedApartament = list.get(position);

                Intent intent = new Intent(ApartamentsList.this, ApartamentsEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ApartamentsList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ApartamentsList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        apartamentsAdapter.notifyDataSetChanged();
    }
}