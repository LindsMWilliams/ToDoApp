package com.lindsaymwilliams.todoapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    protected String flNm_list;
    private final int REQUEST_CODE;

    public MainActivity() {
        super();
        REQUEST_CODE = 1010;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        flNm_list = getString(R.string.list_file_name);
        
        //Create a list and add it to the view
        lvItems = (ListView) findViewById(R.id.lvItems);
        //items = new ArrayList<String>();
        items = readItems(flNm_list);
        //set up the adapter to read the list into the view
        itemsAdapter 
                = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add(getString(R.string.FirstItem));
        //items.add(getString(R.string.SecondItem));
        setupListViewListener();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    
    //For the view's add an item button
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.getText().clear();
        etNewItem.setHint(getString(R.string.AddItemHereString));
        writeItems(flNm_list, items);
    }

    //adds a listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
                    {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems(flNm_list, items);
                        return true;
                    }
                }

        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                        //create an intent
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        //add extras or information to the intent
                        i.putExtra(getString(R.string.itemPositionTxt), pos);
                        i.putExtra(getString(R.string.listItemTxt), items.get(pos));
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //check that the result was ok and that we have the correct request code
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //get the result data
            String listItemValue = data.getExtras().getString(getString(R.string.listItemTxt));
            int listItemPos = data.getExtras().getInt(getString(R.string.itemPositionTxt));
            
            //update the list item with the returned text
            if(null != items && !items.isEmpty()) {
                items.remove(listItemPos);
                items.add(listItemPos, listItemValue);
                itemsAdapter.notifyDataSetChanged();
            }else {
                items = new ArrayList<>();
                items.add(listItemValue);
            }
            writeItems(flNm_list, items);
        }
        
    }
    
    private ArrayList<String> readItems(String flNm){
        File filesDir = getFilesDir();
        File toDoFile = new File(filesDir, flNm);
        
        try{
            return new ArrayList<>(FileUtils.readLines(toDoFile));
        } catch (IOException e){
            return new ArrayList<>();
        }
    }
    
    private void writeItems(String flNm, ArrayList<String> items){
        File filesDir = getFilesDir();
        File toDoFile = new File(filesDir, flNm);
        
        try{
            FileUtils.writeLines(toDoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
