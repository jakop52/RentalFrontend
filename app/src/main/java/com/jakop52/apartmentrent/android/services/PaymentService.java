package com.jakop52.apartmentrent.android.services;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PaymentService {
    @PUT("payments/{paymentId}/pay")
    Call<String> payPayment(@Path("paymentId") Long paymentId);
}
