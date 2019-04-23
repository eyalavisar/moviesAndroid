package com.example.enfer.moviesdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.enfer.moviesdb.beans.Movie;

import java.util.ArrayList;
import java.util.List;

public class DbDao {

    private DbCUD dbCUD;

    public DbDao(Context context) {
        dbCUD = new DbCUD(context);
    }

    // code to add the new movie
    public void addMovie(Movie movie) throws Exception {
        SQLiteDatabase db = null;
        int watched = 0; //SQLite has no boolean definition
        try {
            db = dbCUD.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbConstants.TITLE, movie.getTitle());
            values.put(DbConstants.OVERVIEW, movie.getOverView());
            values.put(DbConstants.URLIMAGE, movie.getUrlImage());
            values.put(DbConstants.RATING, movie.getRating());
            values.put(DbConstants.WATCHED, watched);
            if (movie.isWatched()) {
                watched = 1;
            }

            //values.put(DbConstants.XINDEX, movie.getIndex());
            // Inserting Row
            db.insert(DbConstants.TABLE_MOVIES, null, values);
            Log.i("database", db.toString());
            //2nd argument is String containing nullColumnHack
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        } finally {
            db.close(); // Closing database connection

        }
    }

    public Movie getMovie(int id) throws Exception {
        SQLiteDatabase db = null;

        try {
            db = dbCUD.getReadableDatabase();
            Cursor cursor = db.query(DbConstants.TABLE_MOVIES, new String[]{DbConstants._ID,
                            DbConstants.TITLE, DbConstants.OVERVIEW, DbConstants.URLIMAGE,
                            DbConstants.RATING, DbConstants.WATCHED}, DbConstants._ID + "=?",//, DbConstants.XINDEX
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            boolean watched = false;
            if (Integer.parseInt(cursor.getString(5)) != 0) {
                watched = true;
            }
            Movie movie = new Movie(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    , Float.parseFloat(cursor.getString(4)), watched);
            return movie;
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        } finally {
            db.close();
        }

    }

    public boolean isSameMovie(String name, String overView) throws Exception {
        SQLiteDatabase db = null;

        try {
            db = dbCUD.getReadableDatabase();
            String[] addrWhereParams = new String[]{name, overView};
            Cursor cursor = db.query(DbConstants.TABLE_MOVIES, null, DbConstants.TITLE +
                            "=?" + " AND " + DbConstants.OVERVIEW + "=?",//, DbConstants.XINDEX
                    addrWhereParams, null, null, null, null);

            if (cursor.moveToFirst()) {
                cursor.close();
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        }
        finally {
            db.close();
        }
        return false;
    }

    public List<Movie> getAllMovies() throws Exception {
        List<Movie> moviesList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DbConstants.TABLE_MOVIES;

        SQLiteDatabase db = null;
        int watched = 0; //SQLite has no boolean definition
        try {
            db = dbCUD.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setId(Integer.parseInt(cursor.getString(0)));
                    movie.setTitle(cursor.getString(1));
                    movie.setOverView(cursor.getString(2));
                    movie.setUrlImage(cursor.getString(3));
                    movie.setRating(Float.parseFloat(cursor.getString(4)));
                    if (Integer.parseInt(cursor.getString(5)) == 0) {
                        movie.setWatched(false);
                    } else {
                        movie.setWatched(true);
                    }

                    // Adding movie to list
                    moviesList.add(movie);
                } while (cursor.moveToNext());
            }

            // return contact list
            return moviesList;
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        }

        finally {
            db.close();
        }
    }


    // code to update the single movie
    public int updateMovie(Movie movie) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = dbCUD.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DbConstants.TITLE, movie.getTitle());
            values.put(DbConstants.OVERVIEW, movie.getOverView());
            values.put(DbConstants.URLIMAGE, movie.getUrlImage());
            values.put(DbConstants.RATING, movie.getRating());

            // updating row
            int watched = 0; //SQLite has no boolean definition
            if (movie.isWatched()) {
                watched = 1;
            }
            values.put(DbConstants.WATCHED, watched);
            return db.update(DbConstants.TABLE_MOVIES, values, DbConstants._ID + " = ?",
                    new String[]{String.valueOf(movie.getId())});
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        }
        finally {
            db.close();
        }
    }

    // Deleting single contact
    public void deleteMovie(Movie movie) throws Exception {
        SQLiteDatabase db = null;
        try {
            db = dbCUD.getWritableDatabase();
            db.delete(DbConstants.TABLE_MOVIES, DbConstants._ID + " = ?",
                    new String[]{String.valueOf(movie.getId())});
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        }

        finally {
            db.close();
        }
    }

    public void deleteAll() throws Exception {
        SQLiteDatabase db = null;
        try {
            db = dbCUD.getWritableDatabase();
            db.delete(DbConstants.TABLE_MOVIES, null,
                    new String[]{});
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        }
        finally {
            db.close();
        }
    }

    // Getting contacts Count
    public int getMoviesCount() throws Exception {
        String countQuery = "SELECT  * FROM " + DbConstants.TABLE_MOVIES;
        SQLiteDatabase db = null;

        try {
            db = dbCUD.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();
            // return count
            return cursor.getCount();
        } catch (Exception e) {
            Log.e("Notice!", e.getMessage());
            throw new Exception(e);
        } finally {
            db.close();
        }

    }
}

