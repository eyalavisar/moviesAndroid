package com.example.enfer.moviesdb.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.enfer.moviesdb.R;
import com.example.enfer.moviesdb.beans.Movie;
import com.example.enfer.moviesdb.db.DbDao;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InternetMovieSearch extends AppCompatActivity {

    private EditText text;
    private String search;
    private Movie movie;
    private List<Movie> movies;
    private ArrayAdapter<Movie> adapter;
    private DbDao dao = new DbDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_movie_search);

        movies = new ArrayList<>();
        adapter = new ArrayAdapter<Movie>(getApplicationContext(), android.R.layout.simple_list_item_1,movies);
        ListView listView = (ListView) findViewById(R.id.moviesList);
        listView.setAdapter(adapter);

        Button get = (Button)findViewById(R.id.getBtn);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = findViewById(R.id.movie);
                search = text.getText().toString();
//                movies = new ArrayList<>();
                new GetMovie().execute();
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                movie = adapter.getItem(i);

                try {
                    if(dao.isSameMovie(movie.getTitle(), movie.getOverView())){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                InternetMovieSearch.this);
                        alertDialogBuilder.setTitle("The movie exists");
                        alertDialogBuilder
                                .setMessage("Click yes to view movie!")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        goToEditMovie();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();

                }
                else {
                        goToEditMovie();
                    }
                } catch (Exception e) {
                    Log.e("Notice!", e.getMessage());
                }
            }
        });

    }



    public class GetMovie extends AsyncTask<String, Void, String> {
        //        private final String API_URL = "http://www.omdbapi.com/?s=";
        private ProgressDialog progMsg;

        @Override
        protected void onPreExecute() {
            progMsg = new ProgressDialog(InternetMovieSearch.this);
            progMsg.setMessage("Loading Your data,please wait.");
            progMsg.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuilder builder = new StringBuilder();

            try {
                //URL url = new URL(API_URL + params[0]);

                // Sending a GET request

                URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=1383c074a85a14a0ed99121d43f6eb1a&query=" + search);

                // Performing the GET call to the server
                connection = (HttpURLConnection) url.openConnection();

                // If the response from the server is not OK (the request failed)
                // Then we return an error message and stop here.
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Error From Server - " + connection.getResponseCode();
                }

                // We read the response into a StringBuilder object, to which we insert the JSON
                // object, which returned from the server
                // Remember - a json object can be an array by itself, an array of JSON objects
                //----------------------------------------------------------------------------------
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                //----------------------------------------------------------------------------------


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("get movie task", e.getMessage());
                    }
                }
            }
            return builder.toString();
        }

        protected void onPostExecute(String responseFromServer) {
            // We validate that the response does not start with the word Error ("Error From Server...")
            // That way we know it is safe to parse and display the data.
            adapter.clear();
            if (!responseFromServer.startsWith("Error")) {

                // Close the graphic view which shows an indication of a long wait for the user
                progMsg.dismiss();

                JsonParser jp=new JsonParser();
                JsonElement element = jp.parse(responseFromServer);
                JsonObject results = element.getAsJsonObject();
                JsonArray results1 = results.getAsJsonArray("results");
//
                Iterator iterator = results1.iterator();

                while (iterator.hasNext()) {
                    JsonElement dElement = (JsonElement) iterator.next();
                    String title = String.valueOf((JsonElement) dElement.getAsJsonObject().get("title"));
                    String overview = String.valueOf((JsonElement) dElement.getAsJsonObject().get("overview"));
                    String urlImage = String.valueOf((JsonElement) dElement.getAsJsonObject().get("poster_path"));
                    String averageVote = String.valueOf((JsonElement) dElement.getAsJsonObject().get("vote_average"));
                    float voteAverage = Float.parseFloat(averageVote);

                    title = title.substring(1, title.length() - 1);
                    urlImage = urlImage.substring(2, urlImage.length() - 1);
                    movie = new Movie(title, overview, urlImage, voteAverage, false);//, index

                    movies.add(movie);
                }

                adapter.notifyDataSetChanged();

                if (responseFromServer == null) {
                    Toast.makeText(getApplicationContext(), "null text", Toast.LENGTH_LONG);
                }
            }
//
        }


    }
    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
       state.putSerializable("movies", (ArrayList<Movie>)movies);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
//        this.movies.clear();
        this.movies.addAll((List<Movie>) state.getSerializable("movies"));
        this.adapter.notifyDataSetChanged();
    }

    public void goToEditMovie(){
        Intent intent = new Intent(getApplicationContext(), EditMovie.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
        finish();
        InternetMovieSearch.this.finish();
    }

}