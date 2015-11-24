package org.esiea.domergue_bastide.coursandroid;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class GetBeersService extends IntentService {

    final String TAG = "SERVICE";

    public GetBeersService() {
        super("GetBeersService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Thread service name: " + Thread.currentThread().getName());
            URL url = null;
            try {
                url = new URL("http://binouze.fabrigli.fr/bieres.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                    copyInputStreamToFile(conn.getInputStream(), new File(getCacheDir(), "bieres.json"));
                    Log.d(TAG, "bieres.json downloaded!");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(SecondActivity.BEERS_UPDATE));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
