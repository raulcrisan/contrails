package ro.spaceapps.contrails;

import android.graphics.SurfaceTexture;
import android.media.Image;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 *
 * Actual image processing algorithm.
 * Created by rares.barbantan on 23/04/16.
 */
public class ImageProcessor implements GuidedPhotoPreview.FrameCallback {
    private static final String TAG = ImageProcessor.class.getName();
    boolean shouldFlip = false;
    @Override
    public void onCameraViewStarted(SurfaceTexture texture, int width, int height) {
        //shame on me and android for doing this
        Log.d(TAG, "device: " + android.os.Build.DEVICE);
        shouldFlip = android.os.Build.DEVICE.equals("bullhead")
                || android.os.Build.DEVICE.equals("shamu");
    }

    @Override
    public Mat onCameraFrame(GuidedPhotoPreview.CameraFrame frame) {
        Mat original = frame.rgba();
        if(shouldFlip) {
            Core.flip(original, original, -1);
        }

        return original;
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onValueUpdated(int value) {

    }
}
