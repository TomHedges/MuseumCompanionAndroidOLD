package com.tomhedges.museumcompanion.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SearchResults implements Parcelable, Serializable {

	private static final long serialVersionUID = 6083971132743696546L;
	private int iTotalResults;
	private int iStartPointer;
	private int iEndPointer;
	
	private static String strFilePath;

	private String[] straFullResultsObjectID;
	private String[] straFullResultsTitle;
	private String[] straFullResultsURL;
	private DataSource[] dsaFullDataSource;

	private String[] straSegmentResultsObjectID;
	private String[] straSegmentResultsTitle;
	private String[] straSegmentResultsURL;
	private DataSource[] dsaSegmentDataSource;

	public SearchResults(String[] straResultsObjectID, String[] straResultsTitle, String[] straResultsURL, DataSource[] dsaDataSource) {
		this.iTotalResults = straResultsURL.length;
		this.iStartPointer = 0;
		this.iEndPointer = 0;

		this.straFullResultsObjectID = straResultsObjectID;
		this.straFullResultsTitle = straResultsTitle;
		this.straFullResultsURL = straResultsURL;
		this.dsaFullDataSource = dsaDataSource;
		
		activate();
	}

	public void save(Context context, String fileName) {
		Log.d(SearchResults.class.getName(), "Saving SearchResults to: " + fileName);
		try {
			fileName = "Search_Results_" + fileName;
			File directory = context.getDir(Constants.DIRECTORY_SEARCH_RESULTS, Context.MODE_PRIVATE);
			File mypath = new File(directory, fileName);
			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(this);
			os.close();
			fos.close();
			strFilePath = fileName;
		} catch (Exception e) {
			Log.e(SearchResults.class.getName(), "Error attempting SearchResults save...");
			e.printStackTrace();
		}
	}
	
	public static SearchResults load(Context context) {
		Log.d(SearchResults.class.getName(), "Loading SearchResults from: " + strFilePath);
		try {
			FileInputStream fis = context.openFileInput(strFilePath);
			ObjectInputStream is;
			is = new ObjectInputStream(fis);
			SearchResults searchResults = (SearchResults) is.readObject();
			is.close();
			fis.close();
			return searchResults;
		} catch (Exception e) {
			Log.e(SearchResults.class.getName(), "Error attempting SearchResults load...");
			e.printStackTrace();
			return null;
		}
	}

	public void activate() {
		iStartPointer = 1;
		int iLimit;
		if (iTotalResults < Constants.SEARCH_RESULTS_GROUPING) {
			iLimit = iTotalResults;
		} else {
			iLimit = Constants.SEARCH_RESULTS_GROUPING;
		}
		iEndPointer = iLimit;
		straSegmentResultsObjectID = Arrays.copyOf(straFullResultsObjectID, iLimit);
		straSegmentResultsTitle = Arrays.copyOf(straFullResultsTitle, iLimit);
		straSegmentResultsURL = Arrays.copyOf(straFullResultsURL, iLimit);
		dsaSegmentDataSource = Arrays.copyOf(dsaFullDataSource, iLimit);
	}

	public void loadEarlier() {
		int iLimit;
		if (iEndPointer - iStartPointer < Constants.SEARCH_RESULTS_GROUPING) {
			iLimit = iEndPointer - iStartPointer;
		} else {
			iLimit = Constants.SEARCH_RESULTS_GROUPING;
		}

		iStartPointer = iStartPointer - Constants.SEARCH_RESULTS_GROUPING;
		iEndPointer = iEndPointer - iLimit;
		
		straSegmentResultsObjectID = Arrays.copyOfRange(straFullResultsObjectID, iStartPointer-1, iEndPointer-1);
		straSegmentResultsTitle = Arrays.copyOfRange(straFullResultsTitle, iStartPointer-1, iEndPointer-1);
		straSegmentResultsURL = Arrays.copyOfRange(straFullResultsURL, iStartPointer-1, iEndPointer-1);
		dsaSegmentDataSource = Arrays.copyOfRange(dsaFullDataSource, iStartPointer-1, iEndPointer-1);
	}

	public void loadLater() {
		int iLimit;
		if (iTotalResults < iEndPointer + Constants.SEARCH_RESULTS_GROUPING) {
			iLimit = iTotalResults; //iEndPointer + iTotalResults;
		} else {
			iLimit = iEndPointer + Constants.SEARCH_RESULTS_GROUPING;
		}

		iStartPointer = iStartPointer + Constants.SEARCH_RESULTS_GROUPING;
		iEndPointer = iLimit;
		
		straSegmentResultsObjectID = Arrays.copyOfRange(straFullResultsObjectID, iStartPointer-1, iEndPointer-1);
		straSegmentResultsTitle = Arrays.copyOfRange(straFullResultsTitle, iStartPointer-1, iEndPointer-1);
		straSegmentResultsURL = Arrays.copyOfRange(straFullResultsURL, iStartPointer-1, iEndPointer-1);
		dsaSegmentDataSource = Arrays.copyOfRange(dsaFullDataSource, iStartPointer-1, iEndPointer-1);
	}

	public int getTotalResults() {
		return iTotalResults;
	}

	public int getStartPointer() {
		return iStartPointer;
	}

	public int getEndPointer() {
		return iEndPointer;
	}

	//public String[] getResultsObjectIDs() {
	//	return straResultsObjectID;
	//}

	public String getResultObjectID(int iPosition) {
		if (iPosition<0 || iPosition>Constants.SEARCH_RESULTS_GROUPING-1) {
			return null;
		} else {
			return straSegmentResultsObjectID[iPosition];
		}
	}

	public String[] getResultsTitles() {
		return straSegmentResultsTitle;
	}

	public String getResultTitle(int iPosition) {
		if (iPosition<0 || iPosition>Constants.SEARCH_RESULTS_GROUPING-1) {
			return null;
		} else {
			return straSegmentResultsTitle[iPosition];
		}
	}

	public String[] getResultsURLs() {
		return straSegmentResultsURL;
	}

	public String getResultURL(int iPosition) {
		if (iPosition<0 || iPosition>Constants.SEARCH_RESULTS_GROUPING-1) {
			return null;
		} else {
			return straSegmentResultsURL[iPosition];
		}
	}

	public DataSource[] getDataSources() {
		return dsaSegmentDataSource;
	}

	public DataSource getDataSource(int iPosition) {
		if (iPosition<0 || iPosition>Constants.SEARCH_RESULTS_GROUPING-1) {
			return null;
		} else {
			return dsaSegmentDataSource[iPosition];
		}
	}

	public String getInstitutionName(DataSource dsDataSource) {
		for (int iLoopCounter = 0; iLoopCounter < Constants.DataSourceList.length; iLoopCounter++) {
			if (Constants.DataSourceList[iLoopCounter].equals(dsDataSource)) {
				return Constants.DataSourceLabel[iLoopCounter];
			}
		}
		return null;
	}

	@Override
	public int describeContents() {
		Log.d(SearchResults.class.getName(), "SearchResults describeContents ???");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Log.d(SearchResults.class.getName(), "Writing SearchResults to Parcel start...");
		Log.d(SearchResults.class.getName(), "Parcel data still available: " + out.dataCapacity() + " bytes.");
		out.writeInt(iTotalResults);
		out.writeInt(iStartPointer);
		out.writeInt(iEndPointer);
		out.writeString(strFilePath);
		out.writeStringArray(straSegmentResultsObjectID);
		out.writeStringArray(straSegmentResultsTitle);
		out.writeStringArray(straSegmentResultsURL);

		int[] iaDataSourceValues = new int[dsaSegmentDataSource.length];
		for (int loopCounter = 0; loopCounter<dsaSegmentDataSource.length; loopCounter++) {
			iaDataSourceValues[loopCounter] = dsaSegmentDataSource[loopCounter].ordinal();
		}
		out.writeIntArray(iaDataSourceValues);
		Log.d(SearchResults.class.getName(), "Parcel data still available: " + out.dataCapacity() + " bytes.");

		Log.d(SearchResults.class.getName(), "Writing SearchResults to Parcel end...");
	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Parcelable.Creator<SearchResults> CREATOR = new Parcelable.Creator<SearchResults>() {
		public SearchResults createFromParcel(Parcel in) {
			Log.d(SearchResults.class.getName(), "Creating SearchResults from Parcel...");
			return new SearchResults(in);
		}

		public SearchResults[] newArray(int size) {
			Log.d(SearchResults.class.getName(), "SearchResults array...");
			return new SearchResults[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated with it's values
	private SearchResults(Parcel in) {
		Log.d(SearchResults.class.getName(), "SearchResults constructor taking Parcel start..."); 
		iTotalResults = in.readInt();
		iStartPointer = in.readInt();
		iEndPointer = in.readInt();
		strFilePath = in.readString();
		straSegmentResultsObjectID = in.createStringArray();
		straSegmentResultsTitle = in.createStringArray();
		straSegmentResultsURL = in.createStringArray();

		int[] iaDataSourceValues = in.createIntArray();
		dsaSegmentDataSource = new DataSource[iaDataSourceValues.length];
		for (int loopCounter = 0; loopCounter<dsaSegmentDataSource.length; loopCounter++) {
			dsaSegmentDataSource[loopCounter] = DataSource.values()[iaDataSourceValues[loopCounter]];
		}

		Log.d(SearchResults.class.getName(), "SearchResults constructor taking Parcel end..."); 
	}
}
