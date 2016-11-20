package com.tomhedges.museumcompanion.model.institution_mappings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.model.ItemObject;
import com.tomhedges.museumcompanion.model.ItemObjectBuilder;
import com.tomhedges.museumcompanion.model.SearchResults;

public class BritishMuseumBuilder {

	public BritishMuseumBuilder() {

	}

	public static ItemObject BuildObject(ItemObject ioObjectToBeCompleted) {

		JSONObject jObj;
		JSONObject joObject = null;
		JSONArray jaArray;
		String strRetrievedValue;

		try {
			jObj = new JSONObject(ioObjectToBeCompleted.getItemData());

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
			Log.d(BritishMuseumBuilder.class.getName(), "Parsing: " + strRoot);
			joObject = jObj.getJSONObject(strRoot);

		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "ERROR! Failed to deal with: " + ioObjectToBeCompleted);
			e.printStackTrace();
			return ioObjectToBeCompleted;
		}

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_MAIN_IMAGE);
			strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			ioObjectToBeCompleted.setImageURL(strRetrievedValue);
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No main image");
			e.printStackTrace();
		}

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_OTHER_IMAGES);

			String strMainImageURL = ioObjectToBeCompleted.getImageURL();

			int numImages = jaArray.length();
			boolean mainImageInArray = false;

			if (numImages>0) {
				for (int loopCounter = 0; loopCounter < numImages; loopCounter++) {
					if (strMainImageURL != null && jaArray.getJSONObject(loopCounter).getString("value").equals(strMainImageURL)) {
						mainImageInArray = true;
					}
				}
			}

			int arrayLengthener = 0;
			int selectionReducer = 0;
			if (!mainImageInArray) {
				arrayLengthener = 1;
				selectionReducer = -1;
				numImages++;
			}

			String[] imageList = new String[numImages];
			for (int loopCounter = 0 + arrayLengthener; loopCounter < numImages; loopCounter++) {
				imageList[loopCounter] = jaArray.getJSONObject(loopCounter + selectionReducer).getString("value");
			}

			if (!mainImageInArray && strMainImageURL != null) {
				imageList[0] = strMainImageURL;
			}

			ioObjectToBeCompleted.setImageList(imageList);
			//strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			//ioObjectToBeCompleted.setImageURL(strRetrievedValue);
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No additional images");
			if (ioObjectToBeCompleted.getImageURL() != null) {
				Log.d(BritishMuseumBuilder.class.getName(), "Main image only");
				ioObjectToBeCompleted.setImageList(new String[] {ioObjectToBeCompleted.getImageURL()});
			} else {
				Log.d(BritishMuseumBuilder.class.getName(), "No images at all");
			}
			e.printStackTrace();
		}

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_NAME);
			strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			ioObjectToBeCompleted.setName(strRetrievedValue);
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No name");
			e.printStackTrace();
		}

		String fullDescription = "";// = Constants.IO_FIELD_NO_INFO;

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_PHYSICAL_DESC);
			strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			fullDescription = FullDescBuilder(fullDescription) + "Physical Description:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No physical description");
			e.printStackTrace();
		}

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_EXHIBITION_LABEL);
			strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			fullDescription = FullDescBuilder(fullDescription) + "Exhibition Label:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No exhibition label");
			e.printStackTrace();
		}

		try {
			jaArray = joObject.getJSONArray(Constants.INSTITUTION_FIELD_BRITISH_MUSEUM_CURATORIAL_COMMENT);
			strRetrievedValue = jaArray.getJSONObject(0).getString("value");
			fullDescription = FullDescBuilder(fullDescription) + "Curatorial Comment:\n" + strRetrievedValue;
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "No curatorial comment");
			e.printStackTrace();
		}

		if (fullDescription==null || fullDescription.length()==0) {
			fullDescription = Constants.IO_FIELD_NO_INFO;
		}
		ioObjectToBeCompleted.setFullText(fullDescription);

		Log.d(BritishMuseumBuilder.class.getName(), "Successfully Parsed!");

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

		try {
			jObj = new JSONObject(strRetrievedResultsData);
			joObject = jObj.getJSONObject(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_RESULTS);
			resultsArray = joObject.getJSONArray(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_BINDINGS);

			straTitles = new String[resultsArray.length()];
			straURLs = new String[resultsArray.length()];
			//int iDuplicateCounter = 0;

			//for (int loopCounter = 0; loopCounter < resultsArray.length(); loopCounter++) {
			//	joData = resultsArray.getJSONObject(loopCounter);
			//	joSubData = joData.getJSONObject(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_LABEL);

			//	// BM API throwing back duplicate object with several URLs - command to remove this seems not to work - ask their suppor team
			//	//for now, workaround by removing consecutive duplicates! Though this isn't full-proof!
			//	if (loopCounter>0 && straTitles[loopCounter-1-iDuplicateCounter].equals(joSubData.getString("value"))) {
			//		//DUPLICATE! SO DO NOTHING?
			//		iDuplicateCounter++;
			//	} else {
			//		straTitles[loopCounter-iDuplicateCounter] = joSubData.getString("value");
			//		joSubData = joData.getJSONObject(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_OBJ);
			//		straURLs[loopCounter-iDuplicateCounter] = joSubData.getString("value");
			//	}
			//}

			//String[] temp = new String[straTitles.length-iDuplicateCounter];
			//for (int loopCounter = 0; loopCounter < temp.length; loopCounter++) {
			//	temp[loopCounter] = straTitles[loopCounter];
			//}
			//straTitles = temp.clone();

			//for (int loopCounter = 0; loopCounter < temp.length; loopCounter++) {
			//	temp[loopCounter] = straURLs[loopCounter];
			//}
			//straURLs = temp.clone();




			for (int loopCounter = 0; loopCounter < resultsArray.length(); loopCounter++) {
				joData = resultsArray.getJSONObject(loopCounter);

				try {
					joSubData = joData.getJSONObject(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_LABEL);
					straTitles[loopCounter] = joSubData.getString("value");
				} catch (JSONException e) {
					//Log.d(BritishMuseumBuilder.class.getName(), "No label!");
					//e.printStackTrace();
					straTitles[loopCounter] = "No title provided by institution";
				}
				joSubData = joData.getJSONObject(Constants.SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_OBJ);
				straURLs[loopCounter] = joSubData.getString("value");
			}




			straObjectIDs = new String[straURLs.length];
			for (int loopCounter = 0; loopCounter < straURLs.length; loopCounter++) {
				straObjectIDs[loopCounter] = straURLs[loopCounter].substring(straURLs[loopCounter].lastIndexOf("/")+1);
			}

			dsaDataSources = new DataSource[straURLs.length];
			for (int loopCounter = 0; loopCounter<straURLs.length; loopCounter++) {
				dsaDataSources[loopCounter] = Constants.DataSource.BRITISH_MUSEUM;
			}
		} catch (JSONException e) {
			Log.d(BritishMuseumBuilder.class.getName(), "Search data error!");
			e.printStackTrace();
			return null;
		}

		SearchResults srResults = new SearchResults(straObjectIDs, straTitles, straURLs, dsaDataSources);

		Log.d(BritishMuseumBuilder.class.getName(), "Successfully Parsed Results!");

		return srResults;
	}
}
