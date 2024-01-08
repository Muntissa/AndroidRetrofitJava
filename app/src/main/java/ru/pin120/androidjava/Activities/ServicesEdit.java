package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ServicesEdit extends AppCompatActivity {

    public static Services selectedService;
    public Services findedService;
    EditText nameET, descriptionET, priceET;
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
        nameET = findViewById(R.id.servicesEdit_Name);
        descriptionET = findViewById(R.id.servicesEdit_Description);
        priceET = findViewById(R.id.servicesEdit_Price);
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
                        nameET.setText(findedService.getName());
                        descriptionET.setText(findedService.getDescription());
                        priceET.setText(String.valueOf(findedService.getPrice()));
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

        findedService.setName(String.valueOf(nameET.getText()));
        findedService.setDescription(String.valueOf(descriptionET.getText()));

        int price = 0;

        if(!priceET.getText().toString().isEmpty()) {
            price = Integer.parseInt(priceET.getText().toString());
        }
        findedService.setPrice(price);

        if(isValidInput()) {
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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ServicesList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isValidInput() {
        String name = nameET.getText().toString().trim();
        String description = descriptionET.getText().toString().trim();
        String price = priceET.getText().toString().trim();

        boolean nameBool = false, descBool = false, priceBool = false;

        if(name.isEmpty()) {
            showToastAndAnimation("Введите название", nameET);
        } else if (!isValidString(name)) {
            showToastAndHighlight("Некорректное название. Допустимы только буквы и цифры", nameET);
        } else {
            nameET.setTextColor((Color.BLACK));
            nameBool = true;
        }

        if(description.isEmpty()) {
            showToastAndAnimation("Введите описание", descriptionET);
        } else if (!isValidString(description)) {
            showToastAndHighlight("Некорректное описание. Допустимы только буквы и цифры", descriptionET);
        } else {
            descriptionET.setTextColor((Color.BLACK));
            descBool = true;
        }

        if(price.isEmpty()) {
            showToastAndAnimation("Введите цену", priceET);
        } else {
            priceET.setTextColor((Color.BLACK));
            priceBool = true;
        }


        if(nameBool && descBool && priceBool)
            return true;
        else
            return false;
    }

    private boolean isValidString(String word) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ0-9\\s]+$";
        return word.matches(regex);
    }

    private void showToastAndHighlight(String message, EditText object) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        object.setTextColor(Color.RED);
    }
    private void showToastAndAnimation(String message, EditText object) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        ObjectAnimator animator = ObjectAnimator.ofFloat(object, View.ALPHA, 0.3f);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(3);
        animator.start();
    }
}