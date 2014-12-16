// AddEditContact.java
// Activity for adding a new entry to or  
// editing an existing entry in the address book.
package com.ozi.movieinformer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ozi.addressbook.R;

public class AddEditMovie extends Activity
{
   private long rowID;
   

   private EditText titleEditText;
   private EditText yearEditText;
   private EditText directorEditText;


   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.add_movie);

      titleEditText = (EditText) findViewById(R.id.titleEditText);
      directorEditText = (EditText) findViewById(R.id.directorEditText);
      yearEditText = (EditText) findViewById(R.id.yearEditText);

      
      Bundle extras = getIntent().getExtras();


      if (extras != null)
      {
         rowID = extras.getLong("row_id");
         titleEditText.setText(extras.getString("title"));
         directorEditText.setText(extras.getString("director"));
         yearEditText.setText(extras.getString("year"));

      }
      Button saveMovieButton =
         (Button) findViewById(R.id.saveMovieButton);
      saveMovieButton.setOnClickListener(saveMovieButtonClicked);
   }


   OnClickListener saveMovieButtonClicked = new OnClickListener()
   {
      @Override
      public void onClick(View v) 
      {
         if (titleEditText.getText().length() != 0)
         {
            AsyncTask<Object, Object, Object> saveMovieTask =
               new AsyncTask<Object, Object, Object>() 
               {
                  @Override
                  protected Object doInBackground(Object... params) 
                  {
                     saveMovie();
                      Intent i = new Intent(getApplicationContext(),MovieBook.class);
                      startActivity(i);
                     return null;
                  }
      
                  @Override
                  protected void onPostExecute(Object result) 
                  {
                     finish();
                  }
               };
               

            saveMovieTask.execute((Object[]) null);

         }
         else
         {

            AlertDialog.Builder builder = 
               new AlertDialog.Builder(AddEditMovie.this);
      

            builder.setTitle(R.string.errorTitle); 
            builder.setMessage(R.string.errorMessage);
            builder.setPositiveButton(R.string.errorButton, null); 
            builder.show();
         }
      } // end method onClick
   };


   private void saveMovie()
   {

      DatabaseConnector databaseConnector = new DatabaseConnector(this);

      if (getIntent().getExtras() == null)
      {

         databaseConnector.insertMovie(
                 titleEditText.getText().toString(),
                 directorEditText.getText().toString(),
                 yearEditText.getText().toString());
      }
      else
      {
         databaseConnector.updateMovie(rowID,
                 titleEditText.getText().toString(),
                 directorEditText.getText().toString(),
                 yearEditText.getText().toString());
      }
   }
}

