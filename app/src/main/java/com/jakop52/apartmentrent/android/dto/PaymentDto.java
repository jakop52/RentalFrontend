package com.jakop52.apartmentrent.android.dto;

import com.jakop52.apartmentrent.android.model.enums.PaymentPeriod;
import com.jakop52.apartmentrent.android.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentDto {
    private Long id;
    private Long reservationId;
    private BigDecimal amount;
    private ZonedDateTime paymentDate;
    private PaymentStatus paymentStatus;

    public PaymentDto(){}

    public PaymentDto(BigDecimal amount, PaymentPeriod paymentPeriod) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
