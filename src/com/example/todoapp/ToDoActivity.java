package com.example.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ToDoActivity extends Activity {
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		lvItems = (ListView) findViewById(R.id.lvItems);
		readItems();  //populateArrayItems();
		
		todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);  //'this' == getBaseContext()
		lvItems.setAdapter(todoAdapter);
		//todoAdapter.add("Item 4");   --> can directly add to the adapter as well, it acts as a proxy to your item-list
		
		setupListViewListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}

	private void populateArrayItems(){
		todoItems = new ArrayList<String>();
		todoItems.add("Item 1");
		todoItems.add("Item 2");
		todoItems.add("Item 3");
	}
	
	public void onAddedItem(View v){
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();
	}
	
	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				todoItems.remove(pos);                   //whenever the underlying data is changed, the adapter also needs to be notified
				todoAdapter.notifyDataSetChanged(); 	 //Or, you can directly change the data in the adapter, in this you would need just one line instead of two
				writeItems();
				
				return true;
			}
		});
	}
	
	//------------------------------------------------------
	//File persistence of our to-do list app ---- more complex apps can use databases
	//require the common-io jar, for File APIs
	//------------------------------------------------------
	
	private void readItems(){
		File fileDir = getFilesDir();  //gives the absolute path to the android app location ON THE PHONE [can see it in the 'file explorer' in the DDMS
		File todoFile = new File(fileDir, "todo.txt");
		
		try{
			todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e){
			//if the file does not exist (yet), create a new data structure
			todoItems = new ArrayList<String>();
		}
	}
	
	private void writeItems(){
		File fileDir = getFilesDir();
		File todoFile = new File(fileDir, "todo.txt");
		
		try{
			FileUtils.writeLines(todoFile, todoItems);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
}
