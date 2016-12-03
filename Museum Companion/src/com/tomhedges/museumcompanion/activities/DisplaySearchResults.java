package com.tomhedges.museumcompanion.activities;

import java.util.Observable;
import java.util.Observer;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.model.ItemObject;
import com.tomhedges.museumcompanion.model.ItemObjectBuilder;
import com.tomhedges.museumcompanion.model.SearchResults;
import com.tomhedges.museumcompanion.utilities.ArrayAdapterSearchResults;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class DisplaySearchResults extends Activity implements OnClickListener, OnItemClickListener, Observer {

	private SearchResults srResults;
	
	private ListView lvSearchResults;
	private TextView tvSearchResultSummary;
	private Button btnEarlier;
	private Button btnLater;

	private ItemObjectBuilder ioBuilder;
	
	private Toast displayMessage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_search_results);
		
		//srResults = getIntent().getParcelableExtra(Constants.SEARCH_RESULTS);
		srResults = srResults.load(this);
		
		ioBuilder = ItemObjectBuilder.getInstance(this);
		ioBuilder.addObserver(this);
		
		
		lvSearchResults = (ListView) findViewById(R.id.search_results_listing);
		tvSearchResultSummary = (TextView) findViewById(R.id.tvSearchResultSummary);
		btnEarlier = (Button) findViewById(R.id.btnEarlier);
		btnLater = (Button) findViewById(R.id.btnLater);
		
		refreshResultsDisplay();
		refreshButtonStatus();
		
		btnEarlier.setOnClickListener(this);
		btnLater.setOnClickListener(this);
	}
	
	public void refreshResultsDisplay() {
		ArrayAdapterSearchResults adapterSearchResults = new ArrayAdapterSearchResults(this, R.layout.list_search_results, srResults.getResultsURLs(), srResults);
		lvSearchResults.setAdapter(adapterSearchResults);
		lvSearchResults.setOnItemClickListener(this);
		tvSearchResultSummary.setText("Showing " + srResults.getStartPointer() + " to " + srResults.getEndPointer() + " of " + srResults.getTotalResults() + " results");
	}
	
	public void refreshButtonStatus() {
		if (srResults.getStartPointer() == 1) {
			btnEarlier.setEnabled(false);
		} else {
			btnEarlier.setEnabled(true);
		}
		
		if (srResults.getTotalResults()<=Constants.SEARCH_RESULTS_GROUPING || srResults.getEndPointer() == srResults.getTotalResults()) {
			btnLater.setEnabled(false);
		} else {
			btnLater.setEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnEarlier:
			srResults.loadEarlier();
			refreshResultsDisplay();
			refreshButtonStatus();
			break;
		
		case R.id.btnLater:
			srResults.loadLater();
			refreshResultsDisplay();
			refreshButtonStatus();
			break;
		
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//Toast.makeText(this, "ID for item=" + srResults.getResultObjectID(position), Toast.LENGTH_SHORT).show();

		lvSearchResults.setEnabled(false);
		displayMessage = Toast.makeText(this, "Loading artefact... please wait...", Toast.LENGTH_SHORT);
		displayMessage.show();
		ioBuilder.getObject(srResults.getDataSource(position), srResults.getResultObjectID(position));
	}

	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof ItemObjectBuilder) {
			Log.d(SearchCollections.class.getName(), "Data is: " + data);

			if (data == null) {
				Toast.makeText(this, "Sorry - Artefact could not be loaded! Please try again...", Toast.LENGTH_LONG).show();
				lvSearchResults.setEnabled(true);
			} else {
				displayMessage.cancel();
				Log.d(SearchCollections.class.getName(), "Creating object display intent for object...");
				Intent intent = new Intent(this, DisplayItem.class);
				intent.putExtra(Constants.ITEM_OBJECT, (ItemObject) data);
				Log.d(SearchCollections.class.getName(), "Starting object display intent for object...");
				startActivity(intent);
				lvSearchResults.setEnabled(true);
			}
		}
	}
}
