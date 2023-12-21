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
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Api.TariffsApi;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class TariffsEdit extends AppCompatActivity {

    public static Tariffs selectedTariff;
    public Tariffs findedTariff;
    EditText nameET, descriptionET, priceET;
    Button save_changes, toListTariffsBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariffs_edit);

        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListTariffsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(TariffsEdit.this, TariffsList.class);
                startActivity(intent);
            }
        });
    }
    private void setVars(){
        nameET = findViewById(R.id.tariffsEdit_Name);
        descriptionET = findViewById(R.id.tariffsEdit_Description);
        priceET = findViewById(R.id.tariffsEdit_Price);
        toListTariffsBTN = findViewById(R.id.toListTariffs);
        save_changes = findViewById(R.id.tariffsEdit_saveBTN);
    }
    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);

        tariffsApi.findById(selectedTariff.getId())
                .enqueue(new Callback<Tariffs>() {
                    @Override
                    public void onResponse(Call<Tariffs> call, Response<Tariffs> response) {
                        findedTariff = response.body();
                        nameET.setText(findedTariff.getName());
                        descriptionET.setText(findedTariff.getDescription());
                        priceET.setText(String.valueOf(findedTariff.getPrice()));
                    }

                    @Override
                    public void onFailure(Call<Tariffs> call, Throwable t) {
                        Toast.makeText(TariffsEdit.this, "Не удалось загрузить клиента", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void saveChanges(){
        RESTHelper restHelper = new RESTHelper();
        TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);

        findedTariff.setName(String.valueOf(nameET.getText()));
        findedTariff.setDescription(String.valueOf(descriptionET.getText()));
        findedTariff.setPrice(Integer.valueOf(priceET.getText().toString()));

        tariffsApi.update(findedTariff)
                .enqueue(new Callback<Tariffs>() {
                    @Override
                    public void onResponse(Call<Tariffs> call, Response<Tariffs> response) {
                        Toast.makeText(TariffsEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Tariffs> call, Throwable t) {
                        Toast.makeText(TariffsEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

        ServicesList.updateAdapter(); //Падает, если раскоментить
        finish();
        Intent intent = new Intent(TariffsEdit.this, TariffsList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            TariffsList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }
}