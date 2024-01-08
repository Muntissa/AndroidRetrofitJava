package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

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

public class ClientsEdit extends AppCompatActivity {
    public static Clients selectedClient;
    public Clients findedClient;
    EditText firstNameET, secondNameET, lastNameET, emailET, phoneET;
    Button save_changes, toListClientsBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clients_edit);

        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListClientsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ClientsEdit.this, ClientsList.class);
                startActivity(intent);
            }
        });
    }
    private void setVars(){
        firstNameET = findViewById(R.id.clientsEdit_firstName);
        secondNameET = findViewById(R.id.clientsEdit_secondName);
        lastNameET = findViewById(R.id.clientsEdit_lastName);
        emailET = findViewById(R.id.clientsEdit_email);
        phoneET = findViewById(R.id.clientsEdit_phone);
        toListClientsBTN = findViewById(R.id.toListClients);
        save_changes = findViewById(R.id.clientsEdit_saveBTN);
    }
    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        ClientsApi clientsApi = restHelper.getRetrofit().create(ClientsApi.class);

        clientsApi.findById(selectedClient.getId())
                .enqueue(new Callback<Clients>() {
                    @Override
                    public void onResponse(Call<Clients> call, Response<Clients> response) {
                        findedClient = response.body();
                        firstNameET.setText(findedClient.getFirstName());
                        secondNameET.setText(findedClient.getSecondName());
                        lastNameET.setText(findedClient.getLastName());
                        emailET.setText(findedClient.getEmail());
                        phoneET.setText(findedClient.getPhone());
                    }

                    @Override
                    public void onFailure(Call<Clients> call, Throwable t) {
                        Toast.makeText(ClientsEdit.this, "Не удалось загрузить клиента", Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void saveChanges(){
        RESTHelper restHelper = new RESTHelper();
        ClientsApi clientsApi = restHelper.getRetrofit().create(ClientsApi.class);

        findedClient.setFirstName(String.valueOf(firstNameET.getText()));
        findedClient.setSecondName(String.valueOf(secondNameET.getText()));
        findedClient.setLastName(String.valueOf(lastNameET.getText()));
        findedClient.setPhone(String.valueOf(phoneET.getText()));
        findedClient.setEmail(String.valueOf(emailET.getText()));

        if(isValidInput()) {
            clientsApi.update(findedClient)
                    .enqueue(new Callback<Clients>() {
                        @Override
                        public void onResponse(Call<Clients> call, Response<Clients> response) {
                            Toast.makeText(ClientsEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Clients> call, Throwable t) {
                            Toast.makeText(ClientsEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });

            ClientsList.updateAdapter(); //Падает, если раскоментить
            finish();
            Intent intent = new Intent(ClientsEdit.this, ClientsList.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ClientsList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
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