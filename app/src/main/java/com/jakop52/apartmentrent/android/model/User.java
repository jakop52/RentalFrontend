package com.jakop52.apartmentrent.android.model;

import java.util.List;
import java.util.Set;

public class User{
    private Long id;

    private String username;

    private String password;

    private String email;

    private List<Apartment> apartments;

    private List<Reservation> reservations;
    private Set<Role> roles;

    public User() {
    }
}