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
import ru.pin120.androidjava.Adapters.ClientsAdapter;
import ru.pin120.androidjava.Api.ClientsApi;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ClientsList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;
    List<Clients> list;
    public static ClientsAdapter clientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_list);

        recyclerView = findViewById(R.id.clientsList_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation alphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        Animation alphaOut = AnimationUtils.loadAnimation(this, R.anim.alpha_out);

        @SuppressLint("WrongViewCast") ImageButton toApartamentsListBTN = findViewById(R.id.toApartamentsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toBookingsListBTN = findViewById(R.id.toBookingsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toTariffsListBTN = findViewById(R.id.toTariffsListBTN);
        @SuppressLint("WrongViewCast") ImageButton toServicesListBTN = findViewById(R.id.toServicesListBTN);
        @SuppressLint("WrongViewCast") ImageButton toFacilitiesListBTN = findViewById(R.id.toFacilitiesListBTN);

        ImageButton createBTN = findViewById(R.id.addClientBTN);
        ImageButton navBar_btn = findViewById(R.id.navBar_btn);
        CardView navBar = findViewById(R.id.navBar);

        toApartamentsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ClientsList.this, ApartamentsList.class);
                startActivity(intent);
            }
        });

        toFacilitiesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ClientsList.this, FacilitiesList.class);
                startActivity(intent);
            }
        });

        toServicesListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ClientsList.this, ServicesList.class);
                startActivity(intent);
            }
        });

        toTariffsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ClientsList.this, TariffsList.class);
                startActivity(intent);
            }
        });

        toBookingsListBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ClientsList.this, BookingsList.class);
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
                Intent intent = new Intent(ClientsList.this, ClientsCreate.class);
                startActivity(intent);
            }
        });

        loadClients();
    }

    private void loadClients() {
        RESTHelper restHelper = new RESTHelper();
        ClientsApi clientsApi = restHelper.getRetrofit().create(ClientsApi.class);

        clientsApi.getAllClients()
                .enqueue(new Callback<List<Clients>>() {
                    @Override
                    public void onResponse(Call<List<Clients>> call, Response<List<Clients>> response) {
                        populateListView(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Clients>> call, Throwable t) {
                        Toast.makeText(ClientsList.this, "Не удалось загрузить клиентов", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void populateListView(List<Clients> clientsList) {
        list = clientsList;
        clientsAdapter = new ClientsAdapter(clientsList);
        recyclerView.setAdapter(clientsAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Clients deletedClient = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            RESTHelper restHelper = new RESTHelper();
            ClientsApi clientsApi = restHelper.getRetrofit().create(ClientsApi.class);


            if(direction == ItemTouchHelper.LEFT){
                final int position = viewHolder.getAdapterPosition();
                deletedClient = list.get(position);

                clientsApi.delete(deletedClient)
                        .enqueue(new Callback<Clients>() {
                            @Override
                            public void onResponse(Call<Clients> call, Response<Clients> response) {
                                Toast.makeText(ClientsList.this, "Клиент" + deletedClient.getId() + "удален", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Clients> call, Throwable t) {
                                Toast.makeText(ClientsList.this, "Клиент удален", Toast.LENGTH_SHORT).show();
                            }
                        });


                list.remove(position);
                clientsAdapter.notifyItemRemoved(position);

            }
            else{
                final int position = viewHolder.getAdapterPosition();
                ClientsEdit.selectedClient = list.get(position);
                Intent intent = new Intent(ClientsList.this, ClientsEdit.class);
                startActivity(intent);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(ClientsList.this, R.color.delete))
                    .addSwipeLeftActionIcon(R.drawable.baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(ClientsList.this, R.color.edit))
                    .addSwipeRightActionIcon(R.drawable.baseline_border_color_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public static void updateAdapter(){
        clientsAdapter.notifyDataSetChanged();
    }
}