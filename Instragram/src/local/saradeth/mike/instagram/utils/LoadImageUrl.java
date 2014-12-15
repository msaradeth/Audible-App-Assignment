package local.saradeth.mike.instagram.utils;

import java.io.IOException;
import java.util.ArrayList;

import local.saradeth.mike.instagram.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

/*
 Programmer:  Mike Saradeth
 Date: 12/10/2014
 */

/**
 * Download a list with the image URLs with the size pattern big, small, small
 *
 * @author Android Developer
 * @since 12/10/2014
 * @version 1.1
 */
public class LoadImageUrl extends AsyncTask<String, Integer, ArrayList<String>> {

	/** Activity to access to UI **/
	private final Activity activity;
	/** Progress dialog to display the progress **/
	private ProgressDialog progDialog;
	/** ArrayList with the image URLs **/
	ArrayList<String> alImageUrl = new ArrayList<String>();

	/**
	 * Constructor
	 *
	 * @param activity Activity to access to UI
	 * @param alImageUrl ArrayList where image URLs will be stored
	 */
	public LoadImageUrl(Activity activity, ArrayList<String> alImageUrl) {
		this.activity = activity;
		this.alImageUrl = alImageUrl;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// Create and display a progress dialog to inform the user about the
		// download
		String title = "Load Image URL";
		String message = "Loading image URL from Instagram ...";
		progDialog = ProgressDialog.show(this.activity, title, message, true, false);

	}

	@Override
	protected ArrayList<String> doInBackground(String... params) {
		String urlString = "https://api.instagram.com/v1/tags/selfie/media/recent?client_id=b9e0d20895a74ecc87cf33911032865c";
		String jsonString;

		try {
			// Create HttpClient and make GET request to the given URL
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(urlString));

			// If response not OK (Code 200)
			if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.d("responseCode", "responseCode != HttpStatus.SC_OK "
						+ httpResponse.getStatusLine().getStatusCode());
				return alImageUrl;
			} else {
				// Get the result on a String to parse as JSON object
				jsonString = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (IOException ioException) {
			Log.d("ioException", "ioException EntityUtils.toString(httpResponse.getEntity() "
					+ ioException.toString());
			return alImageUrl;
		}

		// Parse JSON String and load array list
		try {
			JSONObject jsonObject;
			int counter = 0;
			String url;
			alImageUrl.clear();

			JSONObject jsonObjects = new JSONObject(jsonString);
			JSONArray jsonArray = jsonObjects.getJSONArray("data");
			// Get the URL from JSON objects and store it on
			// the ArrayList
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);

				if (counter % 3 == 0) {  // big, small, small, repeat;
					url = jsonObject.getJSONObject("images")
							.getJSONObject("standard_resolution").getString("url");
				} else {
					url = jsonObject.getJSONObject("images")
							.getJSONObject("low_resolution").getString("url");
				}
				alImageUrl.add(url);
				counter = counter + 1;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return alImageUrl;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {

		// If the list is empty show a message
		if (result.isEmpty()) {
			((MainActivity) activity).alert("Impossible download picture from Instagram. Please try it again later.");
		}

		// Dismiss the dialog and update the list
		progDialog.dismiss();
		((MainActivity) activity).setListView(result);
	}

}
