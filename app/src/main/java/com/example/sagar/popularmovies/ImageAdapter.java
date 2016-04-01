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
    public ArrayList<String> movieUrls  = new ArrayList<>();
    public ArrayList<MovieModel> movieList = new ArrayList<>();

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setMovieLists(ArrayList<MovieModel> movieList){
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.d("Get Count Two: ", String.valueOf(movieUrls.size()));
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

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185" + movieUrls.get(position))
                .into((ImageView) convertView);

        return convertView;
    }
}
