package com.example.sagar.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sagar on 3/30/16.
 */
public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    private String[] imageUrls;

    public ImageAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return eatFoodyImages.length;
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
                .load(eatFoodyImages[position])
                .into((ImageView) convertView);

        return convertView;
    }

    public void getMoviePosterUrls(String[] imageUrls){
        eatFoodyImages = imageUrls;
        notifyDataSetChanged();
    }

    public static String[] eatFoodyImages = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };
}
