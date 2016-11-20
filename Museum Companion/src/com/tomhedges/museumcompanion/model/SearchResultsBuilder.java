package com.tomhedges.museumcompanion.model;

import java.util.Observable;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.dao.RemoteDataAccess;
import com.tomhedges.museumcompanion.model.institution_mappings.BritishMuseumBuilder;
import com.tomhedges.museumcompanion.model.institution_mappings.VictoriaAndAlbertBuilder;

public class SearchResultsBuilder extends Observable {

	private static SearchResultsBuilder instance = null;
	private Context context;

	private String results = null;
	private DataSource dsDataSource;

	public static SearchResultsBuilder getInstance(Context context) {
		if(instance == null) {
			instance = new SearchResultsBuilder(context);
		}
		return instance;
	}

	private SearchResultsBuilder(Context context) {
		this.context = context;
	}

	public void getSearchResultsByName(DataSource dsDataSource, String strName) {

		this.dsDataSource = dsDataSource;

		new SearchForResultsByName().execute(strName);
	}

	private class SearchForResultsByName extends AsyncTask<String, Void, Boolean> {
		private RemoteDataAccess remoteDataAccess;
		private SearchResults srResults;

		protected void onPreExecute() {
			super.onPreExecute();
			remoteDataAccess = new RemoteDataAccess();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String strSearchResults = remoteDataAccess.getSearchResultsByName(dsDataSource, params[0]);

			//publishProgress("blah");
			if (strSearchResults.equals(Constants.ERROR_CODE)) {
				Log.e(SearchForResultsByName.class.getName(), "Failed to retrieve result...");
				return false;
			} else {
				Log.d(SearchForResultsByName.class.getName(), "Successfully retrieved result!");
				srResults = buildSearchResults(strSearchResults);
				if (srResults != null) {
					return true;
				} else {
					return false;
				}
			}
		}

		protected void onProgressUpdate(Void... progress) {
			super.onProgressUpdate(progress);
		}

		protected void onPostExecute(Boolean succcessful) {
			super.onPostExecute(succcessful);

			if (succcessful) {
				Log.d(SearchResultsBuilder.class.getName(), "Passing back Search Results, total results: " + srResults.getTotalResults());
				SendUpdate(srResults);
			} else {
				Log.e(SearchResultsBuilder.class.getName(), "Exiting with null data");
				SendUpdate(null);
			}
		}
	}

	//TODO: DEPENDENCY INJECTION
	private SearchResults buildSearchResults(String strResultsData) {
		SearchResults srResults = null;

		switch (dsDataSource) {

		case BRITISH_MUSEUM:
			srResults = BritishMuseumBuilder.BuildSearchResults(strResultsData);
			break;

		case VICTORIA_AND_ALBERT_MUSEUM:
			srResults = VictoriaAndAlbertBuilder.BuildSearchResults(strResultsData);
			break;

		case TEST_DATA_SOURCE:
			//implement test data??
			break;

		default:
			break;
		}
		
		srResults.save(context, System.currentTimeMillis() + "");
		
		return srResults;
	}

	private void SendUpdate(Object objectToSend) {
		setChanged();
		notifyObservers(objectToSend);
	}
}
