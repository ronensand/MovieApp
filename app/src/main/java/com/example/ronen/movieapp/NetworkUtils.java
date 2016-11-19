package com.example.ronen.movieapp;


import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Ronen on 19/11/2016.
 */

public class NetworkUtils{

    private static final String LOG_TAG = NetworkUtils.class.getName();

    private static String THEMOVIEIDB_BASE_URL =  "https://api.themoviedb.org/3/discover/movie";

    private static final String APIKEY_PARAM = "api_key";
    private static final String SORTBY_PARAM = "sort_by";
    private static final String LANGUAGE_PARAM = "language";
    private static final String ID_PARAM = "id";
    private static final String POSER_PATH_PARAM = "poster_path";
    private static final String IMAGE_LIST_PARAM = "results";



    public void getMovies(Callback callback) {
        Uri uri = Uri.parse(THEMOVIEIDB_BASE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, BuildConfig.THEMOVIEDB_MAP_API_KEY)
                .build();

        execute(callback, uri.toString());
    }

    private void execute(Callback callback, String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request)
                .enqueue(callback);
    }

    public static MovieEntry ConvertJsonToMovieEntry(JSONObject object) {

        String id = null;
        try {
            id = object.getString(ID_PARAM);
            String imageUrl = object.getString(POSER_PATH_PARAM);

            return new MovieEntry(imageUrl, id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<MovieEntry> ConvertJsonStringToMovies(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);
            List<MovieEntry> moviesList = new ArrayList<MovieEntry>();
            JSONArray moviesArray = object.getJSONArray(IMAGE_LIST_PARAM);

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject jsonObject = moviesArray.getJSONObject(i);
                moviesList.add(ConvertJsonToMovieEntry(jsonObject));
            }

            return moviesList;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
