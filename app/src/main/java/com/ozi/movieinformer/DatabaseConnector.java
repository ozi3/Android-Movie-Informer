// DatabaseConnector.java
// Provides easy connection and creation of UserContacts database.
package com.ozi.movieinformer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseConnector 
{
   private static final String DATABASE_NAME = "MovieDB";
   private SQLiteDatabase database;
   private DatabaseOpenHelper databaseOpenHelper;
   public DatabaseConnector(Context context) 
   {

      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   }
   public void open() throws SQLException 
   {

      database = databaseOpenHelper.getWritableDatabase();
   }
   public void close() 
   {
      if (database != null)
         database.close();
   }
   public void insertMovie(String title, String director, String year)
   {
      ContentValues newMovie = new ContentValues();
      newMovie.put("title", title);
      newMovie.put("director", director);
      newMovie.put("year", year);

      open();
      database.insert("movies", null, newMovie);
      close();
   }
   public void updateMovie(long id, String name, String email,
                           String phone)
   {
      ContentValues editMovie = new ContentValues();
      editMovie.put("title", name);
      editMovie.put("director", email);
      editMovie.put("year", phone);


      open();
      database.update("movies", editMovie, "_id=" + id, null);
      close();
   }
   public Cursor getAllMovies()
   {
      return database.query("movies", new String[] {"_id", "title"},
         null, null, null, null, "title");
   }
   public Cursor getOneMovie(long id)
   {
      return database.query(
         "movies", null, "_id=" + id, null, null, null, null);
   }
   public void deleteMovies(long id)
   {
      open();
      database.delete("movies", "_id=" + id, null);
      close();
   }
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {

      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      }
      @Override
      public void onCreate(SQLiteDatabase db) 
      {

         String createQuery = "CREATE TABLE movies" +
            "(_id integer primary key autoincrement," +
            "title TEXT, director TEXT, year TEXT);";
                  
         db.execSQL(createQuery);
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      }
   }
}