package com.tomhedges.museumcompanion.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.tomhedges.museumcompanion.config.Constants;
import com.tomhedges.museumcompanion.dao.RemoteDataAccess;

import android.util.Log;

/**
 * Used to retrieve data from the remote location, and parse String data from the response.
 * 
 * Incorporates code sourced from:  http://www.mybringback.com/tutorial-series/12924/android-tutorial-using-remote-databases-php-and-mysql-part-1/
 * And also:  http://stackoverflow.com/questions/693997/how-to-set-httpresponse-timeout-for-android-in-java
 * 
 * @see			RemoteDataAccess
 * @author      Tom Hedges
 */

public class HttpRetriever {
 
    static InputStream is = null;
    //static JSONObject jObj = null;
    static String json = "";
    //private JSONObject errorDetails = new JSONObject();
    private StatusLine status;
 
    public HttpRetriever() {
 
    }
    
    public String makeHttpRequest(String url, String method, List<NameValuePair> params) {

        Log.d(HttpRetriever.class.getName(), "Attempting retrieval from: " + url + ", using method: " + method);
        // Making HTTP request
        try {
        	HttpParams httpParameters = new BasicHttpParams();
        	// Set the timeout in milliseconds until a connection is established.
        	// The default value is zero, that means the timeout is not used. 
        	int timeoutConnection = 5000;
        	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        	// Set the default socket timeout (SO_TIMEOUT) 
        	// in milliseconds which is the timeout for waiting for data.
        	int timeoutSocket = 5000;
        	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        	DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);

            // check for request method
            if(method == "POST"){
                // request method is POST
                // defaultHttpClient
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();                
                is = httpEntity.getContent();
 
            } else if(method == "GET"){
                // request method is GET
                String paramString = URLEncodedUtils.format(params, "utf-8");
                if (paramString.length() > 0) {
                	url += "?" + paramString;
                }
    			HttpGet httpGet = new HttpGet(url);
    			httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
                Log.d(RemoteDataAccess.class.getName(), "Attempting GET of data from: " + url);
                HttpResponse httpResponse = httpClient.execute(httpGet);
                Log.d(RemoteDataAccess.class.getName(), "Attempt has been executed!");

                status = httpResponse.getStatusLine();
                Log.d(RemoteDataAccess.class.getName(), "Response: " + status.getStatusCode() + " : " + status.getReasonPhrase());
        		HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }           

        } catch (ConnectTimeoutException e) {
        	Log.e(HttpRetriever.class.getName(), "Connection could not be made - timed out.");
            e.printStackTrace();
            //try {
			//	errorDetails.put("error", "Connection could not be made - timed out.");
			//} catch (JSONException doubleError) {
			//	doubleError.printStackTrace();
			//}
            //return errorDetails;
            return Constants.ERROR_CODE;
        } catch (UnsupportedEncodingException e) {
        	Log.e(HttpRetriever.class.getName(), "HTTP connection error - unsupported encoding exception.");
            e.printStackTrace();
            return Constants.ERROR_CODE;
        } catch (ClientProtocolException e) {
        	Log.e(HttpRetriever.class.getName(), "HTTP connection error - client protocol error.");
            e.printStackTrace();
            return Constants.ERROR_CODE;
        } catch (IOException e) {
        	Log.e(HttpRetriever.class.getName(), "HTTP connection error - ioException.");
            e.printStackTrace();
            return Constants.ERROR_CODE;
        } catch (Error e) {
        	Log.e(HttpRetriever.class.getName(), "HTTP connection error - unspecified error.");
            e.printStackTrace();
            return Constants.ERROR_CODE;
        }
        
    	Log.d(HttpRetriever.class.getName(), "Attempting to buffer results data...");
 
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder(is.available());
            String line = null;
        	Log.d(HttpRetriever.class.getName(), "About to read each line");
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                
                //UNCOMMENT THIS LINE FOR VERBOSE LOGGING OF RESPONSE DATA
            	//Log.d(HttpRetriever.class.getName(), line);
            }
        	Log.d(HttpRetriever.class.getName(), "Finished handling each line");
            is.close();
            json = sb.toString();
        	Log.d(HttpRetriever.class.getName(), "Data buffered");
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        if (status.getStatusCode() == 200 && json.length()>0) {
            
            // try parse the string to a JSON object
//            try {
//                jObj = new JSONObject(json);
//            } catch (JSONException e) {
//                Log.e("JSON Parser", "Error parsing data " + e.toString());
//            }
     
            return json;
        } else {
        	if (json.length()==0) {
        		Log.e(HttpRetriever.class.getName(), "Data returned - 0 length!");	
        	} else {
        		Log.e(HttpRetriever.class.getName(), "Not successful response - code: " + status.getStatusCode() + " : "+ status.getReasonPhrase());
        	}
            //try {
			//	errorDetails.put("error", status.getStatusCode() + " : " + status.getReasonPhrase() + " : " + json);
			//} catch (JSONException doubleError) {
			//	doubleError.printStackTrace();
			//}
            return Constants.ERROR_CODE;
        }
    }
}

