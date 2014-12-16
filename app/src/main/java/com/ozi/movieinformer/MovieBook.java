// AddressBook.java
// Main activity for the Address Book app.
package com.ozi.movieinformer;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ozi.addressbook.R;

public class MovieBook extends ListActivity
{
   public static final String ROW_ID = "row_id";
   private ListView movieListView;
   private CursorAdapter movieAdapter;
   

   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      movieListView = getListView();
      movieListView.setOnItemClickListener(viewMovieListener);


      String[] from = new String[] { "title" };
      int[] to = new int[] { R.id.movieTextView };
      movieAdapter = new SimpleCursorAdapter(
         MovieBook.this, R.layout.movie_list_item, null, from, to);
      setListAdapter(movieAdapter);
   }


   @Override
   protected void onResume() 
   {
      super.onResume();
       new GetMoviesTask().execute((Object[]) null);
    }

   @Override
   protected void onStop() 
   {
      Cursor cursor = movieAdapter.getCursor();
      
      if (cursor != null) 
         cursor.deactivate();
      
      movieAdapter.changeCursor(null);
      super.onStop();
   }
   private class GetMoviesTask extends AsyncTask<Object, Object, Cursor>
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(MovieBook.this);

      @Override
      protected Cursor doInBackground(Object... params)
      {
         databaseConnector.open();

         return databaseConnector.getAllMovies();
      }
      @Override
      protected void onPostExecute(Cursor result)
      {
         movieAdapter.changeCursor(result);
         databaseConnector.close();
      }
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.moviesbook_menu, menu);
      return true;
   }
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {

      Intent addNewMovie =
         new Intent(MovieBook.this, AddEditMovie.class);
      startActivity(addNewMovie);
      return super.onOptionsItemSelected(item);
   }
   OnItemClickListener viewMovieListener = new OnItemClickListener()
   {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
         long arg3) 
      {

         Intent viewMovie =
            new Intent(MovieBook.this, ViewMovie.class);
         

         viewMovie.putExtra(ROW_ID, arg3);
         startActivity(viewMovie);
      } // end method onItemClick
   };
}
