package com.tomhedges.museumcompanion.model.institution_mappings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.model.ItemObject;
import com.tomhedges.museumcompanion.model.SearchResults;
import com.tomhedges.museumcompanion.model.SearchResultsBuilder;

public class VictoriaAndAlbertBuilder {

	public VictoriaAndAlbertBuilder() {

	}

	public static ItemObject BuildObject(ItemObject ioObjectToBeCompleted) {

		JSONObject joObjectFields = null;
		JSONArray jaArray;
		String strRetrievedValue;

		try {
			//jObj = new JSONObject(ioObjectToBeCompleted.getItemData());

			// PRINTS LIST OF ALL KEYS IN THE JSON!!!
			//			JSONObject jObjINNER = jObj.getJSONObject("http://collection.britishmuseum.org/id/object/" + strObjectID);
			//			Iterator<String> keys = jObjINNER.keys();
			//			Log.d(ItemObjectBuilder.class.getName(), "Outputting all keys!");
			//			Log.d(ItemObjectBuilder.class.getName(), keys.next());
			//		    while(keys.hasNext()){
			//		        String key = keys.next();
			//				Log.d(ItemObjectBuilder.class.getName(), key);
			//		    }
			//			Log.d(ItemObjectBuilder.class.getName(), "Output ended!");


			String strRoot = Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_ROOT_DATA_PREFIX + ioObjectToBeCompleted.getObjectID();
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "Parsing: " + strRoot);
			jaArray = new JSONArray(ioObjectToBeCompleted.getItemData());
			joObjectFields = jaArray.getJSONObject(0);
			joObjectFields = joObjectFields.getJSONObject(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_FIELDS);

		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "ERROR! Failed to deal with: " + ioObjectToBeCompleted);
			e.printStackTrace();
			return ioObjectToBeCompleted;
		}

		//--- start images ---

		try {
			strRetrievedValue = joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_MAIN_IMAGE);
			if (strRetrievedValue.length() > 0) {
				ioObjectToBeCompleted.setImageURL(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_IMAGE_URL_ROOT + "collection_images/" + strRetrievedValue.substring(0, 6) + "/" + strRetrievedValue + ".jpg");
			} else {
				Log.d(VictoriaAndAlbertBuilder.class.getName(), "No main image");
			}

			jaArray = joObjectFields.getJSONArray(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_OTHER_IMAGES);

			int numImages = jaArray.length();

			if (numImages>=1) {
				String[] imageList = new String[numImages];
				for (int loopCounter = 0; loopCounter < numImages; loopCounter++) {
					imageList[loopCounter] = Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_IMAGE_URL_ROOT + jaArray.getJSONObject(loopCounter).getJSONObject(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_FIELDS).get(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_OTHER_IMAGE_FIELD);
				}
				ioObjectToBeCompleted.setImageList(imageList);
			} else {
				Log.d(VictoriaAndAlbertBuilder.class.getName(), "No images at all");
			}
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No images (or error caused)");
			e.printStackTrace();
		}


		//--- end images ---

		try {
			ioObjectToBeCompleted.setName(joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_NAME));
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No name");
			e.printStackTrace();
		}

		String fullDescription = "";// = Constants.IO_FIELD_NO_INFO;

		try {
			strRetrievedValue = joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_DESCRIPTION);
			fullDescription = FullDescBuilder(fullDescription) + "Description:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No physical description");
			e.printStackTrace();
		}

		try {
			strRetrievedValue = joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_LABEL);
			fullDescription = FullDescBuilder(fullDescription) + "Label:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No label");
			e.printStackTrace();
		}

		try {
			strRetrievedValue = joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_HISTORY_NOTE);
			fullDescription = FullDescBuilder(fullDescription) + "Historical Note:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No historical note");
			e.printStackTrace();
		}

		try {
			strRetrievedValue = joObjectFields.getString(Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_MARKS);
			fullDescription = FullDescBuilder(fullDescription) + "Markings:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "No markings");
			e.printStackTrace();
		}

		if (fullDescription==null || fullDescription.length()==0) {
			fullDescription = Constants.IO_FIELD_NO_INFO;
		}
		ioObjectToBeCompleted.setFullText(fullDescription);

		Log.d(VictoriaAndAlbertBuilder.class.getName(), "Successfully Parsed!");

		return ioObjectToBeCompleted;
	}

	private static String FullDescBuilder(String fullDesc) {
		if (fullDesc!=null && fullDesc.length()>0) {
			fullDesc = fullDesc + "\n\n------------------------------------------------------------------------------------\n\n";
		}
		return fullDesc;
	}


	public static SearchResults BuildSearchResults(String strRetrievedResultsData) {
		
		JSONObject jObj;
		JSONObject joObject = null;
		JSONArray resultsArray;
		JSONObject joData = null;
		JSONObject joSubData = null;
		String[] straObjectIDs = null;
		String[] straTitles = null;
		String[] straURLs = null;
		DataSource[] dsaDataSources = null;
		Integer iResultCount;

		try {
			jObj = new JSONObject(strRetrievedResultsData);
			joObject = jObj.getJSONObject(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_RESULTS);
			iResultCount = joObject.getInt(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_RESULT_COUNT);
			Log.v(VictoriaAndAlbertBuilder.class.getName(), "Number of results: " + iResultCount);
			if (iResultCount==0) {
				Log.v(VictoriaAndAlbertBuilder.class.getName(), "No results returned by search - bailing out...");
				return null;
			}
			resultsArray = jObj.getJSONArray(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_RECORDS);

			straTitles = new String[resultsArray.length()];
			straURLs = new String[resultsArray.length()];
			straObjectIDs = new String[resultsArray.length()];

			for (int loopCounter = 0; loopCounter < resultsArray.length(); loopCounter++) {
				joData = resultsArray.getJSONObject(loopCounter);

				try {
					joSubData = joData.getJSONObject(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_FIELDS);
					straTitles[loopCounter] = joSubData.getString(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_NAME) + ", " + joSubData.getString(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_PLACE) + ", " +joSubData.getString(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_DATE_TEXT);
				} catch (JSONException e) {
					straTitles[loopCounter] = "No title provided by institution";
				}

				straObjectIDs[loopCounter] = joSubData.getString(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_NUMBER);
				straURLs[loopCounter] = Constants.INSTITUTION_FIELD_VICTORIA_AND_ALBERT_ROOT_DATA_PREFIX + joSubData.getString(Constants.SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_NUMBER);
			}

			dsaDataSources = new DataSource[straURLs.length];
			for (int loopCounter = 0; loopCounter<straURLs.length; loopCounter++) {
				dsaDataSources[loopCounter] = Constants.DataSource.VICTORIA_AND_ALBERT_MUSEUM;
			}
		} catch (JSONException e) {
			Log.d(VictoriaAndAlbertBuilder.class.getName(), "Search data error!");
			e.printStackTrace();
			return null;
		}

		SearchResults srResults = new SearchResults(straObjectIDs, straTitles, straURLs, dsaDataSources);

		Log.d(VictoriaAndAlbertBuilder.class.getName(), "Successfully Parsed Results!");

		return srResults;
	}
}
