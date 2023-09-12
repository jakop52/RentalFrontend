package com.jakop52.apartmentrent.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.dto.PaymentDto;
import com.jakop52.apartmentrent.android.dto.ReservationDto;
import com.jakop52.apartmentrent.android.model.enums.PaymentPeriod;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ReservationService;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentFragment extends BottomSheetDialogFragment {
    private ReservationService reservationService;

    private ApartmentDto apartmentDto = null;
    private String sessionId = PreferencesManager.getInstance().getSessionId();

    private EditText editTextStartDate;
    private DatePicker datePickerStartDate;
    private EditText editTextEndDate;
    private DatePicker datePickerEndDate;
    private EditText editTextAmount;
    private Spinner spinnerPaymentPeriod;
    private Button buttonCancel;
    private Button buttonSendRentRequest;
    private boolean endDateSelected;
    Context context;


    public RentFragment() {
    }
    public RentFragment(ApartmentDto apartmentDto, Context context) {
        reservationService = ApiClient.getClient().create(ReservationService.class);
        this.apartmentDto=apartmentDto;
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_rent_request, container, false);

        editTextStartDate = rootView.findViewById(R.id.editTextStartDate);
        datePickerStartDate = rootView.findViewById(R.id.datePickerStartDate);
        editTextEndDate = rootView.findViewById(R.id.editTextEndDate);
        datePickerEndDate = rootView.findViewById(R.id.datePickerEndDate);
        editTextAmount = rootView.findViewById(R.id.editTextAmount);
        spinnerPaymentPeriod = rootView.findViewById(R.id.spinnerPaymentPeriod);
        buttonCancel = rootView.findViewById(R.id.buttonCancel);
        buttonSendRentRequest = rootView.findViewById(R.id.buttonSendRentRequest);


        editTextAmount.setText(apartmentDto.getRent().toString());
        ArrayAdapter<PaymentPeriod> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                PaymentPeriod.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentPeriod.setAdapter(adapter);

        editTextStartDate.setOnClickListener(v -> showHideDatePicker(datePickerStartDate));

        editTextEndDate.setOnClickListener(v -> showHideDatePicker(datePickerEndDate));

        buttonSendRentRequest.setOnClickListener(v -> {
            ZonedDateTime startDate = getSelectedDateFromDatePicker(datePickerStartDate);
            ZonedDateTime endDate=null;
            if(endDateSelected){
                endDate = getSelectedDateFromDatePicker(datePickerEndDate);
            }
            
            BigDecimal amount = new BigDecimal(editTextAmount.getText().toString());

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setAmount(amount);
            ReservationDto reservationDto = new ReservationDto();
            reservationDto.setApartmentId(apartmentDto.getId());
            reservationDto.setStartDate(startDate);
            reservationDto.setEndDate(endDate);
            reservationDto.setPaymentPeriod((PaymentPeriod) spinnerPaymentPeriod.getSelectedItem());


            Call<ReservationDto> call = reservationService.addReservation(reservationDto,sessionId);
            call.enqueue(new Callback<ReservationDto>() {
                @Override
                public void onResponse(Call<ReservationDto> call, Response<ReservationDto> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(context, "Rent request sent!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Error: "+response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ReservationDto> call, Throwable t) {
                    Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show();
                }
            });
            dismiss();
        });


        datePickerStartDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String startDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextStartDate.setText(startDate);
                datePickerStartDate.setVisibility(View.GONE);
            }
        });

        datePickerEndDate.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String endDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                editTextEndDate.setText(endDate);
                datePickerEndDate.setVisibility(View.GONE);
                endDateSelected=true;
            }
        });

        return rootView;
    }

    private void showHideDatePicker(DatePicker datePicker) {
        if (datePicker.getVisibility() == View.VISIBLE) {
            datePicker.setVisibility(View.GONE);
        } else {
            datePicker.setVisibility(View.VISIBLE);
        }
    }

    private ZonedDateTime getSelectedDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        ZonedDateTime zonedDateTime = calendar.toInstant().atZone(ZoneId.systemDefault());

        return zonedDateTime;
    }
}