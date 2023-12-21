package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Api.TariffsApi;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class TariffsCreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariffs_create);

        initializeComponents();
    }

    private void initializeComponents() {
        TextInputEditText nameET = findViewById(R.id.tariffsForm_Name);
        TextInputEditText descriptionET = findViewById(R.id.tariffsForm_Description);
        TextInputEditText priceET = findViewById(R.id.tariffsForm_Price);

        Button saveBTN = findViewById(R.id.tariffsForm_AddTariffBTN);

        Button toListTariffsBTN = findViewById(R.id.toListTariffs);

        RESTHelper restHelper = new RESTHelper();
        TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);

        saveBTN.setOnClickListener(view -> {
            String name = String.valueOf(nameET.getText());
            String description = String.valueOf(descriptionET.getText());
            int price = Integer.parseInt(priceET.getText().toString());

            Tariffs tariff = new Tariffs();
            tariff.setName(name);
            tariff.setDescription(description);
            tariff.setPrice(price);

            tariffsApi.save(tariff)
                    .enqueue(new Callback<Tariffs>() {
                        @Override
                        public void onResponse(Call<Tariffs> call, Response<Tariffs> response) {
                            Toast.makeText(TariffsCreate.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Tariffs> call, Throwable t) {
                            Toast.makeText(TariffsCreate.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });

        toListTariffsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent listActivity = new Intent(TariffsCreate.this, TariffsList.class);
                startActivity(listActivity);
            }
        });
    }
}