package com.jakop52.apartmentrent.android.model;

import com.jakop52.apartmentrent.android.model.enums.PaymentPeriod;
import com.jakop52.apartmentrent.android.model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private Long id;
    private Reservation reservation;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentPeriod paymentPeriod;
    private PaymentStatus status = PaymentStatus.NOT_PAID;


    public Payment() {
    }

    public Payment(Long id, Reservation reservation, BigDecimal amount, LocalDate paymentDate, PaymentPeriod paymentPeriod, PaymentStatus status) {
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentPeriod = paymentPeriod;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentPeriod getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(PaymentPeriod paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
