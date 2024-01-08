package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class FacilitiesCreate extends AppCompatActivity {

    TextInputEditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facilities_create);

        initializeComponents();
    }

    private void initializeComponents() {
        name = findViewById(R.id.facilitiesForm_Name);

        Button saveBTN = findViewById(R.id.facilitiesForm_AddFacilityBTN);

        Button toListFacilitiesBTN = findViewById(R.id.toListFacilities);

        RESTHelper restHelper = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelper.getRetrofit().create(FacilitiesApi.class);

        saveBTN.setOnClickListener(view -> {
            String Name = String.valueOf(name.getText());

            Facilities facility = new Facilities();
            facility.setName(Name);

            if(isValidInput()) {
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
            }

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

    private boolean isValidInput() {
        String facilityName = name.getText().toString().trim();

        if(facilityName.isEmpty()) {
            showToastAndAnimation("Введите название");
            return false;
        } else if (!isValid(facilityName)) {
            showToastAndHighlight("Некорректное название. Допустимы только буквы");
            return false;
        } else {
            name.setTextColor((Color.BLACK));
            return true;
        }
    }

    private boolean isValid(String name) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ]+$";
        return name.matches(regex);
    }

    private void showToastAndHighlight(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        name.setTextColor(Color.RED);
    }
    private void showToastAndAnimation(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        ObjectAnimator animator = ObjectAnimator.ofFloat(name, View.ALPHA, 0.3f);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(3);
        animator.start();
    }
}