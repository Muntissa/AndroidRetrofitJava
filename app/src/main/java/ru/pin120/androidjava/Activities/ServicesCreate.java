package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.ClientsApi;
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ServicesCreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_create);

        initializeComponents();
    }

    private void initializeComponents() {
        TextInputEditText nameET = findViewById(R.id.servicesForm_Name);
        TextInputEditText descriptionET = findViewById(R.id.servicesForm_Description);
        TextInputEditText priceET = findViewById(R.id.servicesForm_Price);

        Button saveBTN = findViewById(R.id.servicesForm_AddServiceBTN);

        Button toListServicesBTN = findViewById(R.id.toListServices);

        RESTHelper restHelper = new RESTHelper();
        ServicesApi servicesApi = restHelper.getRetrofit().create(ServicesApi.class);

        saveBTN.setOnClickListener(view -> {
            String name = String.valueOf(nameET.getText());
            String description = String.valueOf(descriptionET.getText());
            int price = Integer.parseInt(priceET.getText().toString());

            Services service = new Services();
            service.setName(name);
            service.setDescription(description);
            service.setPrice(price);

            servicesApi.save(service)
                    .enqueue(new Callback<Services>() {
                        @Override
                        public void onResponse(Call<Services> call, Response<Services> response) {
                            Toast.makeText(ServicesCreate.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Services> call, Throwable t) {
                            Toast.makeText(ServicesCreate.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });

        toListServicesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent listActivity = new Intent(ServicesCreate.this, ServicesList.class);
                startActivity(listActivity);
            }
        });
    }
}