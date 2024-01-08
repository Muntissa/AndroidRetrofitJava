package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
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

    TextInputEditText nameET, descriptionET, priceET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tariffs_create);

        initializeComponents();
    }

    private void initializeComponents() {
        nameET = findViewById(R.id.tariffsForm_Name);
        descriptionET = findViewById(R.id.tariffsForm_Description);
        priceET = findViewById(R.id.tariffsForm_Price);

        Button saveBTN = findViewById(R.id.tariffsForm_AddTariffBTN);

        Button toListTariffsBTN = findViewById(R.id.toListTariffs);

        RESTHelper restHelper = new RESTHelper();
        TariffsApi tariffsApi = restHelper.getRetrofit().create(TariffsApi.class);

        saveBTN.setOnClickListener(view -> {
            String name = String.valueOf(nameET.getText());
            String description = String.valueOf(descriptionET.getText());

            int price = 0;

            if(!priceET.getText().toString().isEmpty()) {
                price = Integer.parseInt(priceET.getText().toString());
            }

            Tariffs tariff = new Tariffs();
            tariff.setName(name);
            tariff.setDescription(description);
            tariff.setPrice(price);

            if(isValidInput()) {
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
            }

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