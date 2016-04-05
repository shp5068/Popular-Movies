package com.example.sagar.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sagar.popularmovies.Model.MovieModel;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra("MovieObject")){

            MovieModel movie = new MovieModel(intent.getBundleExtra("MovieObject"));
            ((TextView) view.findViewById(R.id.detail_movie_title)).setText(movie.movieTitle);
            ((TextView) view.findViewById(R.id.detail_movie_rating)).setText(String.valueOf(movie.movieUserRating) + " / 10");
            ((TextView) view.findViewById(R.id.detail_movie_overview)).setText(movie.movieOverview);
            ((TextView) view.findViewById(R.id.detail_movie_date)).setText(movie.movieReleaseDate);

            Picasso.with(getActivity())
                    .load(movie.moviePosterPath)
                    .into((ImageView) view.findViewById(R.id.detail_movie_poster));

        }

        return view;

    }
}
