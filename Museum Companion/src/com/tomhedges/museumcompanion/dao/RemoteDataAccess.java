package com.tomhedges.museumcompanion.dao;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.util.Log;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.utilities.HttpRetriever;

/**
 * Uses the HttpRetriever class to retrieve data form the remote database, and then construct objects for return to the Game.
 * 
 * Incorporates code sourced from:  http://www.mybringback.com/tutorial-series/12924/android-tutorial-using-remote-databases-php-and-mysql-part-1/
 * 
 * @see			Game
 * @author      Tom Hedges
 */

public class RemoteDataAccess {

	// JSON parser class
	private HttpRetriever httpRetriever;
	//private String[][] results;

	//An array of all our data
	//private JSONArray jaData = null;

	// Storage for user preferences
	//private CoreSettings coreSettings;

	//public RemoteDataAccess() {
	//	coreSettings = CoreSettings.accessCoreSettings();
	//}

	//public BritishMuseumBuilder getObjectData(DataSource dsDataSource, String strObjectID) {
	public String getObjectData(DataSource dsDataSource, String strObjectID) {
		// Check for success tag
		//int success;

		//BritishMuseumBuilder ioItemToReturn;
		String returnData = null;
		httpRetriever = new HttpRetriever();

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			//params.add(new BasicNameValuePair(Constants.TABLE_NAME_VARIABLE, Constants.TABLE_NAME_GLOBAL_SETTINGS));

			//Log.d(RemoteDataAccess.class.getName(), "Attempting retrieval of data from: " + Constants.TABLE_NAME_GLOBAL_SETTINGS);
			// getting product details by making HTTP request

			//TODO: Extract this to some sort of dependency injection??
			
			switch (dsDataSource) {
			case BRITISH_MUSEUM:
				returnData = httpRetriever.makeHttpRequest("http://collection.britishmuseum.org/id/object/" + strObjectID + "&format=json", "GET", params);
				break;

			case VICTORIA_AND_ALBERT_MUSEUM:
				returnData = httpRetriever.makeHttpRequest("http://www.vam.ac.uk/api/json/museumobject/" + strObjectID, "GET", params);
				break;

			case TEST_DATA_SOURCE:
				returnData = Constants.TEST_DATA;
				break;

			default:
				break;
			}

			if (!returnData.equals(Constants.ERROR_CODE)) {
				int iDataLength = 200;
				if (returnData.length()<200) {iDataLength = returnData.length();}
				Log.d(RemoteDataAccess.class.getName(), "Request results sample: " + returnData.substring(0, iDataLength));
				//ioItemToReturn = new BritishMuseumBuilder(dsDataSource, strObjectID, returnData);
			} else {
				Log.e(RemoteDataAccess.class.getName(), "No result to return");
				return Constants.ERROR_CODE;
			}

			return returnData;
		} catch (Error e) {
			e.printStackTrace();
			return Constants.ERROR_CODE;
		}

	}

	public String getSearchResultsByLabelRef(DataSource dsDataSource, String strLabelRef) {
		String returnData = null;
		httpRetriever = new HttpRetriever();

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			Log.d(RemoteDataAccess.class.getName(), "Attempting a search for: '" + strLabelRef + "' in collection of " + dsDataSource);
			// getting product details by making HTTP request

			String strSearchDetail = null;
			//TODO: Extract this to some sort of dependency injection??
			switch (dsDataSource) {
			case BRITISH_MUSEUM:
				//String strSearchDetail = "PREFIX crm: <http://erlangen-crm.org/current/> PREFIX fts: <http://www.ontotext.com/owlim/fts#>"
				//	+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?obj ?label WHERE { ?obj crm:P102_has_title ?title ."
				//	+ "?title rdfs:label ?label .   FILTER(CONTAINS(?label,\"" + strNameComponent + "\")) }";// LIMIT 100";

				
				strSearchDetail = "PREFIX crm: <http://erlangen-crm.org/current/>" + 
				"PREFIX bmo: <http://collection.britishmuseum.org/id/ontology/>" +
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + 
				"SELECT DISTINCT ?obj (group_concat(?label;separator=\", \") as ?label) " + 
				"where {" + 
				"?obj crm:P102_has_title ?title . " + 
				"?title rdfs:label ?label ." +
				"?obj bmo:PX_object_exhibition_label ?exhib_label . " +
				"FILTER regex(?exhib_label, \"" + strLabelRef + "\", \"i\")" +
				"} GROUP BY ?obj " +
				"LIMIT " + Constants.SEARCH_RESULTS_REQUESTED;

				params.add(new BasicNameValuePair("query", strSearchDetail));
				params.add(new BasicNameValuePair("_equivalent", "false"));
				params.add(new BasicNameValuePair("equivalent", "false"));
				params.add(new BasicNameValuePair("_implicit", "false"));
				params.add(new BasicNameValuePair("implicit", "false"));
				returnData = httpRetriever.makeHttpRequest(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_SEARCH_ROOT, "GET", params);
				Log.d(RemoteDataAccess.class.getName(), "Results returned");
				break;

			case VICTORIA_AND_ALBERT_MUSEUM:
				// TODO: IMPLEMENT!
				
				strSearchDetail = strLabelRef;

				params.add(new BasicNameValuePair("q", strSearchDetail));
				params.add(new BasicNameValuePair("limit", "45"));
				returnData = httpRetriever.makeHttpRequest(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_SEARCH_ROOT, "GET", params);
				Log.d(RemoteDataAccess.class.getName(), "Results returned");
				break;

			case TEST_DATA_SOURCE:
				returnData = Constants.TEST_DATA;
				break;

			default:
				break;
			}

			if (!returnData.equals(Constants.ERROR_CODE)) {
				int iDataLength = 200;
				if (returnData.length()<200) {iDataLength = returnData.length();}
				Log.d(RemoteDataAccess.class.getName(), "Request results sample: " + returnData.substring(0, iDataLength));
				//ioItemToReturn = new BritishMuseumBuilder(dsDataSource, strObjectID, returnData);
			} else {
				Log.e(RemoteDataAccess.class.getName(), "No result to return");
				return Constants.ERROR_CODE;
			}

			return returnData;
		} catch (Error e) {
			e.printStackTrace();
			return Constants.ERROR_CODE;
		}
	}

	public String getSearchResultsByName(DataSource dsDataSource, String strNameComponent) {
		String returnData = null;
		httpRetriever = new HttpRetriever();

		try {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			Log.d(RemoteDataAccess.class.getName(), "Attempting a search for: '" + strNameComponent + "' in collection of " + dsDataSource);
			// getting product details by making HTTP request

			String strSearchDetail = null;
			//TODO: Extract this to some sort of dependency injection??
			switch (dsDataSource) {
			case BRITISH_MUSEUM:
				//String strSearchDetail = "PREFIX crm: <http://erlangen-crm.org/current/> PREFIX fts: <http://www.ontotext.com/owlim/fts#>"
				//	+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> SELECT DISTINCT ?obj ?label WHERE { ?obj crm:P102_has_title ?title ."
				//	+ "?title rdfs:label ?label .   FILTER(CONTAINS(?label,\"" + strNameComponent + "\")) }";// LIMIT 100";

				strSearchDetail = "PREFIX crm: <http://erlangen-crm.org/current/>" + 
				"PREFIX fts: <http://www.ontotext.com/owlim/fts#>" + 
				"PREFIX bmo: <http://collection.britishmuseum.org/id/ontology/>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + 
				"SELECT DISTINCT ?obj ?label " + 
				"where {" + 
				"{?obj crm:P102_has_title ?title . " + 
				"?title rdfs:label ?label ." + 
				"FILTER regex(?label, \"" + strNameComponent + "\", \"i\" )}" + 
				"UNION" + 
				"{?obj bmo:PX_curatorial_comment ?curat_comm ." + 
				"FILTER regex(?curat_comm, \"" + strNameComponent + "\", \"i\" )}" + 
				"UNION" + 
				"{?obj  bmo:PX_physical_description ?phys_desc ." + 
				"FILTER regex(?phys_desc, \"" + strNameComponent + "\", \"i\" )}" + 
				"} LIMIT " + Constants.SEARCH_RESULTS_REQUESTED;
				//"}";

				params.add(new BasicNameValuePair("query", strSearchDetail));
				params.add(new BasicNameValuePair("_equivalent", "false"));
				params.add(new BasicNameValuePair("equivalent", "false"));
				params.add(new BasicNameValuePair("_implicit", "false"));
				params.add(new BasicNameValuePair("implicit", "false"));
				returnData = httpRetriever.makeHttpRequest(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_SEARCH_ROOT, "GET", params);
				Log.d(RemoteDataAccess.class.getName(), "Results returned");
				break;

			case VICTORIA_AND_ALBERT_MUSEUM:
				// TODO: IMPLEMENT!
				
				strSearchDetail = strNameComponent;

				params.add(new BasicNameValuePair("q", strSearchDetail));
				params.add(new BasicNameValuePair("limit", "45"));
				returnData = httpRetriever.makeHttpRequest(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_SEARCH_ROOT, "GET", params);
				Log.d(RemoteDataAccess.class.getName(), "Results returned");
				break;

			case TEST_DATA_SOURCE:
				returnData = Constants.TEST_DATA;
				break;

			default:
				break;
			}

			if (!returnData.equals(Constants.ERROR_CODE)) {
				int iDataLength = 200;
				if (returnData.length()<200) {iDataLength = returnData.length();}
				Log.d(RemoteDataAccess.class.getName(), "Request results sample: " + returnData.substring(0, iDataLength));
				//ioItemToReturn = new BritishMuseumBuilder(dsDataSource, strObjectID, returnData);
			} else {
				Log.e(RemoteDataAccess.class.getName(), "No result to return");
				return Constants.ERROR_CODE;
			}

			return returnData;
		} catch (Error e) {
			e.printStackTrace();
			return Constants.ERROR_CODE;
		}
	}
}
