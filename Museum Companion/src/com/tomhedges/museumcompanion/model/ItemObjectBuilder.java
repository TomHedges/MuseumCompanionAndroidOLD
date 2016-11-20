package com.tomhedges.museumcompanion.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Observable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.config.Constants.DataSource;
import com.tomhedges.museumcompanion.dao.RemoteDataAccess;
import com.tomhedges.museumcompanion.model.institution_mappings.BritishMuseumBuilder;
import com.tomhedges.museumcompanion.model.institution_mappings.VictoriaAndAlbertBuilder;

public class ItemObjectBuilder extends Observable {

	private static ItemObjectBuilder instance = null;
	private Context context;

	private ItemObject ioObject = null;
	private DataSource dsDataSource;
	private String strObjectID;

	public static ItemObjectBuilder getInstance(Context context) {
		if(instance == null) {
			instance = new ItemObjectBuilder(context);
		}
		return instance;
	}

	private ItemObjectBuilder(Context context) {
		this.context = context;
	}

	public void getObject(DataSource dsDataSource, String strObjectID) {

		this.dsDataSource = dsDataSource;
		this.strObjectID = strObjectID;

		new DownloadDataAndBuildObject().execute(true);
	}

	private class DownloadDataAndBuildObject extends AsyncTask<Boolean, String, Boolean> {
		private RemoteDataAccess remoteDataAccess;

		protected void onPreExecute() {
			super.onPreExecute();
			remoteDataAccess = new RemoteDataAccess();
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			String strItemData = remoteDataAccess.getObjectData(dsDataSource, strObjectID);

			//publishProgress("blah");
			if (strItemData.equals(Constants.ERROR_CODE)) {
				Log.e(DownloadDataAndBuildObject.class.getName(), "Failed to retrieve result...");
				return false;
			} else {
				Log.d(DownloadDataAndBuildObject.class.getName(), "Successfully retrieved result!");
				buildObject(strItemData);
				return true;
			}
		}

		protected void onProgressUpdate(String... progress) {
			super.onProgressUpdate(progress);
		}

		protected void onPostExecute(Boolean succcessful) {
			super.onPostExecute(succcessful);

			if (succcessful) {
				if (Constants.TEST_STOP_IMAGE_LOAD) {
					Log.d(DownloadDataAndBuildObject.class.getName(), "No image - in test mode");
					SendUpdate(ioObject);
				} else if (ioObject.getImageURL() != null ) {
					Log.d(DownloadDataAndBuildObject.class.getName(), "Aquiring image: " + ioObject.getImageURL());
					new DownloadImageTask().execute(ioObject.getImageURL());
					if (ioObject.getImageList().length>1) {
						new DownloadImageTask().execute(ioObject.getImageList());
					}
				} else {
					Log.d(DownloadDataAndBuildObject.class.getName(), "No image for object??");
					ioObject.setImageURL(null);
					SendUpdate(ioObject);
				}
			} else {
				Log.e(DownloadDataAndBuildObject.class.getName(), "Exiting with null data");
				SendUpdate(null);
			}
		}
	}

	private void buildObject(String strObjectData) {

		ioObject = new ItemObject(dsDataSource, strObjectID, strObjectData);

		//TODO: Break this into some sort of dependency injection?
		switch (dsDataSource) {

		case BRITISH_MUSEUM:
			ioObject = BritishMuseumBuilder.BuildObject(ioObject);
			break;

		case VICTORIA_AND_ALBERT_MUSEUM:
			ioObject = VictoriaAndAlbertBuilder.BuildObject(ioObject);
			break;

		case TEST_DATA_SOURCE:
			ioObject.setFullText(Constants.TEST_DATA);
			ioObject.setItemData(Constants.TEST_DATA);
			ioObject.setName("Test data artefact!");
			ioObject.setImageURL(Constants.TEST_IMAGE_URL);
			break;

		default:
			break;
		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Integer> {

		protected Integer doInBackground(String... urls) {

			for (int loopCounter = 0; loopCounter<urls.length; loopCounter++) {
				String urlForImage = urls[loopCounter];
				String localFilename = urlForImage.substring( urlForImage.lastIndexOf('/')+1, urlForImage.length());
				localFilename = dsDataSource + "_" + strObjectID + "_" + localFilename;
				Log.d(ItemObjectBuilder.class.getName(), "Image local name: " + localFilename);
				File directory = context.getDir(Constants.DIRECTORY_IMAGES, Context.MODE_PRIVATE);
				File mypath = new File(directory, localFilename);

				if(mypath.exists()) {
					Log.d(ItemObjectBuilder.class.getName(), "Image already exists, so not re-downloading");

					if (urls.length == 1) {
						Log.d(ItemObjectBuilder.class.getName(), "Master image identified!");
						ioObject.setImageLocalPath(directory.getAbsolutePath());
						ioObject.setImageLocalName(mypath.getName());
					}
				} else {
					Log.d(ItemObjectBuilder.class.getName(), "Attempt download of new image...");

					InputStream in;
					Bitmap bmpImageDownloaded = null;
					try {
						in = new java.net.URL(urlForImage).openStream();
						bmpImageDownloaded = BitmapFactory.decodeStream(in);
						in.close();

						FileOutputStream fos = null;
						fos = new FileOutputStream(mypath);

						// Use the compress method on the BitMap object to write image to the OutputStream
						bmpImageDownloaded.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						fos.close();

						Log.d(ItemObjectBuilder.class.getName(), "Image path stored as: " + directory.getAbsolutePath());
						Log.d(ItemObjectBuilder.class.getName(), "Image local name stored as: " + mypath.getName());

						if (urls.length == 1) {
							Log.d(ItemObjectBuilder.class.getName(), "Master image retrieved!");
							ioObject.setImageLocalPath(directory.getAbsolutePath());
							ioObject.setImageLocalName(mypath.getName());
						}
					} catch (Exception e) {
						Log.e("Error with image retrieval or saving! Error detail: ", e.getMessage());
						e.printStackTrace();
					}
				}
			}
			return urls.length;
		}

		protected void onPostExecute(Integer imageCount) {
			int numOfImages = (int) imageCount;
			if (numOfImages==1) {
				// This must be the main image (as only one)
				SendUpdate(ioObject);
			} else {

			}
		}
	}

	private void SendUpdate(Object objectToSend) {
		setChanged();
		notifyObservers(objectToSend);
	}
}
