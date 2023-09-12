package com.jakop52.apartmentrent.android.fragments;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakop52.apartmentrent.android.R;
import com.jakop52.apartmentrent.android.api.ApiClient;
import com.jakop52.apartmentrent.android.dto.PaymentDto;
import com.jakop52.apartmentrent.android.dto.ReservationDto;
import com.jakop52.apartmentrent.android.model.enums.PaymentStatus;
import com.jakop52.apartmentrent.android.preferences.PreferencesManager;
import com.jakop52.apartmentrent.android.services.ApartmentService;
import com.jakop52.apartmentrent.android.services.PaymentService;
import com.jakop52.apartmentrent.android.services.ReservationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentListFragment extends BottomSheetDialogFragment {
    private PaymentService paymentService;
    private ReservationService reservationService;
    private ApartmentService apartmentService;
    private ReservationDto reservationDto;
    private Context context;

    private String sessionId = PreferencesManager.getInstance().getSessionId();
    private List<PaymentDto> paymentList;
    private boolean asOwner;

    LinearLayout paymentsLinearLayout;
    public PaymentListFragment(){}

    public PaymentListFragment(ReservationDto reservationDto, Context context) {
        this.paymentService = ApiClient.getClient().create(PaymentService.class);
        this.reservationService = ApiClient.getClient().create(ReservationService.class);
        this.apartmentService = ApiClient.getClient().create(ApartmentService.class);
        this.reservationDto=reservationDto;
        this.context=context;
    }



    private void checkIsOwner() {
        Call<Boolean> isOwnerCall = apartmentService.isApartmentOwner(reservationDto.getApartmentId(),sessionId);
        isOwnerCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    asOwner=response.body();
                }else asOwner=false;
                refreshFragment();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                asOwner=false;
            }
        });

    }

    private void refreshFragment() {
        Call<List<PaymentDto>> call = reservationService.getReservationPayments(reservationDto.getId());
        call.enqueue(new Callback<List<PaymentDto>>() {
            @Override
            public void onResponse(Call<List<PaymentDto>> call, Response<List<PaymentDto>> response) {
                if (response.isSuccessful()) {
                    paymentList = response.body();
                    displayReservations(paymentsLinearLayout);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PaymentDto>> call, Throwable t) {
                Toast.makeText(getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void displayReservations(LinearLayout linearLayout) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        linearLayout.removeAllViews();

        for (PaymentDto payment : paymentList) {
            Log.d("TAG", "displayReservations: "+payment.getPaymentStatus());
            View reservationView = inflater.inflate(R.layout.item_payment, linearLayout, false);

            TextView paymentStatus = reservationView.findViewById(R.id.paymentStatusTextView);
            TextView paymentDateTextView = reservationView.findViewById(R.id.paymentDateTextView);
            Button payButton = reservationView.findViewById(R.id.payButton);

            paymentDateTextView.setText("Payment date: " + payment.getPaymentDate().toString());
            paymentStatus.setText("Status: "+payment.getPaymentStatus());

            if(!asOwner){
                if (payment.getPaymentStatus() == PaymentStatus.NOT_PAID) {
                    payButton.setVisibility(View.VISIBLE);
                } else {
                    payButton.setVisibility(View.GONE);
                }
            }else {
                payButton.setVisibility(View.GONE);
            }

            payButton.setOnClickListener(v -> payPayment(payment));

            linearLayout.addView(reservationView);
        }
    }

    private void payPayment(PaymentDto payment) {
        Call<String> payPaymentCall = paymentService.payPayment(payment.getId());
        payPaymentCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    refreshFragment();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void showPaymentsForReservation(ReservationDto reservation) {
        PaymentListFragment paymentListFragment = new PaymentListFragment(reservation,context);
        paymentListFragment.show(getChildFragmentManager(), paymentListFragment.getTag());
    }
}
