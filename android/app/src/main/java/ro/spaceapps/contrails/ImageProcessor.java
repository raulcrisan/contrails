package ro.spaceapps.contrails;

import android.graphics.SurfaceTexture;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 *
 * Actual image processing algorithm.
 * Created by rares.barbantan on 23/04/16.
 */
public class ImageProcessor implements GuidedPhotoPreview.FrameCallback {
    @Override
    public void onCameraViewStarted(SurfaceTexture texture, int width, int height) {

    }

    @Override
    public Mat onCameraFrame(GuidedPhotoPreview.CameraFrame frame) {
        Mat original = frame.rgba();
        Core.flip(original, original, -1);

        return original;
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onValueUpdated(int value) {

    }
}
