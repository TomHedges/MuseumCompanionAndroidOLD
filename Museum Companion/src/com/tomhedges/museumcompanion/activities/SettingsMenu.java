package com.tomhedges.museumcompanion.activities;

import java.io.File;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.utilities.ImageGalleryHelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsMenu extends Activity implements OnClickListener {

	private Button btnDeleteAllImages;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		btnDeleteAllImages = (Button) findViewById(R.id.btnSettingsDeleteImages);

		btnDeleteAllImages.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnSettingsDeleteImages:
			new AlertDialog.Builder(this)
			.setTitle("Deletion")
			.setMessage("Do you really want to delete all images?")
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				//delete all images
				public void onClick(DialogInterface dialog, int whichButton) {
					File directory = new File(getDir(Constants.DIRECTORY_IMAGES, Context.MODE_PRIVATE) + "");

					// check for directory
					if (directory.isDirectory()) {
						// getting list of file paths
						File[] listFiles = directory.listFiles();

						// Check for count
						if (listFiles.length > 0) {
							Toast.makeText(SettingsMenu.this, "DELETING", Toast.LENGTH_SHORT).show();
							
							// loop through all files
							for (int i = listFiles.length-1; i >= 0; i--) {

								listFiles[i].delete();
							}

							Toast.makeText(SettingsMenu.this, "All images deleted!", Toast.LENGTH_SHORT).show();
						} else {
							// image directory is empty
							Toast.makeText(SettingsMenu.this, "No images", Toast.LENGTH_SHORT).show();
						}
					}
				}
			})
			.setNegativeButton(android.R.string.no, null).show();
			break;

		default:
			break;
		}
	}
}
