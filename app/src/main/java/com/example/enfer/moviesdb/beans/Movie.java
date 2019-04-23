package com.example.enfer.moviesdb.beans;

import java.io.Serializable;

public class Movie implements Serializable {
    private int id;
    private String title;
    private String overView;
    private String urlImage;
    private float rating;
    private boolean watched;

    public Movie(){}

    public Movie(String title, String overView, String urlImage, float rating, boolean watched) {//, long xIndex
        setTitle(title);
        this.overView = overView;
        this.urlImage = urlImage;
        this.rating = rating;
        this.watched = watched;
    }

    public Movie(int id, String title, String overView, String urlImage, float rating, boolean watched) {//, long xIndex
        this.id = id;
        this.title = title;
        this.overView = overView;
        this.urlImage = urlImage;
        this.rating = rating;
        this.watched = watched;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {

        this.urlImage = urlImage;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals(Object obj) {
        Movie movie = (Movie) obj;
        if(this.getOverView() == movie.getOverView() && this.getTitle() == movie.getTitle()){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getOverView().hashCode() +  this.getTitle().hashCode();
    }
}

