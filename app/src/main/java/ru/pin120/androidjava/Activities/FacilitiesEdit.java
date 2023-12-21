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
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class FacilitiesEdit extends AppCompatActivity {

    public static Facilities selectedFacility;
    public Facilities findedFacility;
    EditText name;
    Button save_changes, toListFacilitiesBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facilities_edit);

        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListFacilitiesBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(FacilitiesEdit.this, ClientsList.class);
                startActivity(intent);
            }
        });
    }
    private void setVars(){
        name = findViewById(R.id.facilitiesEdit_Name);
        toListFacilitiesBTN = findViewById(R.id.toListFacilities);
        save_changes = findViewById(R.id.facilitiesEdit_saveBTN);
    }
    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);

        facilitiesApi.findById(selectedFacility.getId())
                .enqueue(new Callback<Facilities>() {
                    @Override
                    public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                        findedFacility = response.body();
                        name.setText(findedFacility.getName());
                    }

                    @Override
                    public void onFailure(Call<Facilities> call, Throwable t) {
                        Toast.makeText(FacilitiesEdit.this, "Не удалось обновить удобство", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void saveChanges(){
        RESTHelper restHelper = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);

        findedFacility.setName(String.valueOf(name.getText()));

        facilitiesApi.update(findedFacility)
                .enqueue(new Callback<Facilities>() {
                    @Override
                    public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                        Toast.makeText(FacilitiesEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Facilities> call, Throwable t) {
                        Toast.makeText(FacilitiesEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

        FacilitiesList.updateAdapter(); //Падает, если раскоментить
        finish();
        Intent intent = new Intent(FacilitiesEdit.this, FacilitiesList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            FacilitiesList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }
}