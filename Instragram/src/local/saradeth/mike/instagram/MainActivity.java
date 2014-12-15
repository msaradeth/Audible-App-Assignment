package local.saradeth.mike.instagram;

import java.util.ArrayList;

import local.saradeth.mike.instagram.adapters.Adapter;
import local.saradeth.mike.instagram.utils.LoadImageUrl;
import local.saradeth.mike.instragram.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/*
 Programmer:  Mike Saradeth
 Date: 12/10/2014
 Data Source: https://api.instagram.com/v1/tags/selfie/media/recent?client_id=b9e0d20895a74ecc87cf33911032865c

 Description:  Using the Instagram API, create an Android app that displays photos tagged with 
 hashtag #selfie and arranges them using the pattern: big, small, small, big, small small, and repeat. 
 Then implement one of the following features: Tap to enlarge, Drag and drop reordering, or Infinite scrolling. 
 Scrolling should be smooth so performance is essential. Be creative and have fun!
 */
/**
 * Main Activity, where a ListView will be display with all the image. Each
 * time that the user tap on any image a new Activity will be launched with
 * a big version of the selected image. 
 * 
 * @author Android Developer
 * @since 15-12-2014
 * @version 1.1
 */
public class MainActivity extends Activity {
	
	
	/** ArrayList where all the image's URLs will be stored **/
	private ArrayList<String> alImageUrl = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_list_view);
		getActionBar().hide(); 
		
		//To prevent errors or download again the URL list in case of screen rotations
		if(savedInstanceState != null){
			alImageUrl = savedInstanceState.getStringArrayList("ALL_URL");
		}
		//If there're not data on the ArrayList
		if(alImageUrl.size() == 0){
			// Load ImageUrl from Instagram
			LoadImageUrl loadImageUrl = new LoadImageUrl(this, this.alImageUrl);
			try {
				loadImageUrl.execute();
			} catch (Exception e) {
				loadImageUrl.cancel(true);
				alert("Problem loading from Instagram");
			}
		} else { // get ImageUrl from memory
			this.setListView(alImageUrl);
		}
		
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArrayList("ALL_URL", alImageUrl);
		
	}


	/**
	 * Toast a given message
	 * 
	 * @param msg Message to be displayed 
	 */
	public void alert(String msg) {
		Toast.makeText(this.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * Setup and create list view after data is loaded
	 * 
	 * @param myImageUrl ArrayList with the image URLs
	 */
	public void setListView(ArrayList<String> myImageUrl) {
		
		this.alImageUrl = myImageUrl;

		// Create the adapter
		Adapter adapter = new Adapter(this, R.layout.image_list_row, alImageUrl);
		ListView lvImage = (ListView) findViewById(R.id.image_list_view);
		lvImage.setAdapter(adapter);

		//OnItemClickListener to launch a new activity
		lvImage.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(getApplicationContext(), LargeImageActivity.class);
				intent.putExtra("imageUrl", alImageUrl.get(position));
				startActivity(intent);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

}
