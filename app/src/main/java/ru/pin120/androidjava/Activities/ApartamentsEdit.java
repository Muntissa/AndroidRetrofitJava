package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.ApartamentsApi;
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Api.TariffsApi;
import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class ApartamentsEdit extends AppCompatActivity {

    public static Apartaments selectedApartament;
    public Apartaments findedApartament;
    List<Facilities> selectedFacilities;
    List<Services> selectedServices;
    private List<Facilities> facilitiesList;
    private List<Services> servicesList;
    private List<Tariffs> tariffsList;
    Button save_changes, toListApartaments;

    private LinearLayout linearLayoutFacilities;
    private LinearLayout linearLayoutServices;
    private EditText numberEditText, areaEditText /*photoPathEditText*/;
    /*private CheckBox reservationCheckBox;*/
    private Spinner tariffsSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartaments_edit);

        selectedFacilities = new ArrayList<>();
        selectedServices = new ArrayList<>();

        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListApartaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ApartamentsEdit.this, ApartamentsList.class);
                startActivity(intent);
            }
        });
    }

    private void setVars(){
        linearLayoutFacilities = findViewById(R.id.linearLayoutFacilities);
        linearLayoutServices = findViewById(R.id.linearLayoutServices);

        numberEditText = findViewById(R.id.editTextNumber);
        areaEditText = findViewById(R.id.editTextArea);
        /*photoPathEditText = findViewById(R.id.editTextPhotoPath);*/
        /*reservationCheckBox = findViewById(R.id.checkBoxReservation);*/
        tariffsSpinner = findViewById(R.id.spinnerTariffs);
        toListApartaments = findViewById(R.id.toListApartaments);
        save_changes = findViewById(R.id.apartamentsEdit_saveBTN);
    }

    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        ApartamentsApi apartamentsApi = restHelper.getRetrofit().create(ApartamentsApi.class);

        apartamentsApi.findById(selectedApartament.getId())
                .enqueue(new Callback<Apartaments>() {
                    @Override
                    public void onResponse(Call<Apartaments> call, Response<Apartaments> response) {
                        findedApartament = response.body();
                        numberEditText.setText(String.valueOf(findedApartament.getNumber()));
                        areaEditText.setText(String.valueOf(findedApartament.getArea()));
                        /*photoPathEditText.setText(findedApartament.getPhotoPath());*/
                        /*reservationCheckBox.setChecked(findedApartament.getReservation());*/
                    }
                    @Override
                    public void onFailure(Call<Apartaments> call, Throwable t) {
                        Toast.makeText(ApartamentsEdit.this, "Не удалось загрузить апартаменты", Toast.LENGTH_SHORT).show();
                    }
                });

        RESTHelper restHelperFacilities = new RESTHelper();
        FacilitiesApi facilitiesApi = restHelperFacilities.getRetrofit().create(FacilitiesApi.class);

        facilitiesApi.getAllFacilities()
                .enqueue(new Callback<List<Facilities>>() {
                    @Override
                    public void onResponse(Call<List<Facilities>> call, Response<List<Facilities>> response) {
                        facilitiesList = response.body();
                    }

                    @Override
                    public void onFailure(Call<List<Facilities>> call, Throwable t) {
                        Toast.makeText(ApartamentsEdit.this, "Не удалось загрузить услуги", Toast.LENGTH_SHORT).show();
                    }
                });

        RESTHelper restHelperServices = new RESTHelper();
        ServicesApi servicesApi = restHelperServices.getRetrofit().create(ServicesApi.class);

        servicesApi.getAllServices()
                .enqueue(new Callback<List<Services>>() {
                    @Override
                    public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                        servicesList = response.body();

                    }

                    @Override
                    public void onFailure(Call<List<Services>> call, Throwable t) {
                        Toast.makeText(ApartamentsEdit.this, "Не удалось загрузить услуги", Toast.LENGTH_SHORT).show();
                    }
                });

        RESTHelper restHelperTariffs = new RESTHelper();
        TariffsApi tariffsApi = restHelperTariffs.getRetrofit().create(TariffsApi.class);

        tariffsApi.getAllTariffs()
                .enqueue(new Callback<List<Tariffs>>() {
                    @Override
                    public void onResponse(Call<List<Tariffs>> call, Response<List<Tariffs>> response) {
                        tariffsList = response.body();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initializeSpinners();
                            }
                        }, 2000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initializeCheckbox();
                            }
                        }, 2000);
                    }

                    @Override
                    public void onFailure(Call<List<Tariffs>> call, Throwable t) {
                        Toast.makeText(ApartamentsEdit.this, "Не удалось загрузить тарифы", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeSpinners() {
        // Инициализация для Tariffs
        tariffsSpinner = findViewById(R.id.spinnerTariffs);
        ArrayAdapter<Tariffs> tariffsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tariffsList);
        tariffsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tariffsSpinner.setAdapter(tariffsAdapter);


        // Обработчик выбора элемента для Tariffs
        tariffsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Tariffs selectedTariff = (Tariffs) parentView.getSelectedItem();
                findedApartament.setTariff(selectedTariff);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не выбрано
            }
        });
    }

    private void initializeCheckbox() {
        for (Facilities facility : facilitiesList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(facility.getName());
            checkBox.setChecked(findedApartament.getFacilities().contains(facility));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedFacilities.add(facility);
                    } else {
                        selectedFacilities.remove(facility);
                    }
                }
            });

            linearLayoutFacilities.addView(checkBox);
        }

        // Создаем CheckBox для Services
        for (Services service : servicesList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(service.getName());
            checkBox.setChecked(findedApartament.getServices().contains(service));

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedServices.add(service);
                    } else {
                        selectedServices.remove(service);
                    }
                }
            });

            linearLayoutServices.addView(checkBox);
        }
    }

    private void saveChanges(){
        RESTHelper restHelper = new RESTHelper();
        ApartamentsApi apartamentsApi = restHelper.getRetrofit().create(ApartamentsApi.class);

        findedApartament.setNumber(Integer.valueOf(numberEditText.getText().toString()));
        findedApartament.setArea(Integer.valueOf(areaEditText.getText().toString()));
        /*findedApartament.setPhotoPath(String.valueOf(photoPathEditText.getText()));*/
        /*findedApartament.setReservation(reservationCheckBox.isChecked());*/
        findedApartament.setFacilities(selectedFacilities);
        findedApartament.setServices(selectedServices);

        apartamentsApi.update(findedApartament)
                .enqueue(new Callback<Apartaments>() {
                    @Override
                    public void onResponse(Call<Apartaments> call, Response<Apartaments> response) {
                        Toast.makeText(ApartamentsEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Apartaments> call, Throwable t) {
                        Toast.makeText(ApartamentsEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

        ApartamentsList.updateAdapter();
        finish();
        Intent intent = new Intent(ApartamentsEdit.this, ApartamentsList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            ApartamentsList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }


}