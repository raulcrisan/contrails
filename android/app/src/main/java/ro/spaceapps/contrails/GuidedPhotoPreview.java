package ro.spaceapps.contrails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by raresbarbantan on 05/01/16.
 */
public class GuidedPhotoPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = GuidedPhotoPreview.class.getName();

    private Camera mCamera;
    private int mFrameWidth, mFrameHeight;
    private byte mBuffer[];
    private Mat originalFrame;
    private CameraFrame currentFrame;
    private Bitmap cachedBitmap;
    private SurfaceTexture surfaceTexture;
    List<Camera.Size> mSupportedPreviewSizes;
    private int[] measure_spec = new int[2];
    boolean has_aspect_ratio = false;
    double aspect_ratio;
    private Matrix previewMatrix;
    private Scalar white = new Scalar(200, 200, 200, 200);


    private FrameCallback frameCallback;

    public GuidedPhotoPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        getHolder().addCallback(this);

    }

    public void setFrameCallback(FrameCallback callback) {
        this.frameCallback = callback;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        //do nothing
        Log.d(TAG, "surface created");
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
            requestLayout();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surface destroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }

        if (cachedBitmap != null) {
            cachedBitmap.recycle();
            cachedBitmap = null;
        }

        if (frameCallback != null) {
            frameCallback.onCameraViewStopped();
            frameCallback = null;
        }

        if (originalFrame != null) {
            originalFrame.release();
            originalFrame = null;
        }

        if (currentFrame != null) {
            currentFrame.release();
            currentFrame = null;
        }


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Log.d(TAG, "surface changed");
        if (getHolder().getSurface() == null || mCamera == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        Camera.Parameters params = setDocumentImageParams();


        params.setPreviewFormat(ImageFormat.NV21);


        // Configure image format. RGB_565 is the most common format.
        //List<Integer> formats = params.getSupportedPictureFormats();
        //if (formats.contains(ImageFormat.RGB_565))
        //    params.setPictureFormat(ImageFormat.RGB_565);
        //else
        params.setPictureFormat(ImageFormat.JPEG);
        params.setJpegQuality(100);

        // Choose the biggest picture size supported by the hardware
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        Collections.sort(pictureSizes, areaComparator);

        Camera.Size pictureSize = getBestPictureSize(pictureSizes);
        params.setPictureSize(pictureSize.width, pictureSize.height);

        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        Camera.Size bestFit = getBestPreview(pictureSizes, previewSizes);

        if (bestFit != null) {
            params.setPreviewSize(bestFit.width, bestFit.height);
            setAspectRatio((double) bestFit.width / (double) bestFit.height);
            previewMatrix = new Matrix();
            previewMatrix.setScale((float) w / bestFit.width, (float) h / bestFit.height);

        }

        mCamera.setParameters(params);

        params = mCamera.getParameters();

        mFrameWidth = params.getPreviewSize().width;
        mFrameHeight = params.getPreviewSize().height;
        Log.d(TAG, "sizes: " + w + ", " + h + ", " + mFrameWidth + ", " + mFrameHeight);
        int size = mFrameWidth * mFrameHeight;

        size = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
        mBuffer = new byte[size];


        originalFrame = new Mat(mFrameHeight + (mFrameHeight / 2), mFrameWidth, CvType.CV_8UC1);
        currentFrame = new CameraFrame(originalFrame, mFrameWidth, mFrameHeight);
        cachedBitmap = Bitmap.createBitmap(mFrameWidth, mFrameHeight, Bitmap.Config.ARGB_8888);

        // start preview with new settings
        try {
            mCamera.addCallbackBuffer(mBuffer);
            mCamera.setPreviewCallbackWithBuffer(this);
            //mCamera.setPreviewDisplay(getHolder());
            surfaceTexture = new SurfaceTexture(10);
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }

        if (frameCallback != null) {
            frameCallback.onCameraViewStarted(surfaceTexture, mFrameWidth, mFrameHeight);
        }
    }

    private Camera.Size getBestPictureSize(List<Camera.Size> pictureSizes) {
        Camera.Size bestFist = pictureSizes.get(0);

        // try force the 8 Megapixels resolution used for training
        // 2448X3264 (used in iPhone version as well)
        /*for (Camera.Size size : pictureSizes) {
            if(size.width == 2448 || size.height == 2448) {
                bestFist = size;
                break;
            }
        }*/
        return bestFist;
    }

    @NonNull
    private Camera.Parameters setDocumentImageParams() {
        Camera.Parameters params = mCamera.getParameters();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (params.isVideoStabilizationSupported()) {
                params.setVideoStabilization(true);
                Log.d(TAG, "Video stabilization is on.");
            }else{
                Log.d(TAG, "Video stabilization not supported.");
            }
        }


        /*if(params.isZoomSupported()){
            params.setZoom(Math.min(3, params.getMaxZoom()));
        }*/

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            Log.d(TAG, "Settings focus mode CONTINUOUS_VIDEO.");
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }

        params.setExposureCompensation(params.getMaxExposureCompensation() / 3);
        List<String> abs = params.getSupportedAntibanding();
        params.setAntibanding(abs.get(1));

        params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        return params;
    }


    private void setAspectRatio(double ratio) {
        if (ratio <= 0.0)
            throw new IllegalArgumentException();

        has_aspect_ratio = true;
        if (aspect_ratio != ratio) {
            aspect_ratio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        getMeasureSpec(measure_spec, widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(measure_spec[0], measure_spec[1]);
    }

    private void getMeasureSpec(int[] spec, int widthSpec, int heightSpec) {
        if (!this.has_aspect_ratio) {
            spec[0] = widthSpec;
            spec[1] = heightSpec;
            return;
        }
        double aspect_ratio = this.aspect_ratio;

        int previewWidth = MeasureSpec.getSize(widthSpec);
        int previewHeight = MeasureSpec.getSize(heightSpec);

        // Get the padding of the border background.
        int hPadding = getPaddingLeft() + getPaddingRight();
        int vPadding = getPaddingTop() + getPaddingBottom();

        // Resize the preview frame with correct aspect ratio.
        previewWidth -= hPadding;
        previewHeight -= vPadding;

        boolean widthLonger = previewWidth > previewHeight;
        int longSide = (widthLonger ? previewWidth : previewHeight);
        int shortSide = (widthLonger ? previewHeight : previewWidth);
        if (longSide > shortSide * aspect_ratio) {
            longSide = (int) ((double) shortSide * aspect_ratio);
        } else {
            shortSide = (int) ((double) longSide / aspect_ratio);
        }
        if (widthLonger) {
            previewWidth = longSide;
            previewHeight = shortSide;
        } else {
            previewWidth = shortSide;
            previewHeight = longSide;
        }

        // Add the padding of the border.
        previewWidth += hPadding;
        previewHeight += vPadding;

        spec[0] = MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY);
        spec[1] = MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY);
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        Log.d(TAG, "received frame of " + bytes.length);


        //convert to opencv
        originalFrame.put(0, 0, bytes);
        mCamera.addCallbackBuffer(mBuffer);

        Mat modified;

        if (frameCallback != null) {
            long start = System.currentTimeMillis();
            modified = frameCallback.onCameraFrame(currentFrame);
//            Log.d(TAG, "processed frame in " + (System.currentTimeMillis()-start));
        } else {
            modified = currentFrame.rgba();
        }

        drawHelpers(modified);

        Utils.matToBitmap(modified, cachedBitmap);

        Canvas canvas = getHolder().lockCanvas();
        if (canvas != null) {
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(cachedBitmap, previewMatrix, null);
            getHolder().unlockCanvasAndPost(canvas);
        }


        //originalFrame.release();

    }

    private void drawHelpers(Mat dest) {
        Imgproc.line(dest, new Point(dest.size().width / 2, 0), new Point(dest.size().width / 2, dest.size().height), white);
        Imgproc.line(dest, new Point(0, dest.size().height / 2), new Point(dest.size().width, dest.size().height / 2), white);
    }

    private Camera.Size getBestPreview(List<Camera.Size> pictureSizes, List<Camera.Size> previewSizes) {
        Camera.Size bestFit = previewSizes.get(0);
        Collections.sort(pictureSizes, areaComparator);
        Collections.sort(previewSizes, areaComparator);
        Camera.Size bestPicture = pictureSizes.get(0);

        for (Camera.Size size : previewSizes) {
            if ((double) size.width / (double) size.height == (double) bestPicture.width / (double) bestPicture.height) {
                bestFit = size;
                break;
            }
        }
        return bestFit;
    }

    public class CameraFrame implements CameraBridgeViewBase.CvCameraViewFrame {
        @Override
        public Mat gray() {
            return mYuvFrameData.submat(0, mHeight, 0, mWidth);
        }

        @Override
        public Mat rgba() {
            Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2RGBA_NV21, 4);
            return mRgba;
        }

        public CameraFrame(Mat Yuv420sp, int width, int height) {
            super();
            mWidth = width;
            mHeight = height;
            mYuvFrameData = Yuv420sp;
            mRgba = new Mat();
        }

        public void release() {
            mRgba.release();
        }

        private Mat mYuvFrameData;
        private Mat mRgba;
        private int mWidth;
        private int mHeight;
    }

    ;

    public interface FrameCallback {
        void onCameraViewStarted(SurfaceTexture texture, int width, int height);

        Mat onCameraFrame(CameraFrame frame);

        void onCameraViewStopped();

        void onValueUpdated(int value);
    }

    private Comparator<Camera.Size> areaComparator = new Comparator<Camera.Size>() {
        @Override
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            return rhs.height * rhs.width - lhs.height * lhs.width;
        }
    };


}
