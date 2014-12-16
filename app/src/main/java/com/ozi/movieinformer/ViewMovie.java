// ViewContact.java
// Activity for viewing a single contact.
package com.ozi.movieinformer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ozi.addressbook.R;

public class ViewMovie extends Activity
{
   private long rowID;
   private TextView titleTextView;
   private TextView yearTextView;
   private TextView directorTextView;
   private Button trailerButton;
   private Button updateButton;



   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.view_movie);


      titleTextView = (TextView) findViewById(R.id.titleTextView);
      yearTextView = (TextView) findViewById(R.id.yearTextView);
      directorTextView = (TextView) findViewById(R.id.directorTextView);
      trailerButton = (Button) findViewById(R.id.trailerButton);
      updateButton = (Button) findViewById(R.id.updateButton);

      Bundle extras = getIntent().getExtras();
      rowID = extras.getLong(MovieBook.ROW_ID);


      trailerButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/results?search_query=" + titleTextView.getText().toString()));
              startActivity(browserIntent);
          }

      });
       updateButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent updateIntent = new Intent(getApplicationContext(), AddEditMovie.class);
               updateIntent.putExtra("row_id", rowID);
               updateIntent.putExtra("title", titleTextView.getText().toString());
               updateIntent.putExtra("year", yearTextView.getText().toString());
               updateIntent.putExtra("director", directorTextView.getText().toString());
               startActivity(updateIntent);


           }
       });
   }
   @Override
   protected void onResume()
   {
      super.onResume();
      

      new LoadMovieTask().execute(rowID);
   }
   private class LoadMovieTask extends AsyncTask<Long, Object, Cursor>
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(ViewMovie.this);


      @Override
      protected Cursor doInBackground(Long... params)
      {
         databaseConnector.open();

         return databaseConnector.getOneMovie(params[0]);
      }
      @Override
      protected void onPostExecute(Cursor result)
      {
         super.onPostExecute(result);
   
         result.moveToFirst();

         int titleIndex = result.getColumnIndex("title");
         int yearIndex = result.getColumnIndex("year");
         int directorIndex = result.getColumnIndex("director");

   

         titleTextView.setText(result.getString(titleIndex));
         yearTextView.setText(result.getString(yearIndex));
         directorTextView.setText(result.getString(directorIndex));

   
         result.close();
         databaseConnector.close();
      }
   }
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.view_movie_menu, menu);
      return true;
   }
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId())
      {
         case R.id.editItem:

            Intent addEditMovie =
               new Intent(this, AddEditMovie.class);
            

            addEditMovie.putExtra(MovieBook.ROW_ID, rowID);
            addEditMovie.putExtra("title", titleTextView.getText());
            addEditMovie.putExtra("year", yearTextView.getText());
            addEditMovie.putExtra("director", directorTextView.getText());
            startActivity(addEditMovie);
            return true;
         case R.id.deleteItem:
            deleteMovie();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }
   private void deleteMovie()
   {

      AlertDialog.Builder builder = 
         new AlertDialog.Builder(ViewMovie.this);

      builder.setTitle(R.string.confirmTitle);
      builder.setMessage(R.string.confirmMessage);


      builder.setPositiveButton(R.string.button_delete,
         new DialogInterface.OnClickListener()
         {
            @Override
            public void onClick(DialogInterface dialog, int button)
            {
               final DatabaseConnector databaseConnector = 
                  new DatabaseConnector(ViewMovie.this);


               AsyncTask<Long, Object, Object> deleteTask =
                  new AsyncTask<Long, Object, Object>()
                  {
                     @Override
                     protected Object doInBackground(Long... params)
                     {
                        databaseConnector.deleteMovies(params[0]);
                        return null;
                     }

                     @Override
                     protected void onPostExecute(Object result)
                     {
                        finish();
                     }
                  };


               deleteTask.execute(new Long[] { rowID });               
            } // end method onClick
         }
      );
      
      builder.setNegativeButton(R.string.button_cancel, null);
      builder.show();
   }
}


