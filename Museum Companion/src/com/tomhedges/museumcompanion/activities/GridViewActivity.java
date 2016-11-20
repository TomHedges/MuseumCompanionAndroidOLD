package com.tomhedges.museumcompanion.activities;
 
import java.util.ArrayList;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.utilities.ImageGalleryHelper;
import com.tomhedges.museumcompanion.utilities.adapter.GridViewImageAdapter;
 
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.GridView;
 
public class GridViewActivity extends Activity {
 
    private ImageGalleryHelper utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private String imageNameRoot;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

		Intent intent = getIntent();
		imageNameRoot = intent.getExtras().getString(Constants.ITEM_IMAGE_NAME_ROOT);
		
        gridView = (GridView) findViewById(R.id.grid_view);
 
        utils = new ImageGalleryHelper(this);
 
        // Initilizing Grid View
        InitilizeGridLayout();
 
        // loading all image paths from SD card
        imagePaths = utils.getFilePaths(imageNameRoot);
 
        // Gridview adapter
        adapter = new GridViewImageAdapter(GridViewActivity.this, imagePaths, columnWidth, imageNameRoot);
 
        // setting grid view adapter
        gridView.setAdapter(adapter);
    }
 
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Constants.GRID_PADDING, r.getDisplayMetrics());
 
        columnWidth = (int) ((utils.getScreenWidth() - ((Constants.NUM_OF_COLUMNS + 1) * padding)) / Constants.NUM_OF_COLUMNS);
 
        gridView.setNumColumns(Constants.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
 
}
