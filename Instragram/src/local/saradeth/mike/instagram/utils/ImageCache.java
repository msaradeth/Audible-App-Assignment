package local.saradeth.mike.instagram.utils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import local.saradeth.mike.instagram.adapters.Adapter;
import local.saradeth.mike.instagram.adapters.Adapter.ViewHolder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/*
 Programmer:  Mike Saradeth
 Date: 12/10/2014
 */

/**
 * Download a Bitmap for each URL to act like cache memory. This class
 * implements Singleton pattern.
 * 
 * @author Android Developer
 * @since 15-12-2014
 * @version 1.1
 */
public class ImageCache {
	
	/** ImageCache instance for singleton class **/
	private static ImageCache instance = null;
	/** Map where each Bitmap and the URL associated will be stored **/
	private Map<String, Bitmap> imageCache = new HashMap<String, Bitmap>();

	/**
	 * Protected constructor, exists only to defeat instantiation
	 */
	protected ImageCache(){

	}
	
	/**
	 * Get the instance of ImageCache as a Singleton class
	 * 
	 * @return ImageCache instance
	 */
	public static ImageCache getInstance(){
		if(instance == null){
			instance = new ImageCache();
		} 
		return instance;
	}
	
	
	/**
	 * Draw an image. If the image was previously downloaded is taken from the
	 * Map, it not is downloaded via ImageTask
	 * 
	 * @param holder ViewHolder with the image's data that will be draw
	 */
	public void drawImage(ViewHolder holder) {
		// if image Bitmap exist in memory use it
		if (imageCache.get(holder.urlString) != null) {
			holder.imageView.setImageBitmap(imageCache.get(holder.urlString));
		} else {
			// If not, get image Bitmap from image URL via AsyncTask
			new ImageTask().execute(holder);
		}
	}

	/**
	 * Download an image and save it on the Map via AsyncTask
	 * 
	 */
	private class ImageTask extends AsyncTask<Adapter.ViewHolder, Void, ViewHolder> {

		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {

			ViewHolder holder = params[0];
			try {
				//Create the URL object with the image URL and try to download it
				URL imageURL = new URL(holder.urlString);
				holder.bitMap = BitmapFactory.decodeStream(imageURL.openStream());
			} catch (IOException e) {
				Log.e("error", "Downloading Image Failed");
			}

			return holder;
		}

		@Override
		protected void onPostExecute(ViewHolder result) {
			super.onPostExecute(result);
			//If the image was successfully downloaded save it on the Map
			if (result.bitMap != null) {
				result.imageView.setImageBitmap(result.bitMap);
				synchronized (imageCache) {
					imageCache.put(result.urlString, result.bitMap);
				}
			}
		}
	}

	/**
	 * Get the Map with the String URL, Bitmap pair
	 * 
	 * @return imageCache
	 */
	public Map<String, Bitmap> getImageCache() {
		return imageCache;
	}

    /**
     * Sets a Map with the String URL, Bitmap pair
     * 
     * @param imageCache The map to set
     */
	public void setImageCache(Map<String, Bitmap> imageCache) {
		this.imageCache = imageCache;
	}

}
