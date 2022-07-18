package uk.rib.armouryoftheguardian;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * This downloads and returns the end user's selected weapon's specified image.
 * <p>
 * This is invoked on the background thread immediately after the end user presses on a weapon after
 * previously searching for one.
 * <p>
 * For 'extends AsyncTask'. the three types used by an asynchronous task are the following:
 * <ul>
 *      <li>The params, the type of the parameters sent to the task upon execution. This is the
 *      end user's selected weapon's image's URL as a string.</li>
 *      <li>The progress, the type of the progress units published during the background computation.
 *      This is void.</li>
 *      <li>The result, the type of the result of the background computation. This is bitmap of the
 *      end user's selected weapon's image.</li>
 * </ul>
 *
 * @version 22.3
 * @since 22.3
 */
public class DownloadWeaponImageTask extends AsyncTask<String, Void, Bitmap> {

    /**
     * This downloads the end user's selected weapon's specified image in the background.
     *
     * @param params The parameters of the task.
     * @return The end user's selected weapon's specified image.
     * @since 22.3
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        String weaponImageUrl = params[0];
        Bitmap weaponImage = null;

        try {
            InputStream InputStream = new java.net.URL(weaponImageUrl).openStream();
            weaponImage = BitmapFactory.decodeStream(InputStream);
            InputStream.close();
        } catch (IOException IOException) {
            IOException.printStackTrace();
        }
        return weaponImage;
    }
}
