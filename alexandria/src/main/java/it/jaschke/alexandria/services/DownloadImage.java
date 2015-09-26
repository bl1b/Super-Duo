package it.jaschke.alexandria.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by saj on 11/01/15.
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap bookCover = null;
        // Edit JG: do not try to load an image from an empty/not existing url
        if (urls != null && urls.length > 0 && !urls[0].isEmpty()) {
            String urlDisplay = urls[0];
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                bookCover = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }

        return bookCover;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

