package com.example.sagar.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.sagar.popularmovies.Model.MovieModel;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter movieUrls;
    private int pageNumber = 0;
    private boolean movieLoading = false;

    public MainActivityFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        movieUrls = new ImageAdapter(getActivity());
        gridView.setAdapter(movieUrls);

        startLoading();
        listeners(view);

        return view;
    }

    private void listeners(View view){

        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageAdapter adapter = (ImageAdapter) parent.getAdapter();
                MovieModel movie = adapter.getItem(position);



                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("MovieObject", movie.bundle());
                getActivity().startActivity(intent);
            }
        });

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount)
                {
                    startLoading();
                }
            }
        });

    }

    private void startLoading (){

        if(!movieLoading)
        {
            FetchMovieInformation MovieTask = new FetchMovieInformation();
            pageNumber +=1;
            MovieTask.execute(pageNumber);
            movieLoading = true;
        }
    }


    public class FetchMovieInformation extends AsyncTask<Integer, Void, ArrayList<MovieModel>> {

        public  final String LOG_TAG = FetchMovieInformation.class.getSimpleName();

        @Override
        protected ArrayList<MovieModel> doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr;

            String APPID = "";
            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String PAGE_PARAM = "page";
                final String APPID_PARAM = "api_key";
                final String PREF_PARAM = PreferenceManager
                        .getDefaultSharedPreferences(getActivity())
                        .getString(
                                getString(R.string.pref_sorting_key),
                                getString(R.string.pref_sorting_default_value)
                        );

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(PREF_PARAM)
                        .appendQueryParameter(PAGE_PARAM, String.valueOf(params[0]))
                        .appendQueryParameter(APPID_PARAM, APPID)
                        .build();

//                Log.d(LOG_TAG, "QUERY URI: " + builtUri.toString());

                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment: ", "Error ", e);

                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment: ", "Error closing stream", e);
                    }
                }
            }

            try{
                return  MovieModel.movieDataFromJson(movieJsonStr);
            }
            catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> result) {
            if(result.size() != 0){
                movieUrls.setMovieLists(result);
                startLoading();
            }
            movieLoading = false;
        }
    }
}
