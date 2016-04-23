package ro.spaceapps.contrails;

import android.graphics.SurfaceTexture;
import android.media.Image;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Actual image processing algorithm.
 * Created by rares.barbantan on 23/04/16.
 */
public class ImageProcessor implements GuidedPhotoPreview.FrameCallback {
    private static final String TAG = ImageProcessor.class.getName();
    private static final int SCALE = 4;
    private boolean shouldFlip = false;

    private Scalar lineColor = new Scalar(255,0,0);
    @Override
    public void onCameraViewStarted(SurfaceTexture texture, int width, int height) {
        //shame on me and android for doing this
        Log.d(TAG, "device: " + android.os.Build.DEVICE);
        shouldFlip = android.os.Build.DEVICE.equals("bullhead")
                || android.os.Build.DEVICE.equals("shamu");
    }

    @Override
    public Mat onCameraFrame(GuidedPhotoPreview.CameraFrame frame) {
        Mat original = frame.gray();
        if(shouldFlip) {
            Core.flip(original, original, -1);
        }

        Mat tmp = new Mat();
        Mat canny = new Mat();
        //Mat lines = new Mat();
        Mat hierarchy = new Mat();
        Mat pyr = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();

        Imgproc.resize(original, tmp, new Size(original.width()/SCALE, original.height()/SCALE));
        //Imgproc.dilate(tmp, tmp, new Mat());

        Imgproc.medianBlur(tmp, tmp, 3);
        Imgproc.pyrDown(tmp, pyr, new Size(tmp.cols()/2, tmp.rows()/2));
        Imgproc.pyrUp(pyr,tmp, tmp.size());

        Imgproc.Canny(tmp, canny, 50, 150);

        Imgproc.findContours(canny, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Log.d(TAG, "found contours " + contours.size());
        Imgproc.drawContours(tmp, contours, -1, lineColor);
        for(int i=0; i< contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            int h = contour.height();
            int w = contour.width();
            float ratio = (h<w) ? (float)h/w : (float)w/h;
            if(ratio < 0.05) {
                if(Imgproc.contourArea(contour) > 500) {
                    MatOfPoint approx = getApproximation(contour);
                    draw(tmp, approx, lineColor);

                }

            }
        }

        /*Imgproc.HoughLinesP(canny, lines, 1, Math.PI/180, 50, 100, 10);
        Log.d(TAG, "lines2: " + lines.rows());

        for(int i=0; i<lines.rows(); i++) {
            double vec[] = lines.get(i, 0);

            if(vec != null && vec.length ==4) {
                Imgproc.line(tmp, new Point(vec[0], vec[1]), new Point(vec[2], vec[3]), lineColor);
            }

        }*/
        Imgproc.resize(canny, original, original.size());


        return original;
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onValueUpdated(int value) {

    }

    MatOfPoint getApproximation(MatOfPoint contour) {
        MatOfPoint2f contour2f = new MatOfPoint2f();
        contour.convertTo(contour2f, CvType.CV_32FC2);
        double contourLength = Imgproc.arcLength(contour2f, true);
        MatOfPoint2f approx2f = new MatOfPoint2f();
        Imgproc.approxPolyDP(contour2f, approx2f, 3, true);
        MatOfPoint approx = new MatOfPoint();
        approx2f.convertTo(approx, CvType.CV_32S);
        return approx;
    }

    void draw(Mat dest, MatOfPoint matrix, Scalar color) {
        Point points[] = matrix.toArray();
        for(int j=0; j<points.length; j++) {
            Imgproc.line(dest, points[j], points[(j+1)%points.length], color, 2, Imgproc.LINE_AA,0);
        }
    }
}
