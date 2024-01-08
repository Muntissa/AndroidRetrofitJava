package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.ClientsApi;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ClientsCreate extends AppCompatActivity {
    TextInputEditText secondNameET, firstNameET, lastNameET, emailET, phoneET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_create);

        initializeComponents();
    }

    private void initializeComponents() {
        secondNameET = findViewById(R.id.clientForm_SecondName);
        firstNameET = findViewById(R.id.clientForm_FirstName);
        lastNameET = findViewById(R.id.clientForm_LastName);
        emailET = findViewById(R.id.clientForm_Email);
        phoneET = findViewById(R.id.clientForm_Phone);

        Button saveBTN = findViewById(R.id.clientForm_AddClientBTN);

        Button toListClientsBTN = findViewById(R.id.toListClients);

        RESTHelper restHelper = new RESTHelper();
        ClientsApi clientsApi = restHelper.getRetrofit().create(ClientsApi.class);

        saveBTN.setOnClickListener(view -> {
            String secondName = String.valueOf(secondNameET.getText());
            String firstName = String.valueOf(firstNameET.getText());
            String lastName = String.valueOf(lastNameET.getText());
            String email = String.valueOf(emailET.getText());
            String phone = String.valueOf(phoneET.getText());

            Clients client = new Clients();
            client.setSecondName(secondName);
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setEmail(email);
            client.setPhone(phone);

            if(isValidInput()) {
                clientsApi.save(client)
                        .enqueue(new Callback<Clients>() {
                            @Override
                            public void onResponse(Call<Clients> call, Response<Clients> response) {
                                Toast.makeText(ClientsCreate.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Clients> call, Throwable t) {
                                Toast.makeText(ClientsCreate.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                            }
                        });
            }

        });

        toListClientsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent listActivity = new Intent(ClientsCreate.this, ClientsList.class);
                startActivity(listActivity);
            }
        });
    }

        private boolean isValidInput() {
            String secondName = secondNameET.getText().toString().trim();
            String firstName = firstNameET.getText().toString().trim();
            String lastName = lastNameET.getText().toString().trim();
            String email = emailET.getText().toString().trim();
            String phone = phoneET.getText().toString().trim();

            boolean snBool = false, fnBool = false, lnBool = false, emailBool = false, phoneBool = false;

            if(secondName.isEmpty()) {
                showToastAndAnimation("Введите фамилию", secondNameET);
            } else if (!isValidString(secondName)) {
                showToastAndHighlight("Некорректная фамилия. Допустимы только буквы", secondNameET);
            } else {
                secondNameET.setTextColor((Color.BLACK));
                snBool = true;
            }

            if(firstName.isEmpty()) {
                showToastAndAnimation("Введите имя", firstNameET);
            } else if (!isValidString(firstName)) {
                showToastAndHighlight("Некорректное имя. Допустимы только буквы", firstNameET);
            } else {
                firstNameET.setTextColor((Color.BLACK));
                fnBool = true;
            }

            if(lastName.isEmpty()) {
                showToastAndAnimation("Введите отчество", lastNameET);
            } else if (!isValidString(lastName)) {
                showToastAndHighlight("Некорректное отчество. Допустимы только буквы", lastNameET);
            } else {
                lastNameET.setTextColor((Color.BLACK));
                lnBool = true;
            }

            if(email.isEmpty()) {
                showToastAndAnimation("Введите почту", emailET);
            } else if (!isValidEmail(email)) {
                showToastAndHighlight("Некорректная почта", emailET);
            } else {
                emailET.setTextColor((Color.BLACK));
                emailBool = true;
            }

            if(phone.isEmpty()) {
                showToastAndAnimation("Введите телефон", phoneET);
            } else if (!isValidPhone(phone)) {
                showToastAndHighlight("Некорректный телефон", phoneET);
            } else {
                phoneET.setTextColor((Color.BLACK));
                phoneBool = true;
            }

            if(snBool && fnBool && lnBool && emailBool && phoneBool)
                return true;
            else
                return false;
        }

        private boolean isValidString(String word) {
            String regex = "^[a-zA-Zа-яА-ЯёЁ]+$";
            return word.matches(regex);
        }

        private boolean isValidEmail(String email) {
            String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
            return email.matches(regex);
        }

        private boolean isValidPhone(String phone) {
            String regex = "^\\+7\\d{10}$";
            return phone.matches(regex);
        }

        private void showToastAndHighlight(String message, TextInputEditText object) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            object.setTextColor(Color.RED);
        }
        private void showToastAndAnimation(String message, TextInputEditText object) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            ObjectAnimator animator = ObjectAnimator.ofFloat(object, View.ALPHA, 0.3f);
            animator.setDuration(300);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setRepeatMode(ObjectAnimator.REVERSE);
            animator.setRepeatCount(3);
            animator.start();
        }
}