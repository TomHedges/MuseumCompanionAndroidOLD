package com.tomhedges.museumcompanion.activities;

import com.tomhedges.museumcompanion.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {

	private Intent intent;

	private Button btnRandom;
	private Button btnAdvancedSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);

		btnRandom = (Button) findViewById(R.id.btnMainMenuRandomItem);
		btnAdvancedSearch = (Button) findViewById(R.id.btnMainMenuAdvancedSearch);

		btnRandom.setOnClickListener(this);
		btnAdvancedSearch.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {

		case R.id.main_menu_settings:
			intent = new Intent(this, SettingsMenu.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnMainMenuRandomItem:
			intent = new Intent(this, DisplayItem.class);
			startActivity(intent);
			break;

		case R.id.btnMainMenuAdvancedSearch:
			intent = new Intent(this, SearchCollections.class);
			startActivity(intent);
			break;

		default:
			break;
		}

		hideSoftKeyboard(this, v);
	}

	public static void hideSoftKeyboard (Activity activity, View view) 
	{
		InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}
}
