package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

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
    EditText fName, sName, lName, email, phone;
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
        fName = findViewById(R.id.clientsEdit_firstName);
        sName = findViewById(R.id.clientsEdit_secondName);
        lName = findViewById(R.id.clientsEdit_lastName);
        email = findViewById(R.id.clientsEdit_email);
        phone = findViewById(R.id.clientsEdit_phone);
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
                        fName.setText(findedClient.getFirstName());
                        sName.setText(findedClient.getSecondName());
                        lName.setText(findedClient.getLastName());
                        email.setText(findedClient.getEmail());
                        phone.setText(findedClient.getPhone());
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

        findedClient.setFirstName(String.valueOf(fName.getText()));
        findedClient.setSecondName(String.valueOf(sName.getText()));
        findedClient.setLastName(String.valueOf(lName.getText()));
        findedClient.setPhone(String.valueOf(phone.getText()));
        findedClient.setEmail(String.valueOf(email.getText()));

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ClientsList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }
}