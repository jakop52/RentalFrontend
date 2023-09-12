package com.jakop52.apartmentrent.android.model;

import java.time.ZonedDateTime;
import java.util.List;
public class Reservation {
    private Long id;
    private User user;
    private Apartment apartment;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private List<Payment> payments;

    public Reservation() {
    }

    public Reservation(Long id, User user, Apartment apartment, ZonedDateTime startDate, ZonedDateTime endDate, List<Payment> payments) {
        this.id = id;
        this.user = user;
        this.apartment = apartment;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payments = payments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}