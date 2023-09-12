package com.jakop52.apartmentrent.android.dto;

import com.jakop52.apartmentrent.android.model.enums.PaymentPeriod;

import java.time.ZonedDateTime;
import java.util.List;

public class ReservationDto {

    private Long id;
    private Long userId;
    private Long apartmentId;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    private PaymentPeriod paymentPeriod;
    private List<PaymentDto> payments;
    private boolean confirmed;

    public ReservationDto(){
    }

    public ReservationDto(Long id, Long userId, Long apartmentId, ZonedDateTime startDate, ZonedDateTime endDate, PaymentPeriod paymentPeriod, List<PaymentDto> payments, boolean confirmed) {
        this.id = id;
        this.userId = userId;
        this.apartmentId = apartmentId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.paymentPeriod = paymentPeriod;
        this.payments = payments;
        this.confirmed = confirmed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public List<PaymentDto> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDto> payments) {
        this.payments = payments;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
    public PaymentPeriod getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }
}
