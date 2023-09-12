package com.jakop52.apartmentrent.android.model;


public class Media {
    private Long id;
    private String mediaPath;
    private Apartment apartment;

    public Media(String mediaPath) {
        this.mediaPath=mediaPath;
    }

    public Media() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
}
