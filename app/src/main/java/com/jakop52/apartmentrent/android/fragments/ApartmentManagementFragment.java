package com.jakop52.apartmentrent.android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.jakop52.apartmentrent.android.dto.ApartmentDto;
import com.jakop52.apartmentrent.android.dto.ReservationDto;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ApartmentService;
import com.jakop52.apartmentrent.android.services.ReservationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentManagementFragment extends BottomSheetDialogFragment {
    private ApartmentService apartmentService;
    private ReservationService reservationService;
    private ApartmentDto apartmentDto = null;
    private String sessionId = PreferencesManager.getInstance().getSessionId();
    private List<ReservationDto> reservationList;
    private Button buttonDeleteApartment;
    private LinearLayout reservationLayout;

    private View rootView;

    Context context;
    public ApartmentManagementFragment(){

    }

    public ApartmentManagementFragment(ApartmentDto apartmentDto, Context context) {
        apartmentService = ApiClient.getClient().create(ApartmentService.class);
        reservationService = ApiClient.getClient().create(ReservationService.class);
        this.apartmentDto=apartmentDto;
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_apartment_management, container, false);

        reservationLayout = rootView.findViewById(R.id.reservationLayout);
        buttonDeleteApartment = rootView.findViewById(R.id.buttonDeleteApartment);

        buttonDeleteApartment.setOnClickListener(v -> showDeleteApartmentConfirmationDialog());

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
                confirmButton.setVisibility(View.VISIBLE);
                paymentsButton.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(v -> deleteReservation(reservation));

            confirmButton.setOnClickListener(v -> confirmReservation(reservation));

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

    private void confirmReservation(ReservationDto reservation) {
        Call<ReservationDto> reservationDtoCall = reservationService.confirmReservation(reservation.getId());
        reservationDtoCall.enqueue(new Callback<ReservationDto>() {
            @Override
            public void onResponse(Call<ReservationDto> call, Response<ReservationDto> response) {
                if(response.isSuccessful()){
                    refreshFragment();
                }
            }

            @Override
            public void onFailure(Call<ReservationDto> call, Throwable t) {

            }
        });
    }
    private void showDeleteApartmentConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Apartment")
                .setMessage("Are you sure you want to delete this apartment?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> voidCall = ApiClient.getClient().create(ApartmentService.class).deleteOwnApartment(apartmentDto.getId(), sessionId);
                        voidCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private void refreshFragment() {
        Call<List<ReservationDto>> call = apartmentService.getApartmentReservations(apartmentDto.getId());
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
