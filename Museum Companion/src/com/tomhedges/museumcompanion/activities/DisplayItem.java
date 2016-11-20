package com.tomhedges.museumcompanion.activities;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.model.ItemObject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class DisplayItem extends Activity implements OnClickListener {

	private ItemObject ioArtefact;
	private Intent imageGridView;

	//private RelativeLayout lytObjectDetailsWrapper;
	private ImageView ivMainImage;
	private TextView tvName;
	private TextView tvMoreImages;
	private TextView tvInstitution;
	private TextView tvResults;
	private Button btnFullData;
	
	private ScrollView pane;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_item);

		Intent intent = getIntent();
		ioArtefact = intent.getParcelableExtra(Constants.ITEM_OBJECT);

		//lytObjectDetailsWrapper = (RelativeLayout) findViewById(R.id.lytObjectDetailsWrapper);
		ivMainImage = (ImageView) findViewById(R.id.ivMainImage);
		tvMoreImages = (TextView) findViewById(R.id.tvMoreImages);
		tvName = (TextView) findViewById(R.id.tvArtefactname);
		tvInstitution = (TextView) findViewById(R.id.tvInstitution);
		tvResults = (TextView) findViewById(R.id.tvResults);
		btnFullData = (Button) findViewById(R.id.btnFullData);

		ivMainImage.setImageBitmap(ioArtefact.getMainImage(this));
		tvName.setText(ioArtefact.getName());
		switch (ioArtefact.getImageList().length) {
		case 0:
			tvMoreImages.setVisibility(View.GONE);
			break;

		case 1:
			tvMoreImages.setText("Image gallery (" + ioArtefact.getImageList().length + " image)");
			break;

		default:
			tvMoreImages.setText("Image gallery (" + ioArtefact.getImageList().length + " images)");
			break;

		}
		tvInstitution.setText(ioArtefact.getInstitution());
		tvResults.setText(ioArtefact.getFullText());

		tvMoreImages.setOnClickListener(this);
		btnFullData.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.tvMoreImages:		
			imageGridView = new Intent(this, GridViewActivity.class);
			String imageNameRoot = ioArtefact.getDataSource() + "_" + ioArtefact.getObjectID() + "_";
			Log.d(DisplayItem.class.getName(), "GridView set for images with name beginning: '" + imageNameRoot + "'");
			imageGridView.putExtra(Constants.ITEM_IMAGE_NAME_ROOT, imageNameRoot);
			Log.d(DisplayItem.class.getName(), "Starting object image GridViewActivity display for object...");
			startActivity(imageGridView);
			break;

		case R.id.btnFullData:
			final AlertDialog.Builder alert = new AlertDialog.Builder(this);
			//final ScrollView
			pane = new ScrollView(this);
			final TextView fullDetails = new TextView(this);
			fullDetails.setText(ioArtefact.getItemData());
			pane.setPadding(15, 15, 15, 15);
			pane.addView(fullDetails);
			alert.setView(pane);
			alert.setView(pane);
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//alert.
				}
			});

			alert.show();  
			break;

		default:
			break;
		}		
	}

	//@Override
	//public void onBackPressed() {
	//	Log.d(DisplayItem.class.getName(), "Back button pressed!");
	//	//pane.removeAllViewsInLayout();
	//	//finishActivity();
    //    super.onBackPressed();
    //    super.onBackPressed();
    //    super.onBackPressed();
	//	finish(); // finish activity
	//	finish(); // finish activity
	//}
	
	//@Override
	//public void onDestroy() {
    //    super.onDestroy();
	//	Log.d(DisplayItem.class.getName(), "onDestroy!");
	//	finish();
	//}
}