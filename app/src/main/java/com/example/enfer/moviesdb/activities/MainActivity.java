package com.example.enfer.moviesdb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.enfer.moviesdb.R;
import com.example.enfer.moviesdb.beans.Movie;
import com.example.enfer.moviesdb.db.DbDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayAdapter<Movie> adapterDb;
    private List<Movie> moviesDb;
    private ListView listViewDb;
    private Movie movie;
    private DbDao dao = new DbDao(this);
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            moviesDb = dao.getAllMovies();
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
        }
        adapterDb = new ArrayAdapter<Movie>(getApplicationContext(), android.R.layout.simple_list_item_1, moviesDb);

        listViewDb = (ListView) findViewById(R.id.listViewDb);
        listViewDb.setAdapter(adapterDb);
        registerForContextMenu(listViewDb);

//        Button surf = findViewById(R.id.surf); //search a movie on the net
//        surf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), InternetMovieSearch.class);
//                startActivity(intent);
//            }
//        });

//        Button addManually = findViewById(R.id.addMovie);
//        addManually.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),AddMovie.class);
//                startActivity(intent);
//            }
//        });

        listViewDb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = adapterDb.getItem(i);
                Intent intent = new Intent(getApplicationContext(), EditMovie.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //inhibit use of back button from the first screen
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.exit:
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                System.exit(1);
                return true;
            case R.id.deleteAll:
                try {
                    dao.deleteAll();
                    moviesDb = dao.getAllMovies();
                } catch (Exception e) {
                    Log.e("Notice!", e.getMessage());
                }
                adapterDb = new ArrayAdapter<Movie>(getApplicationContext(), android.R.layout.simple_list_item_1,moviesDb);
                listViewDb.setAdapter(adapterDb);
                return true;
            case R.id.addManually:
                Intent intent = new Intent(getApplicationContext(),AddMovie.class);
                startActivity(intent);
                return true;
            case R.id.addUrl:
                intent = new Intent(getApplicationContext(), InternetMovieSearch.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        position = info.position;

        // Now you can do whatever.. (Example, load different menus for different items)
        movie = adapterDb.getItem(position);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        menu.setHeaderTitle("Select The Action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.edit){
            movie = adapterDb.getItem(position);
            Intent intent = new Intent(getApplicationContext(), EditMovie.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.delete){
            try {
                dao.deleteMovie(movie);
                moviesDb = dao.getAllMovies();
            } catch (Exception e) {
                Log.e("Notice!", e.getMessage());
            }
            adapterDb = new ArrayAdapter<Movie>(getApplicationContext(), android.R.layout.simple_list_item_1,moviesDb);
            listViewDb.setAdapter(adapterDb);

        }
        else{
            return false;
        }
        return true;
    }


}
