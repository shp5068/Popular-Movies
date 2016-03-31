package com.example.sagar.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter movieUrls;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        FetchMovieInformation MovieTask = new FetchMovieInformation();
        MovieTask.execute();
        movieUrls = new ImageAdapter(getActivity());

        GridView gridView = (GridView) view.findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(getActivity()));

        return view;
    }

    public class FetchMovieInformation extends AsyncTask<Void, Void, String[]> {

        public  final String LOG_TAG = FetchMovieInformation.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... params) {

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
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(PREF_PARAM)
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
                return movieDataFromJson(movieJsonStr);
            }
            catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;

        }

        private String[] movieDataFromJson(String movieJsonStr) throws JSONException {

            final String MD_RESULTS = "results";
            final String MD_POSTER = "poster_path";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieResultArray = movieJson.getJSONArray(MD_RESULTS);

            String[] moviePosterlinks = new String[movieResultArray.length()];

            for(int i = 0; i < movieResultArray.length(); i++) {

                JSONObject movie = movieResultArray.getJSONObject(i);
                moviePosterlinks[i] = "http://image.tmdb.org/t/p/w185" + movie.getString(MD_POSTER);
            }

//            Log.d(LOG_TAG, "QUERY URI: " + movieJsonStr);
            return moviePosterlinks;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result != null){
                movieUrls.getMoviePosterUrls(result);
            }
        }
    }
}
