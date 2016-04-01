package com.example.sagar.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
    private String[] imageUrls;
    public static ArrayList<String> movieUrls = new ArrayList<String>();

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return movieUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
                .load(movieUrls.get(position))
                .into((ImageView) convertView);

        return convertView;
    }

    public void getMoviePosterUrls(ArrayList<String> imageUrls){
        movieUrls.addAll(imageUrls);
        notifyDataSetChanged();
    }
}
