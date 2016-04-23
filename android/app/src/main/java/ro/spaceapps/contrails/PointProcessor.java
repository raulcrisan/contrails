package ro.spaceapps.contrails;

import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 */
public class PointProcessor implements GuidedPhotoPreview.FrameCallback, SensorEventListener {
    boolean shouldFlip = false;
    SensorManager sm;
//    AstronomerModel astro;

    public PointProcessor(SensorManager sm){


// Register this class as a listener for the accelerometer sensor
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
// ...and the orientation sensor
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);


//        astro = new AstronomerModelImpl(new RealMagneticDeclinationCalculator());
//        astro.setLocation(new LatLong(userLat, userLon));

    }

    @Override
    public void onCameraViewStarted(SurfaceTexture texture, int width, int height) {

    }

    double lat = 46.1736;
    double lon = 22.6306;
    double alt = 11887.2;

    double userLat = 46.776444821320034;
    double userLon = 23.6039481125772;
    double userAlt = 336.872;

    double CIRCLE_RATIO = 0.05;
    @Override
    public Mat onCameraFrame(GuidedPhotoPreview.CameraFrame frame) {
        Mat original = frame.rgba();
        if(shouldFlip) {
            Core.flip(original, original, -1);
        }



        Imgproc.circle(original, new Point(original.width()/2, original.height()/2), (int) (original.width()*CIRCLE_RATIO),  new Scalar( 255, 0, 0, 255 ),-1 );

//        astro.getPointing().getLineOfSight();

        Imgproc.putText(original, "azimuth " +azimuth,new Point(original.width()/2, original.height()/2 + 40),0,1,new Scalar(255,255,255, 255) );
        Imgproc.putText(original, "pitch " +pitch,new Point(original.width()/2, original.height()/2 + 80),0,1,new Scalar(255,255,255, 255) );
        Imgproc.putText(original, "roll " +roll,new Point(original.width()/2, original.height()/2 + 120),0,1,new Scalar(255,255,255,255) );


        return original;
    }



    float[] inR = new float[16];
    float[] I = new float[16];
    float[] gravity = new float[3];
    float[] geomag = new float[]{-1.5625f,-26.75f, 27.625f};
    float[] orientVals = new float[3];

    double azimuth = 0;
    double pitch = 0;
    double roll = 0;

    public void onSensorChanged(SensorEvent sensorEvent) {
        // If the sensor data is unreliable return
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        // Gets the value of the sensor that has been changed
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomag = sensorEvent.values.clone();
                break;
        }
        // If gravity and geomag have values then find rotation matrix
        if (gravity != null && geomag != null) {

//            astro.setPhoneSensorValues(new Vector3(gravity), new Vector3(geomag));

            // checks that the rotation matrix is found
            boolean success = SensorManager.getRotationMatrix(inR, I,
                    gravity, geomag);
            if (success) {
                SensorManager.getOrientation(inR, orientVals);
                azimuth = Math.toDegrees(orientVals[0]);
                pitch = Math.toDegrees(orientVals[1]);
                roll = Math.toDegrees(orientVals[2]);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onValueUpdated(int value) {

    }
}
