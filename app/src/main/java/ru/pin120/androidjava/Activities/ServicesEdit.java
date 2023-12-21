package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ServicesEdit extends AppCompatActivity {

    public static Services selectedService;
    public Services findedService;
    EditText name, description, price;
    Button save_changes, toListServicesBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_edit);

        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListServicesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ServicesEdit.this, ServicesList.class);
                startActivity(intent);
            }
        });
    }
    private void setVars(){
        name = findViewById(R.id.servicesEdit_Name);
        description = findViewById(R.id.servicesEdit_Description);
        price = findViewById(R.id.servicesEdit_Price);
        toListServicesBTN = findViewById(R.id.toListServices);
        save_changes = findViewById(R.id.servicesEdit_saveBTN);
    }
    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        ServicesApi servicesApi = restHelper.getRetrofit().create(ServicesApi.class);

        servicesApi.findById(selectedService.getId())
                .enqueue(new Callback<Services>() {
                    @Override
                    public void onResponse(Call<Services> call, Response<Services> response) {
                        findedService = response.body();
                        name.setText(findedService.getName());
                        description.setText(findedService.getDescription());
                        price.setText(String.valueOf(findedService.getPrice()));
                    }

                    @Override
                    public void onFailure(Call<Services> call, Throwable t) {
                        Toast.makeText(ServicesEdit.this, "Не удалось загрузить клиента", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void saveChanges(){
        RESTHelper restHelper = new RESTHelper();
        ServicesApi servicesApi = restHelper.getRetrofit().create(ServicesApi.class);

        findedService.setName(String.valueOf(name.getText()));
        findedService.setDescription(String.valueOf(description.getText()));
        findedService.setPrice(Integer.valueOf(price.getText().toString()));

        servicesApi.update(findedService)
                .enqueue(new Callback<Services>() {
                    @Override
                    public void onResponse(Call<Services> call, Response<Services> response) {
                        Toast.makeText(ServicesEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Services> call, Throwable t) {
                        Toast.makeText(ServicesEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

        ServicesList.updateAdapter(); //Падает, если раскоментить
        finish();
        Intent intent = new Intent(ServicesEdit.this, ServicesList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ServicesList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }
}