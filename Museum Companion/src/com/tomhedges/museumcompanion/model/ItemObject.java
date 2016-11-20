package com.tomhedges.museumcompanion.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;

public class ItemObject implements Parcelable {

	private DataSource dsDataSource;
	private String strItemData;
	private String strObjectID;
	private String strName;
	private String strImageURL;
	private String strImageLocalPath;
	private String strImageLocalName;
	private String strFullText;
	private Bitmap bmpMainImage;
	private String[] arrayImageListURL;
	private String strInstitution;

	public ItemObject(DataSource dsDataSource, String strObjectID, String strItemData) {
		this.dsDataSource = dsDataSource;
		this.strObjectID = strObjectID;
		this.strItemData = strItemData;
		this.strFullText = Constants.IO_FIELD_NO_INFO;
		this.strName = Constants.IO_FIELD_NO_NAME;
		this.bmpMainImage = null;
		this.arrayImageListURL = new String[0];
		
		for (int iLoopCounter = 0; iLoopCounter < Constants.DataSourceList.length; iLoopCounter++) {
			if (Constants.DataSourceList[iLoopCounter].equals(dsDataSource)) {
				strInstitution = Constants.DataSourceLabel[iLoopCounter];
			}
		}
	}

	public void setDataSource(DataSource dsDataSource) {
		this.dsDataSource = dsDataSource;
	}

	public DataSource getDataSource() {
		return dsDataSource;
	}

	public void setItemData(String strItemData) {
		this.strItemData = strItemData;
	}

	public String getItemData() {
		return strItemData;
	}

	public void setObjectID(String strObjectID) {
		this.strObjectID = strObjectID;
	}

	public String getObjectID() {
		return strObjectID;
	}

	public void setName(String strName) {
		this.strName = strName;
	}

	public String getName() {
		return strName;
	}

	public void setImageURL(String strImageURL) {
		this.strImageURL = strImageURL;
	}

	public String getImageURL() {
		return strImageURL;
	}

	public void setFullText(String strFullText) {
		this.strFullText = strFullText;
	}

	public String getFullText() {
		return strFullText;
	}

	//public void setMainImage(Bitmap bmpMainImage) {
	//	this.bmpMainImage = bmpMainImage;
	//}

	public Bitmap getMainImage(Context context) {
		//Log.d(ItemObject.class.getName(), "Values: " + bmpMainImage + " : " + strImageURL + " : " + strImageURL.equals(Constants.TEST_IMAGE_URL));
		if (bmpMainImage == null && strImageURL != null && !Constants.TEST_STOP_IMAGE_LOAD) {
			Log.d(ItemObject.class.getName(), "Attempting retrieval of local image: " + strImageLocalPath);
			try {
				File f=new File(strImageLocalPath, strImageLocalName);
				bmpMainImage = BitmapFactory.decodeStream(new FileInputStream(f));
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
		} else {
			Log.d(ItemObject.class.getName(), "No image available (or in test mode)");
			bmpMainImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
		}

		return bmpMainImage;
	}

	public String getInstitution() {
		return strInstitution;
	}

	public void setImageLocalPath(String strImageLocalPath) {
		this.strImageLocalPath = strImageLocalPath;
	}

	public String getImageLocalPath() {
		return strImageLocalPath;
	}
	
	public void setImageLocalName(String strImageLocalName) {
		this.strImageLocalName = strImageLocalName;
	}

	public String getImageLocalName() {
		return strImageLocalName;
	}

	public String[] getImageList() {
		return arrayImageListURL;
	}
	
	public void setImageList(String[] imageList) {
		this.arrayImageListURL = imageList;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		Log.d(ItemObject.class.getName(), "ItemObject describeContents ???");
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		Log.d(ItemObject.class.getName(), "Writing ItemObject to Parcel...");
		out.writeValue(dsDataSource);
		out.writeString(strItemData);
		out.writeString(strObjectID);
		out.writeString(strName);
		out.writeString(strInstitution);
		out.writeString(strImageURL);
		out.writeString(strFullText);
		out.writeString(strImageLocalPath);
		out.writeString(strImageLocalName);
		out.writeStringArray(arrayImageListURL);
		Log.d(ItemObject.class.getName(), "Writing ItemObject to Parcel 2...");
	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
	public static final Parcelable.Creator<ItemObject> CREATOR = new Parcelable.Creator<ItemObject>() {
		public ItemObject createFromParcel(Parcel in) {
			Log.d(ItemObject.class.getName(), "Creating ItemObject from Parcel...");
			return new ItemObject(in);
		}

		public ItemObject[] newArray(int size) {
			Log.d(ItemObject.class.getName(), "ItemObject array...");
			return new ItemObject[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated with it's values
	private ItemObject(Parcel in) {   
		Log.d(ItemObject.class.getName(), "ItemObject constructor taking Parcel..."); 	
		dsDataSource = (DataSource) in.readValue(DataSource.class.getClassLoader());
		strItemData = in.readString();
		strObjectID = in.readString();
		strName = in.readString();
		strInstitution = in.readString();
		strImageURL = in.readString();
		strFullText = in.readString();
		strImageLocalPath = in.readString();
		strImageLocalName = in.readString();
		arrayImageListURL = in.createStringArray();
		//arrayImageListURL = (String[]) in.re(String.class.getClassLoader());
	}
}
