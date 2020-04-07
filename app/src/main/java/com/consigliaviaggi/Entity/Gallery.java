package com.consigliaviaggi.Entity;

public class Gallery {

    private int idGallery;
    private String immagine;

    public Gallery(int idGallery, String immagine) {
        this.idGallery = idGallery;
        this.immagine = immagine;
    }

    public int getIdGallery() {
        return idGallery;
    }

    public void setIdGallery(int idGallery) {
        this.idGallery = idGallery;
    }

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
    }
}
