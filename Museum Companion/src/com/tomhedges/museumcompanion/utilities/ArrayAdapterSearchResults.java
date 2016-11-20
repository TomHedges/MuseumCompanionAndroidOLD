package com.tomhedges.museumcompanion.utilities;

import com.tomhedges.museumcompanion.R;
import com.tomhedges.museumcompanion.model.SearchResults;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Convert an array of objectives into a View for display in the UI
 * <br>
 * Based on code from: http://www.javacodegeeks.com/2013/09/android-listview-with-adapter-example.html
 *  
 * @see			GameUI3D
 * @author      Tom Hedges
 */

public class ArrayAdapterSearchResults extends ArrayAdapter<String> {

    Context mContext;
    int layoutResourceId;
    SearchResults srResults = null;

    //Need to pass array in 'super' command, i think. So passing in array as well as full results-listing object
    public ArrayAdapterSearchResults(Context mContext, int layoutResourceId, String[] straResultsURLs, SearchResults srResults) {

        super(mContext, layoutResourceId, straResultsURLs);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.srResults = srResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        //// object item based on the position
        //Objective objective = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        //ImageView completionImage = (ImageView) convertView.findViewById(R.id.objective_image);
        //if (objective.isCompleted()) {
        //	completionImage.setImageResource(R.drawable.completed);
        //} else {
        //	completionImage.setImageResource(R.drawable.not_completed);
        //}
        
        TextView tvArtefactName = (TextView) convertView.findViewById(R.id.search_results_artefact_name);
        tvArtefactName.setText(srResults.getResultTitle(position));
        
        TextView tvInstitutionName = (TextView) convertView.findViewById(R.id.search_results_institution);
        tvInstitutionName.setText(srResults.getInstitutionName(srResults.getDataSource(position)));

        return convertView;
    }
}
