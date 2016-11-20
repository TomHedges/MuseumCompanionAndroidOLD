package com.tomhedges.museumcompanion.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.model.ItemObject;
import com.tomhedges.museumcompanion.model.ItemObjectBuilder;
import com.tomhedges.museumcompanion.model.SearchResultsBuilder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchCollections extends Activity implements OnClickListener, OnItemSelectedListener, Observer {

	private Intent intent;

	private ItemObjectBuilder ioBuilder;
	private SearchResultsBuilder srBuilder;

	private DataSource dsDataSource;

	private EditText etObjectID;
	private EditText etObjectName;
	private ProgressBar mProgress;
	private Button btnFind;
	private Button btnSearchByName;
	private Spinner spMuseum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_collections);

		ioBuilder = ItemObjectBuilder.getInstance(this);
		ioBuilder.addObserver(this);
		srBuilder = SearchResultsBuilder.getInstance(this);
		srBuilder.addObserver(this);

		etObjectID = (EditText) findViewById(R.id.etObjectID);
		etObjectName = (EditText) findViewById(R.id.etObjectName);
		mProgress = (ProgressBar) findViewById(R.id.pbLoad);
		btnFind = (Button) findViewById(R.id.btnFind);
		btnSearchByName = (Button) findViewById(R.id.btnSearchByName);
		spMuseum = (Spinner) findViewById(R.id.spMuseum);

		etObjectID.setText("O34256");
		etObjectName.setText("statue");
		
		btnFind.setOnClickListener(this);
		btnSearchByName.setOnClickListener(this);
		spMuseum.setOnItemSelectedListener(this);

		List<String> spinnerArray =  new ArrayList<String>();
		for (String museum : Constants.DataSourceLabel) {
			spinnerArray.add(museum);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, spinnerArray);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spMuseum.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnFind:
			mProgress.setVisibility(View.VISIBLE);
			if (Constants.TEST_DATA_FORCE_LOAD) {
				// FORCE FAKE DATA
				ioBuilder.getObject(DataSource.TEST_DATA_SOURCE, "12345");
			} else {
				ioBuilder.getObject(dsDataSource, etObjectID.getText().toString());
			}
			break;

		case R.id.btnSearchByName:
			mProgress.setVisibility(View.VISIBLE);
			srBuilder.getSearchResultsByName(dsDataSource, etObjectName.getText().toString());
			break;

		default:
			break;
		}

		hideSoftKeyboard(this, v);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
		switch (parent.getId()) {

		case R.id.spMuseum:
			//Toast.makeText(this, "Museum item selected - pos: " + pos + ", id: " + id, Toast.LENGTH_SHORT).show();

			dsDataSource = Constants.DataSourceList[pos];

			//			switch (pos) {
			//			case 0:
			//				dsDataSource = DataSource.BRITISH_MUSEUM;
			//				break;
			//
			//			case 1:
			//				dsDataSource = DataSource.VICTORIA_AND_ALBERT_MUSEUM;
			//				break;
			//
			//			default:
			//				break;
			//			}
			//
			//			break;

		default:
			break;
		}

		hideSoftKeyboard(this, v);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	public static void hideSoftKeyboard (Activity activity, View view) 
	{
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	@Override
	public void update(Observable observable, Object data) {
		// Receiving downloaded object data!

		Log.d(SearchCollections.class.getName(), "Data received: " + observable.getClass());
		
		if (observable instanceof ItemObjectBuilder) {
			Log.d(SearchCollections.class.getName(), "Data is: " + data);

			if (data == null) {
				mProgress.setVisibility(View.INVISIBLE);
				Toast.makeText(this, "Sorry - no data returned!", Toast.LENGTH_SHORT).show();
			} else {
				ItemObject ioTest;
				ioTest = (ItemObject) data;
				Log.d(SearchCollections.class.getName(), "Object ID: " + ioTest.getObjectID());
				Log.d(SearchCollections.class.getName(), "Creating object display intent for object...");
				intent = new Intent(this, DisplayItem.class);
				intent.putExtra(Constants.ITEM_OBJECT, ioTest);
				Log.d(SearchCollections.class.getName(), "Starting object display intent for object...");
				startActivity(intent);
				mProgress.setVisibility(View.INVISIBLE);
			}
		}

		if (observable instanceof SearchResultsBuilder) {
			if (data == null) {
				mProgress.setVisibility(View.INVISIBLE);
				Toast.makeText(this, "Sorry - no results returned!", Toast.LENGTH_SHORT).show();
			} else {
				//TEST DISPLAY CUNT OF RESULTS
				//SearchResults srResults;
				//srResults = (SearchResults) data;
				//Toast.makeText(this, "Returned: " + srResults.getTotalResults() + " results!", Toast.LENGTH_SHORT).show();
				
				intent = new Intent(this, DisplaySearchResults.class);
				//intent.putExtra(Constants.SEARCH_RESULTS, (SearchResults) data);
				Log.d(SearchCollections.class.getName(), "Starting search results intent...");
				startActivity(intent);
				
				mProgress.setVisibility(View.INVISIBLE);
			}
		}
	}
}
