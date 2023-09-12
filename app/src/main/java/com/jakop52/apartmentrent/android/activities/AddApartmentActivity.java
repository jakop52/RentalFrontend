package com.jakop52.apartmentrent.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.services.ApartmentService;

import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddApartmentActivity extends AppCompatActivity {

    private TextInputEditText etApartmentName;
    private TextInputEditText etDescription;
    private TextInputEditText etRent;
    private TextInputEditText etCity;

    private Button btnAddApartment;
    private ApartmentService apartmentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartment);

        etApartmentName = findViewById(R.id.etApartmentName);
        etDescription = findViewById(R.id.etDescription);
        etRent = findViewById(R.id.etRent);
        etCity = findViewById(R.id.etCity);

        btnAddApartment = findViewById(R.id.btnAddApartment);

        apartmentService = ApiClient.getClient().create(ApartmentService.class);

        btnAddApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addApartment();
            }
        });
    }

    private void addApartment() {
        String name = etApartmentName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String rentStr = etRent.getText().toString().trim();
        String city = etCity.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(rentStr) || TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal rent = new BigDecimal(rentStr);

        ApartmentDto newApartment = new ApartmentDto();
        newApartment.setName(name);
        newApartment.setDescription(description);
        newApartment.setRent(rent);
        newApartment.setCity(city);
        Call<ApartmentDto> call = apartmentService.addApartment(newApartment);
        call.enqueue(new Callback<ApartmentDto>() {
            @Override
            public void onResponse(Call<ApartmentDto> call, Response<ApartmentDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddApartmentActivity.this, "Mieszkanie dodane pomyślnie", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(AddApartmentActivity.this, "Wystąpił błąd podczas dodawania mieszkania", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApartmentDto> call, Throwable t) {
                Toast.makeText(AddApartmentActivity.this, "Nie udało się połączyć z serwerem", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
