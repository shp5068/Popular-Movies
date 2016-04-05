package com.example.sagar.popularmovies.Model;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sagar on 4/1/16.
 *
 * original title
 *   movie poster image thumbnail
 *   A plot synopsis (called overview in the api)
 *   user rating (called vote_average in the api)
 *   release date
 *
 *
 */
public class MovieModel {

    public static final String MOVIE_TITLE = "title";
    public static final String MOVIE_POSTER_PATH = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String MOVIE_USER_RATING = "vote_average";
    public static final String MOVIE_RESULT = "results";

    public final String movieTitle;
    public final String moviePosterPath;
    public final String movieOverview;
    public final String movieReleaseDate;
    public final Double movieUserRating;

    public MovieModel(String title, String posterPath, String overview, String releaseDate, Double userRating){
        this.movieTitle = title;
        this.moviePosterPath = posterPath;
        this.movieOverview = overview;
        this.movieReleaseDate = releaseDate;
        this.movieUserRating = userRating;
    }

    public MovieModel(Bundle bundle) {
        this(
                bundle.getString(MOVIE_TITLE),
                bundle.getString(MOVIE_POSTER_PATH),
                bundle.getString(MOVIE_OVERVIEW),
                bundle.getString(MOVIE_RELEASE_DATE),
                bundle.getDouble(MOVIE_USER_RATING)

        );
    }

    public static ArrayList<MovieModel> movieDataFromJson(String movieJsonStr) throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieResultArray = movieJson.getJSONArray(MOVIE_RESULT);

        ArrayList<MovieModel> movieList = new ArrayList<>();


        for(int i = 0; i < movieResultArray.length(); i++) {

            JSONObject singleMovieData = movieResultArray.getJSONObject(i);

            movieList.add(new MovieModel(
                            singleMovieData.getString(MOVIE_TITLE),
                            "http://image.tmdb.org/t/p/w185" + singleMovieData.getString(MOVIE_POSTER_PATH),
                            singleMovieData.getString(MOVIE_OVERVIEW),
                            singleMovieData.getString(MOVIE_RELEASE_DATE),
                            Double.parseDouble(singleMovieData.getString(MOVIE_USER_RATING)))
            );
        }

        return movieList;
    }

    public Bundle bundle(){
        Bundle bundle = new Bundle();

        bundle.putString(MOVIE_TITLE, movieTitle);
        bundle.putString(MOVIE_OVERVIEW, movieOverview);
        bundle.putString(MOVIE_POSTER_PATH, moviePosterPath);
        bundle.putDouble(MOVIE_USER_RATING, movieUserRating);
        bundle.putString(MOVIE_RELEASE_DATE, movieReleaseDate);

        return bundle;
    }

}

