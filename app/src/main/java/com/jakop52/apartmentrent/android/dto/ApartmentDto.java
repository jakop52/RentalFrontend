package com.jakop52.apartmentrent.android.dto;

import com.jakop52.apartmentrent.android.model.Media;

import java.math.BigDecimal;
import java.util.List;

public class ApartmentDto{

    private Long id;
    private String name;
    private String description;
    private BigDecimal rent;
    private String city;
    private String street;
    private String postalCode;
    private String country;
    private Long ownerId;

    private List<ReservationDto> reservations;

    private List<Media> media;

    public ApartmentDto(Long id, String name, String description, BigDecimal rent, String city, String street, String postalCode, String country, Long ownerId, List<ReservationDto> reservations, List<Media> media) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rent = rent;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.country = country;
        this.ownerId = ownerId;
        this.reservations = reservations;
        this.media = media;
    }

    public ApartmentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRent() {
        return rent;
    }

    public void setRent(BigDecimal rent) {
        this.rent = rent;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<ReservationDto> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDto> reservations) {
        this.reservations = reservations;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

}
