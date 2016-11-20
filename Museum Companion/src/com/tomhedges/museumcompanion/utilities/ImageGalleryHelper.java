package com.tomhedges.museumcompanion.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.model.ItemObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class ImageGalleryHelper {

	private Context _context;

	// constructor
	public ImageGalleryHelper(Context context) {
		this._context = context;
	}

	// Reading file paths from SDCard
	public ArrayList<String> getFilePaths(String imageNameRoot) {
		ArrayList<String> filePaths = new ArrayList<String>();

		//File directory = new File(
		//		android.os.Environment.getExternalStorageDirectory()
		//		+ File.separator + Constants.PHOTO_ALBUM);
		File directory = new File(
				_context.getDir(Constants.DIRECTORY_IMAGES, Context.MODE_PRIVATE)
				+ "");
		Log.d(ImageGalleryHelper.class.getName(), "Attempting to load images for GridView from: " + directory);

		// check for directory
		if (directory.isDirectory()) {
			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					String filePath = listFiles[i].getAbsolutePath();
					
					String fileName = listFiles[i].getName();
					if (fileName.startsWith(imageNameRoot, 0)) {
						// check for supported file extension
						if (IsSupportedFile(filePath)) {
							// Add image path to array list
							filePaths.add(filePath);
						}
					}
				}
			} else {
				// image directory is empty
				Toast.makeText(
						_context,
						Constants.PHOTO_ALBUM
						+ " is empty. Please load some images in it !",
						Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
			alert.setMessage(Constants.PHOTO_ALBUM + " directory path is not valid! Please set the image directory name in Constants class");
			alert.setPositiveButton("OK", null);
			alert.show();
		}

		return filePaths;
	}

	// Check supported file extensions
	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (Constants.FILE_EXTN
				.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	@SuppressLint("NewApi")
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
		.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point); //Android SDK version minimum 13
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
}
