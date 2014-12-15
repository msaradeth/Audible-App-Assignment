package local.saradeth.mike.instagram;

import local.saradeth.mike.instagram.adapters.Adapter.ViewHolder;
import local.saradeth.mike.instagram.utils.ImageCache;
import local.saradeth.mike.instragram.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/*
 Programmer:  Mike Saradeth
 Date: 12/10/2014
 */

/**
 * Activity where a simple image will be displayed.
 * 
 * @author Android Developer
 * @since 15-12-2014
 * @version 1.1
 */
public class LargeImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Going full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.large_image);

		//Get the image URL from intent
		Bundle args = getIntent().getExtras();
		String imageUrl = args.getString("imageUrl");

		ViewHolder holder = new ViewHolder();
		holder.urlString = imageUrl;
		holder.imageView = (ImageView) findViewById(R.id.large_image);
		
		// Look for the image on the cache memory
		ImageCache imageDraw = ImageCache.getInstance();
		imageDraw.drawImage(holder);

	}
}
