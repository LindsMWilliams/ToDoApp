package com.lindsaymwilliams.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditItemActivity extends ActionBarActivity {

    int position;
    String itemValue;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        
        //grab intent information
        position = getIntent().getIntExtra(getString(R.string.itemPositionTxt),0);
        itemValue = getIntent().getStringExtra(getString(R.string.listItemTxt));
        
        //place the item value in the edit text box
        EditText editText = (EditText) findViewById(R.id.editItemText);
        editText.append(itemValue);
        editText.setCursorVisible(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onSave(View v){
        //Grab the text that needs to be saved
        EditText editText = (EditText) findViewById(R.id.editItemText);
        //create a data intent to pass back to the parent
        Intent data = new Intent();
        data.putExtra(getString(R.string.itemPositionTxt),position);
        data.putExtra(getString(R.string.listItemTxt), editText.getText().toString());
        //set the result for the activity
        setResult(RESULT_OK,data);
        //close the activity
        this.finish();
    }
}
