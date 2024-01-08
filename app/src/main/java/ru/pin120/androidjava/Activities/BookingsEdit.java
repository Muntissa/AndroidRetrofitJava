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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.pin120.androidjava.Api.ApartamentsApi;
import ru.pin120.androidjava.Api.BookingsApi;
import ru.pin120.androidjava.Api.ClientsApi;
import ru.pin120.androidjava.Api.FacilitiesApi;
import ru.pin120.androidjava.Api.ServicesApi;
import ru.pin120.androidjava.Api.TariffsApi;
import ru.pin120.androidjava.Entities.Apartaments;
import ru.pin120.androidjava.Entities.Bookings;
import ru.pin120.androidjava.Entities.Clients;
import ru.pin120.androidjava.Entities.Facilities;
import ru.pin120.androidjava.Entities.Services;
import ru.pin120.androidjava.Entities.Tariffs;
import ru.pin120.androidjava.R;
import ru.pin120.androidjava.REST.RESTHelper;

public class BookingsEdit extends AppCompatActivity {

    public static Bookings selectedBooking;
    public Bookings findedBookings;
    List<Services> selectedServices;
    private List<Services> servicesList;
    private List<Apartaments> apartamentsList;
    private List<Clients> clientsList;
    Button save_changes, toListBookings;

    private LinearLayout linearLayoutServices;
    private DatePicker startDatePicker, endDatePicker;
    private Spinner clientsSpinner, apartamentsSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookings_edit);

        selectedServices = new ArrayList<>();

        setVars();
        setFields();

        endDatePicker.init(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth(), (view, year, monthOfYear, dayOfMonth) -> {
            // Проверка, чтобы endDatePicker не был раньше startDatePicker
            if (endDatePicker.getYear() < startDatePicker.getYear() ||
                    (endDatePicker.getYear() == startDatePicker.getYear() && endDatePicker.getMonth() < startDatePicker.getMonth()) ||
                    (endDatePicker.getYear() == startDatePicker.getYear() && endDatePicker.getMonth() == startDatePicker.getMonth() && endDatePicker.getDayOfMonth() < startDatePicker.getDayOfMonth())) {

                // Установка endDatePicker на ту же дату, что и startDatePicker
                endDatePicker.updateDate(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth() + 1);
            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        toListBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(BookingsEdit.this, BookingsList.class);
                startActivity(intent);
            }
        });
    }

    private void setVars(){
        linearLayoutServices = findViewById(R.id.linearLayoutServices);

        startDatePicker = findViewById(R.id.datePickerStartDate);
        endDatePicker = findViewById(R.id.datePickerEndDate);



        clientsSpinner = findViewById(R.id.spinnerClients);
        apartamentsSpinner = findViewById(R.id.spinnerApartaments);

        toListBookings = findViewById(R.id.toListBookings);
        save_changes = findViewById(R.id.bookingsEdit_saveBTN);
    }

    private void setFields(){
        RESTHelper restHelper = new RESTHelper();
        BookingsApi bookingsApi = restHelper.getRetrofit().create(BookingsApi.class);

        bookingsApi.findById(selectedBooking.getId())
                .enqueue(new Callback<Bookings>() {
                    @Override
                    public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                        findedBookings = response.body();
                        startDatePicker.updateDate(LocalDate.parse(findedBookings.getStartTime()).getYear(),
                                LocalDate.parse(findedBookings.getStartTime()).getMonthValue() - 1,
                                LocalDate.parse(findedBookings.getStartTime()).getDayOfMonth());
                        endDatePicker.updateDate(LocalDate.parse(findedBookings.getEndTime()).getYear(),
                                LocalDate.parse(findedBookings.getEndTime()).getMonthValue() - 1,
                                LocalDate.parse(findedBookings.getEndTime()).getDayOfMonth());
                    }
                    @Override
                    public void onFailure(Call<Bookings> call, Throwable t) {
                        Toast.makeText(BookingsEdit.this, "Не удалось загрузить апартаменты", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BookingsEdit.this, "Не удалось загрузить услуги", Toast.LENGTH_SHORT).show();
                    }
                });

        RESTHelper restHelperClients = new RESTHelper();
        ClientsApi clientsApi = restHelperClients.getRetrofit().create(ClientsApi.class);

        clientsApi.getAllClients()
                .enqueue(new Callback<List<Clients>>() {
                    @Override
                    public void onResponse(Call<List<Clients>> call, Response<List<Clients>> response) {
                        clientsList = response.body();
                    }

                    @Override
                    public void onFailure(Call<List<Clients>> call, Throwable t) {
                        Toast.makeText(BookingsEdit.this, "Не удалось загрузить клиентов", Toast.LENGTH_SHORT).show();
                    }
                });

        RESTHelper restHelperApartaments = new RESTHelper();
        ApartamentsApi apartamentsApi = restHelperApartaments.getRetrofit().create(ApartamentsApi.class);

        apartamentsApi.getAllApartaments()
                .enqueue(new Callback<List<Apartaments>>() {
                    @Override
                    public void onResponse(Call<List<Apartaments>> call, Response<List<Apartaments>> response) {
                        apartamentsList = response.body();
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
                    public void onFailure(Call<List<Apartaments>> call, Throwable t) {
                        Toast.makeText(BookingsEdit.this, "Не удалось загрузить апартаменты", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initializeSpinners() {
        apartamentsSpinner = findViewById(R.id.spinnerApartaments);
        ArrayAdapter<Apartaments> apartamentsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, apartamentsList);
        apartamentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        apartamentsSpinner.setAdapter(apartamentsAdapter);

        clientsSpinner = findViewById(R.id.spinnerClients);
        ArrayAdapter<Clients> clientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientsList);
        clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientsSpinner.setAdapter(clientsAdapter);

        apartamentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Apartaments selectedApartament = (Apartaments) parentView.getSelectedItem();
                findedBookings.setApartament(selectedApartament);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не выбрано
            }
        });

        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Clients selectedClient = (Clients) parentView.getSelectedItem();
                findedBookings.setClient(selectedClient);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не выбрано
            }
        });
    }

    private void initializeCheckbox() {

        // Создаем CheckBox для Services
        for (Services service : servicesList) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(service.getName());

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
        BookingsApi bookingsApi = restHelper.getRetrofit().create(BookingsApi.class);

        findedBookings.setStartTime((startDatePicker.getYear()) + "-" + (startDatePicker.getMonth() / 10 == 0 ? "0" + (startDatePicker.getMonth() + 1) : (startDatePicker.getMonth() + 1)) + "-" + (startDatePicker.getDayOfMonth() / 10 == 0 ? "0" + startDatePicker.getDayOfMonth() : startDatePicker.getDayOfMonth()));
        findedBookings.setEndTime((endDatePicker.getYear()) + "-" + (endDatePicker.getMonth() / 10 == 0 ? "0" + (endDatePicker.getMonth() + 1): (endDatePicker.getMonth() + 1)) + "-" + (endDatePicker.getDayOfMonth() / 10 == 0 ? "0" + endDatePicker.getDayOfMonth() : endDatePicker.getDayOfMonth()));
        findedBookings.setServices(selectedServices);

        bookingsApi.update(findedBookings)
                .enqueue(new Callback<Bookings>() {
                    @Override
                    public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                        Toast.makeText(BookingsEdit.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Bookings> call, Throwable t) {
                        Toast.makeText(BookingsEdit.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

        BookingsList.updateAdapter();
        finish();
        Intent intent = new Intent(BookingsEdit.this, BookingsList.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            BookingsList.updateAdapter();
        }
        return super.onKeyDown(keyCode, event);
    }
}