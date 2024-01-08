package ru.pin120.androidjava.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Calendar;
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

public class BookingsCreate extends AppCompatActivity {
    Bookings newBooking;
    List<Services> selectedServices;
    Apartaments selectedApartament;
    Clients selectedClient;
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
        setContentView(R.layout.bookings_create);

        selectedServices = new ArrayList<>();
        newBooking = new Bookings();
        setVars();
        setFields();

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNew();
            }
        });

        toListBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(BookingsCreate.this, BookingsList.class);
                startActivity(intent);
            }
        });
    }

    private void setVars(){
        linearLayoutServices = findViewById(R.id.linearLayoutServices);

        startDatePicker = findViewById(R.id.datePickerStartDate);
        endDatePicker = findViewById(R.id.datePickerEndDate);

        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        startDatePicker.init(startYear, startMonth, startDay, (view, year, monthOfYear, dayOfMonth) -> {
            // Проверка, чтобы endDatePicker был не раньше startDatePicker
            if (endDatePicker.getYear() < year ||
                    (endDatePicker.getYear() == year && endDatePicker.getMonth() < monthOfYear) ||
                    (endDatePicker.getYear() == year && endDatePicker.getMonth() == monthOfYear && endDatePicker.getDayOfMonth() < dayOfMonth)) {

                // Установка endDatePicker на ту же дату, что и startDatePicker
                endDatePicker.updateDate(year, monthOfYear, dayOfMonth);
            }
        });

        int endYear = calendar.get(Calendar.YEAR);
        int endMonth = calendar.get(Calendar.MONTH);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH) + 1;

        endDatePicker.init(endYear, endMonth, endDay, (view, year, monthOfYear, dayOfMonth) -> {
            // Проверка, чтобы endDatePicker не был раньше startDatePicker
            if (endDatePicker.getYear() < startDatePicker.getYear() ||
                    (endDatePicker.getYear() == startDatePicker.getYear() && endDatePicker.getMonth() < startDatePicker.getMonth()) ||
                    (endDatePicker.getYear() == startDatePicker.getYear() && endDatePicker.getMonth() == startDatePicker.getMonth() && endDatePicker.getDayOfMonth() < startDatePicker.getDayOfMonth())) {

                // Установка endDatePicker на ту же дату, что и startDatePicker
                endDatePicker.updateDate(startDatePicker.getYear(), startDatePicker.getMonth(), startDatePicker.getDayOfMonth() + 1);
            }
        });

        clientsSpinner = findViewById(R.id.spinnerClients);
        apartamentsSpinner = findViewById(R.id.spinnerApartaments);

        toListBookings = findViewById(R.id.toListBookings);
        save_changes = findViewById(R.id.bookingsCreate_saveBTN);
    }

    private void setFields(){
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
                        Toast.makeText(BookingsCreate.this, "Не удалось загрузить услуги", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BookingsCreate.this, "Не удалось загрузить клиентов", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BookingsCreate.this, "Не удалось загрузить апартаменты", Toast.LENGTH_SHORT).show();
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
                selectedApartament = (Apartaments) parentView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не выбрано
            }
        });

        clientsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedClient = (Clients) parentView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ничего не выбрано
            }
        });
    }

    private void initializeCheckbox() {
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

    private void saveNew() {
        RESTHelper restHelper = new RESTHelper();
        BookingsApi bookingsApi = restHelper.getRetrofit().create(BookingsApi.class);

        newBooking.setStartTime((startDatePicker.getYear()) + "-" + (startDatePicker.getMonth() / 10 == 0 ? "0" + (startDatePicker.getMonth() + 1) : (startDatePicker.getMonth() + 1)) + "-" + (startDatePicker.getDayOfMonth() / 10 == 0 ? "0" + startDatePicker.getDayOfMonth() : startDatePicker.getDayOfMonth()));
        newBooking.setEndTime((endDatePicker.getYear()) + "-" + (endDatePicker.getMonth() / 10 == 0 ? "0" + (endDatePicker.getMonth() + 1): (endDatePicker.getMonth() + 1)) + "-" + (endDatePicker.getDayOfMonth() / 10 == 0 ? "0" + endDatePicker.getDayOfMonth() : endDatePicker.getDayOfMonth()));
        newBooking.setServices(selectedServices);
        newBooking.setApartament(selectedApartament);
        newBooking.setClient(selectedClient);
        newBooking.setActive(true);

        bookingsApi.update(newBooking)
                .enqueue(new Callback<Bookings>() {
                    @Override
                    public void onResponse(Call<Bookings> call, Response<Bookings> response) {
                        Toast.makeText(BookingsCreate.this, "Сохранение получилось!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Bookings> call, Throwable t) {
                        Toast.makeText(BookingsCreate.this, "Сохранение провалилось!!!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(ClientsCreate.class.getName()).log(Level.SEVERE, "Error occurred", t);
                    }
                });

    }
}