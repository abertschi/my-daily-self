package ch.abertschi.mydailyself;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by abertschi on 24.02.17.
 */
public class CaptureService extends BroadcastReceiver {

    private static final String TAG = "CaptureService";
    private static final String FOLDER_NAME = "my-daily-self";
    private final Handler handler;

    public CaptureService() {
        Log.i(TAG, "creating service");
        handler = new Handler();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "unlocking device ...");
        final CameraHelper helper = new CameraHelper(context);
        helper.open(() -> handler.postDelayed(() -> helper.takePicture(getOutputMediaFile(), () -> helper.close()),
                500));
    }

    private static File getOutputMediaFile() {
        String secStore = System.getenv("EXTERNAL_SDCARD_STORAGE");
        File mediaStorageDir;
        if (secStore != null && !secStore.isEmpty()) {
            mediaStorageDir = new File(new File(secStore), "Pictures/" + FOLDER_NAME);
            System.out.println(mediaStorageDir.toString());

        } else {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), FOLDER_NAME
            );
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
}
