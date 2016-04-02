package com.example.sagar.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.sagar.popularmovies.Model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sagar on 3/30/16.
 * ImageAdapter - loads images into ImageView using Picasso
 *
 */
public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private final ArrayList<String> movieUrls;
    private final ArrayList<MovieModel> movieList;

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.movieUrls = new ArrayList<>();
        this.movieList = new ArrayList<>();
    }

    public void setMovieLists(ArrayList<MovieModel> mList){
        movieList.addAll(mList);
        movieUrls.addAll(getPosterLinksArray(mList));

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return movieUrls.size();
    }

    @Override
    public MovieModel getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_movie_posters, parent, false);
        }

        Log.d("Link: ", movieUrls.get(position));

        Picasso.with(context)
                .load(movieUrls.get(position))
                .into((ImageView) convertView);

        return convertView;
    }

    public static ArrayList<String> getPosterLinksArray(ArrayList<MovieModel> movieList){
        ArrayList<String> posterLinksArray = new ArrayList<>();


        for(int i = posterLinksArray.size(); i < movieList.size(); i++){
            posterLinksArray.add("http://image.tmdb.org/t/p/w185" + movieList.get(i).moviePosterPath);
        }

        return posterLinksArray;
    }
}
