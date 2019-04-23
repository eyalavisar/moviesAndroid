package com.example.enfer.moviesdb.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.example.enfer.moviesdb.R;
import com.example.enfer.moviesdb.beans.Movie;

import java.io.InputStream;

public class EditMovie extends AddMovie {
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_movie);

        movieTitle = findViewById(R.id.editTitle);
        overView = findViewById(R.id.editBody);
        urlImage = findViewById(R.id.editUrl);
        ratingBar = findViewById(R.id.ratingBar);
        watched = findViewById(R.id.isWatched);
        image = findViewById(R.id.image);
        save = findViewById(R.id.saveBtn);


        extras = getIntent().getExtras();
        movie = (Movie) extras.get("movie");
        movieTitle.setText(movie.getTitle());
        overView.setText(movie.getOverView());
        urlImage.setText(movie.getUrlImage());
        String pic = "https://image.tmdb.org/t/p/w500/" + movie.getUrlImage().toString();
        Toast.makeText(this, pic, Toast.LENGTH_LONG).show();

        try {
            new DownloadImageTask(image)
                    .execute(pic);
        }
        catch (Exception e){}
        ratingBar.setRating((movie.getRating()));
        watched.setChecked(movie.isWatched());


        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = movieTitle.getText().toString();
                String url = urlImage.getText().toString();
                String description = overView.getText().toString();
                float rating = ratingBar.getRating();
                boolean isWatched = watched.isChecked();
                int id = movie.getId();

                if(id != 0){
                    movie = new Movie(id, title, description, url, rating, isWatched);
                    try {
                        dao.updateMovie(movie);
                    } catch (Exception e) {
                        Log.e("Notice!", e.getMessage());
                    }
                }
                else {
                    movie = new Movie(title, description, url, rating, isWatched);
                    try {
                        dao.addMovie(movie);
                    } catch (Exception e) {
                        Log.e("Notice!", e.getMessage());
                    }
                }


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Notice!", e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
