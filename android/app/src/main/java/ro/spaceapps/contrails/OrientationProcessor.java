package ro.spaceapps.contrails;

import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by rares.barbantan on 23/04/16.
 */
public class OrientationProcessor implements GuidedPhotoPreview.FrameCallback, SensorEventListener {
    private static final String TAG = OrientationProcessor.class.getName();
    private boolean shouldFlip = false;
    private SensorManager sm;
    private float[] gyro = new float[3];
    private float[] acc = new float[3];
    private float[] compass = new float[3];
    private Scalar lineColor = new Scalar(255,0,0);



    public OrientationProcessor(SensorManager sm) {
        this.sm = sm;
    }
    @Override
    public void onCameraViewStarted(SurfaceTexture texture, int width, int height) {
        shouldFlip = android.os.Build.DEVICE.equals("bullhead")
                || android.os.Build.DEVICE.equals("shamu");
        //sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        //        SensorManager.SENSOR_DELAY_NORMAL);
        //sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
        //        SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public Mat onCameraFrame(GuidedPhotoPreview.CameraFrame frame) {
        Mat original = frame.gray();
        if(shouldFlip) {
            Core.flip(original, original, -1);
        }

        // compute rotation matrix
        float rotation[] = new float[9];
        float identity[] = new float[9];
        boolean gotRotation = SensorManager.getRotationMatrix(rotation, identity, acc, compass);
        if (gotRotation) {
            float cameraRotation[] = new float[9];
            // remap such that the camera is pointing along the positive direction of the Y axis
            //SensorManager.remapCoordinateSystem(rotation, SensorManager.AXIS_X,
            //        SensorManager.AXIS_Z, cameraRotation);

            // orientation vector
            float orientation[] = new float[3];
            SensorManager.getOrientation(cameraRotation, orientation);
            int x = (int)(100 + 100* orientation[0]);
            int y = (int)(100 + 100* orientation[0]);
            Log.d(TAG, "orientation " + (100+100*orientation[0]) + " , " + (100+100*orientation[1]) +" , "+ orientation[2]);
            //canvas.rotate((float)(0.0f- Math.toDegrees(orientation[2])));
            // Translate, but normalize for the FOV of the camera -- basically, pixels per degree, times degrees == pixels
            //float dx = (float) ( (canvas.getWidth()/ horizontalFOV) * (Math.toDegrees(orientation[0])-curBearingToMW));
            //float dy = (float) ( (canvas.getHeight()/ verticalFOV) * Math.toDegrees(orientation[1])) ;
            Imgproc.line(original, new Point(100+100*orientation[0],100+orientation[1]), new Point(200+orientation[0]*10,200+orientation[1]*10), lineColor);
        }
        return original;
    }

    @Override
    public void onCameraViewStopped() {
        sm.unregisterListener(this);
    }

    @Override
    public void onValueUpdated(int value) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                acc = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE:
                gyro = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                compass = event.values.clone();
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
