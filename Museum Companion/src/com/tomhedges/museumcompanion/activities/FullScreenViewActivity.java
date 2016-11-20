package com.tomhedges.museumcompanion.activities;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.utilities.ImageGalleryHelper;
import com.tomhedges.museumcompanion.utilities.adapter.FullScreenImageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class FullScreenViewActivity extends Activity{

	private ImageGalleryHelper utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		utils = new ImageGalleryHelper(getApplicationContext());

		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
		String imageNameRoot = i.getStringExtra(Constants.ITEM_IMAGE_NAME_ROOT);

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				utils.getFilePaths(imageNameRoot));

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);
	}
}
