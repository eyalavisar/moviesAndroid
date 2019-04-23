package com.example.enfer.moviesdb.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.enfer.moviesdb.R;
import com.example.enfer.moviesdb.beans.Movie;
import com.example.enfer.moviesdb.db.DbDao;
import java.io.InputStream;

public class AddMovie extends AppCompatActivity {
    protected EditText movieTitle;
    protected EditText overView;
    protected EditText urlImage;
    protected RatingBar ratingBar;
    protected CheckBox watched;
    protected Movie movie;
    protected ImageView image;
    protected Button save;
    protected DbDao dao = new DbDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        movieTitle = findViewById(R.id.editTitle);
        overView = findViewById(R.id.editBody);
        urlImage = findViewById(R.id.editUrl);
        ratingBar = findViewById(R.id.ratingBar);
        watched = findViewById(R.id.isWatched);
        image = findViewById(R.id.image);

        save = findViewById(R.id.saveBtn);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = movieTitle.getText().toString();
                String url = urlImage.getText().toString();
                String description = overView.getText().toString();
                float rating = ratingBar.getRating();
                boolean isWatched = watched.isChecked();

                movie = new Movie(title, description, url, rating, isWatched);

                try {
                    dao.addMovie(movie);
                } catch (Exception e) {
                    Log.e("Notice!", e.getMessage());
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
