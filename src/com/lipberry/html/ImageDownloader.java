package com.lipberry.html;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by aoe on 1/3/14.
 */
public class ImageDownloader extends AsyncTask {

    public interface ImageDownloadListener {
        public void onImageDownloadComplete(byte[] bitmapData);
        public void onImageDownloadFailed(Exception ex);
    }

    private ImageDownloadListener mListener = null;

    public ImageDownloader(ImageDownloadListener listener) {
        mListener = listener;
    }
   //04-03 20:13:07.481: E/image(18900): 

    protected Object doInBackground(Object... urls) {
        String url = "" + (String)urls[0];
        ByteArrayOutputStream baos = null;
        InputStream mIn = null;
        try {
            mIn = new java.net.URL(url).openStream();
            int bytesRead;
            byte[] buffer = new byte[64];
            baos = new ByteArrayOutputStream();
            while ((bytesRead = mIn.read(buffer)) > 0) {
                if (isCancelled()) return null;
                baos.write(buffer, 0, bytesRead);
            }
            return new AsyncTaskResult<byte[]>(baos.toByteArray());

        } catch (Exception ex) {
            return new AsyncTaskResult<byte[]>(ex);
        }
        finally {
            try {
                mIn.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onPostExecute(Object objResult) {
        AsyncTaskResult<byte[]> result = (AsyncTaskResult<byte[]>)objResult;
        if (isCancelled() || result == null) return;
        if (result.getError() != null) {
            mListener.onImageDownloadFailed(result.getError());
        }
        else if (mListener != null)
            mListener.onImageDownloadComplete(result.getResult());
    }
}
