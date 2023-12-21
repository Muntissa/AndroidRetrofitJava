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
import ru.pin120.androidjava.Api.ClientsApi;
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class FacilitiesCreate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facilities_create);

        initializeComponents();
    }

    private void initializeComponents() {
        TextInputEditText name = findViewById(R.id.facilitiesForm_Name);

        Button saveBTN = findViewById(R.id.facilitiesForm_AddFacilityBTN);

        Button toListFacilitiesBTN = findViewById(R.id.toListFacilities);

        RESTHelper restHelper = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);

        saveBTN.setOnClickListener(view -> {
            String Name = String.valueOf(name.getText());

            Facilities facility = new Facilities();
            facility.setName(Name);

            facilitiesApi.save(facility)
                    .enqueue(new Callback<Facilities>() {
                        @Override
                        public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                            Toast.makeText(FacilitiesCreate.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Facilities> call, Throwable t) {
                            Toast.makeText(FacilitiesCreate.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });

        toListFacilitiesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent listActivity = new Intent(FacilitiesCreate.this, FacilitiesList.class);
                startActivity(listActivity);
            }
        });
    }
}