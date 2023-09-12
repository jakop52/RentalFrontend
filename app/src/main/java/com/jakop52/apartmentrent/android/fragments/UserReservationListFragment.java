package com.jakop52.apartmentrent.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.ReservationDto;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ReservationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserReservationListFragment extends BottomSheetDialogFragment {
    private Context context;
    private ReservationService reservationService;
    private String sessionId = PreferencesManager.getInstance().getSessionId();

    private View rootView;
    private LinearLayout reservationLayout;
    private Button buttonDeleteApartment;
    private List<ReservationDto> reservationList;


    public UserReservationListFragment() {
    }

    public UserReservationListFragment(Context context) {
        reservationService = ApiClient.getClient().create(ReservationService.class);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_apartment_management, container, false);

        reservationLayout = rootView.findViewById(R.id.reservationLayout);
        buttonDeleteApartment = rootView.findViewById(R.id.buttonDeleteApartment);
        buttonDeleteApartment.setVisibility(View.GONE);


        refreshFragment();


        return rootView;
    }

    private void displayReservations(LinearLayout reservationLayout) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        reservationLayout.removeAllViews();

        for (ReservationDto reservation : reservationList) {
            Log.d("TAG", "displayReservations: "+reservation.isConfirmed());
            View reservationView = inflater.inflate(R.layout.item_reservation, reservationLayout, false);

            TextView startDateTextView = reservationView.findViewById(R.id.startDateTextView);
            TextView endDateTextView = reservationView.findViewById(R.id.endDateTextView);
            TextView paymentPeriodTextView = reservationView.findViewById(R.id.paymentPeriodTextView);
            Button deleteButton = reservationView.findViewById(R.id.deleteButton);
            Button confirmButton = reservationView.findViewById(R.id.confirmButton);
            Button paymentsButton = reservationView.findViewById(R.id.paymentsButton);

            startDateTextView.setText("Start date: " + reservation.getStartDate().toString());
            endDateTextView.setText("End date: " + reservation.getEndDate().toString());
            paymentPeriodTextView.setText("Payment period: " + reservation.getPaymentPeriod().toString());

            if (reservation.isConfirmed()) {
                deleteButton.setVisibility(View.GONE);
                confirmButton.setVisibility(View.GONE);
                paymentsButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.GONE);
                paymentsButton.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(v -> deleteReservation(reservation));

            paymentsButton.setOnClickListener(v -> showPaymentsForReservation(reservation));

            reservationLayout.addView(reservationView);
        }
    }
    private void showPaymentsForReservation(ReservationDto reservation) {
        PaymentListFragment paymentListFragment = new PaymentListFragment(reservation,context);
        paymentListFragment.show(getChildFragmentManager(), paymentListFragment.getTag());
    }

    private void deleteReservation(ReservationDto reservation) {
        Call<Void> reservationDtoCall = reservationService.deleteReservation(reservation.getId());
        reservationDtoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    refreshFragment();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    private void refreshFragment() {
        Call<List<ReservationDto>> call = reservationService.getUserReservations(sessionId);
        call.enqueue(new Callback<List<ReservationDto>>() {
            @Override
            public void onResponse(Call<List<ReservationDto>> call, Response<List<ReservationDto>> response) {
                if (response.isSuccessful()) {
                    reservationList = response.body();
                    displayReservations(reservationLayout);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReservationDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
