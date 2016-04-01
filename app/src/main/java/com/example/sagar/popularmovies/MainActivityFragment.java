package com.example.sagar.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        initateLoading();
        listeners(view);

        return view;
    }

    private void listeners(View view){

        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    initateLoading();
                }
            }
        });

    }

    private void initateLoading (){

        if(!movieLoading)
        {
            final FetchMovieInformation MovieTask = new FetchMovieInformation();
            pageNumber +=1;
            MovieTask.execute(pageNumber);
            movieUrls = new ImageAdapter(getActivity());
            movieLoading = true;
        }
    }


    public class FetchMovieInformation extends AsyncTask<Integer, Void, ArrayList<String>> {

        public  final String LOG_TAG = FetchMovieInformation.class.getSimpleName();

        @Override
        protected ArrayList<String> doInBackground(Integer... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr;

            String APPID = "";
            try {
                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String PREF_PARAM = "popular";
                final String PAGE_PARAM = "page";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(PREF_PARAM)
                        .appendQueryParameter(PAGE_PARAM, String.valueOf(params[0]))
                        .appendQueryParameter(APPID_PARAM, APPID)
                        .build();

                Log.d(LOG_TAG, "QUERY URI: " + builtUri.toString());

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
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            Log.d(LOG_TAG, "QUERY URI: " + movieJsonStr);

            try{
                return  movieDataFromJson(movieJsonStr);
            }
            catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;

        }

        private ArrayList<String> movieDataFromJson(String movieJsonStr) throws JSONException {

            final String MD_RESULTS = "results";
            final String MD_POSTER = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieResultArray = movieJson.getJSONArray(MD_RESULTS);

            ArrayList<String> moviePosterlinks = new ArrayList<>();


            for(int i = 0; i < movieResultArray.length(); i++) {

                JSONObject movie = movieResultArray.getJSONObject(i);
                moviePosterlinks.add("http://image.tmdb.org/t/p/w185" + movie.getString(MD_POSTER));
            }

//            Log.d(LOG_TAG, "QUERY URI: " + movieJsonStr);
            return moviePosterlinks;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if(result != null){
                movieUrls.getMoviePosterUrls(result);
            }
            movieLoading = false;
        }
    }
}
