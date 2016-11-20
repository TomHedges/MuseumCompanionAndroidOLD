package com.tomhedges.museumcompanion.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Constant values for use across the rest of the system.
 * 
 * @author      Tom Hedges
 */

public class Constants {
	public static boolean TEST_DATA_FORCE_LOAD = false;
	public static boolean TEST_STOP_IMAGE_LOAD = false;

	public static int SEARCH_RESULTS_GROUPING = 20;
	public static String SEARCH_RESULTS_REQUESTED = "100";
	
	public static String TEST_DATA = "TEST DATA!";
	public static String TEST_IMAGE_URL = "TEST URL";
	
	public static String ERROR_CODE = "Error";
	public static String DIRECTORY_PREFIX = "app_";
	public static String DIRECTORY_IMAGES = "images";
	public static String DIRECTORY_SEARCH_RESULTS = "search_results";

	public static String IO_FIELD_NO_NAME = "[No name provided by institution]";
	public static String IO_FIELD_NO_INFO = "[No information available at this time]";

	public static String ITEM_OBJECT = "Object";
	public static String ITEM_IMAGE_NAME_ROOT = "Image Root Name";
	public static String SEARCH_RESULTS = "Search Results";
	
    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;
    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp
    // SD card image directory
    // public static final String PHOTO_ALBUM = "androidhive";
     public static final String PHOTO_ALBUM = DIRECTORY_IMAGES;
    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

	public static String SEARCH_TYPE_NAME = "Name";
	
	public static String MUSEUM_BRITISH = "British Musem";
	public static String MUSEUM_VANDA = "Victoria & Albert Museum";

	public static enum DataSource {
		BRITISH_MUSEUM,
		VICTORIA_AND_ALBERT_MUSEUM,
		TEST_DATA_SOURCE
	}

	public static String[] DataSourceLabel = {
		MUSEUM_BRITISH,
		MUSEUM_VANDA
	};
	
	public static DataSource[] DataSourceList = {
		DataSource.BRITISH_MUSEUM,
		DataSource.VICTORIA_AND_ALBERT_MUSEUM
	};
	
    private static final Map<String, String> BRITISH_MUSEUM_KEY_VALUES;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        //aMap.put(1, "one");
        //aMap.put(2, "two");
        BRITISH_MUSEUM_KEY_VALUES = Collections.unmodifiableMap(aMap);
    }

	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_ROOT_DATA_PREFIX = "http://collection.britishmuseum.org/id/object/";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_MAIN_IMAGE = "http://collection.britishmuseum.org/id/ontology/PX_has_main_representation";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_OTHER_IMAGES = "http://erlangen-crm.org/current/P138i_has_representation";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_NAME = "http://www.w3.org/2000/01/rdf-schema#label";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_PHYSICAL_DESC = "http://collection.britishmuseum.org/id/ontology/PX_physical_description";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_EXHIBITION_LABEL = "http://collection.britishmuseum.org/id/ontology/PX_object_exhibition_label";
	public static String INSTITUTION_FIELD_BRITISH_MUSEUM_CURATORIAL_COMMENT = "http://collection.britishmuseum.org/id/ontology/PX_curatorial_comment";


	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_ROOT_DATA_PREFIX = "http://www.vam.ac.uk/api/json/museumobject/";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_FIELDS = "fields";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_IMAGE_URL_ROOT = "http://media.vam.ac.uk/media/thira/";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_MAIN_IMAGE = "primary_image_id";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_OTHER_IMAGES = "image_set";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_OTHER_IMAGE_FIELD = "local";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_NAME = "object";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_DESCRIPTION = "public_access_description";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_LABEL = "label";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_HISTORY_NOTE = "history_note";
	public static String INSTITUTION_FIELD_VICTORIA_AND_ALBERT_MARKS = "marks";
	
	public static String SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_SEARCH_ROOT = "http://collection.britishmuseum.org/sparql.json";
	public static String SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_RESULTS = "results";
	public static String SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_BINDINGS = "bindings";
	public static String SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_LABEL = "label";
	public static String SEARCH_RESULTS_FIELD_BRITISH_MUSEUM_OBJ = "obj";	

	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_SEARCH_ROOT = "http://www.vam.ac.uk/api/json/museumobject/search";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_RESULTS = "meta";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_RECORDS = "records";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_FIELDS = "fields";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_NAME = "object";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_PLACE = "place";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_DATE_TEXT = "date_text";
	public static String SEARCH_RESULTS_FIELD_VICTORIA_AND_ALBERT_OBJECT_NUMBER = "object_number";
}