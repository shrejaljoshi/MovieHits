package com.b2infosoft.moviehits.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Choudhary Computer on 7/21/2018.
 */

public class Dashboard_item implements Parcelable {
   public String id;
    public String rating;
    public String MovieTitle;
    public String MovieImage;
    public  String MovieDescription;
    public String MovieReleaseDate;


    public Dashboard_item(String id, String rating, String movieTitle, String movieImage, String movieDescription, String movieReleaseDate) {
        this.id = id;
        this.rating = rating;
        MovieTitle = movieTitle;
        MovieImage = movieImage;
        MovieDescription = movieDescription;
        MovieReleaseDate = movieReleaseDate;
    }


    protected Dashboard_item(Parcel in) {
        id = in.readString();
        rating = in.readString();
        MovieTitle = in.readString();
        MovieImage = in.readString();
        MovieDescription = in.readString();
        MovieReleaseDate = in.readString();
    }

    public static final Creator<Dashboard_item> CREATOR = new Creator<Dashboard_item>() {
        @Override
        public Dashboard_item createFromParcel(Parcel in) {
            return new Dashboard_item(in);
        }

        @Override
        public Dashboard_item[] newArray(int size) {
            return new Dashboard_item[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getMovieImage() {
        return MovieImage;
    }

    public void setMovieImage(String movieImage) {
        MovieImage = movieImage;
    }

    public String getMovieDescription() {
        return MovieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        MovieDescription = movieDescription;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getMovieReleaseDate() {
        return MovieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        MovieReleaseDate = movieReleaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMovieTitle());
        dest.writeString(getMovieImage());
    }
}
