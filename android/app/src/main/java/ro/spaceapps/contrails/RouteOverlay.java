package ro.spaceapps.contrails;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by rares.barbantan on 23/04/16.
 */
public class RouteOverlay extends View implements SensorEventListener{
    private static final String TAG = RouteOverlay.class.getName();
    private Paint routeColor = new Paint(Color.RED);

    private float[] acc = new float[3];
    private float[] compass = new float[3];
    SensorManager sm;

    public RouteOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rotation[] = new float[9];
        float identity[] = new float[9];
        float orientation[] = new float[3];
        boolean gotRotation = SensorManager.getRotationMatrix(rotation, identity, acc, compass);
        if(gotRotation){
            SensorManager.getOrientation(rotation, orientation);
            Log.d(TAG, "orientation: " + orientation[0] + ", "+orientation[1]+","+orientation[2]);
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            int r;
            if(w > h){
                r = h/2;
            }else{
                r = w/2;
            }
            float horizontalFOV = 67.57f;
            float verticalFOV = 53.29f;
            canvas.rotate((float)(0.0f- Math.toDegrees(orientation[2])));
            float curBearingToMW = 0;
// Translate, but normalize for the FOV of the camera -- basically, pixels per degree, times degrees == pixels
            float dx = (float) ( (canvas.getWidth()/ horizontalFOV) * (Math.toDegrees(orientation[0])-curBearingToMW));
            float dy = (float) ( (canvas.getHeight()/ verticalFOV) * Math.toDegrees(orientation[1])) ;

// wait to translate the dx so the horizon doesn't get pushed off
            canvas.translate(0.0f, 0.0f-dy);

// make our line big enough to draw regardless of rotation and translation
            canvas.drawLine(0f - canvas.getHeight(), canvas.getHeight()/2, canvas.getWidth()+canvas.getHeight(), canvas.getHeight()/2, routeColor);


// now translate the dx
            canvas.translate(0.0f-dx, 0.0f);

// draw our point -- we've rotated and translated this to the right spot already
            canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 8.0f, routeColor);
        }

        //canvas.drawCircle( orientation[0], 100 *target[1], 20, routeColor);
        invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType())
        {
            case Sensor.TYPE_ACCELEROMETER:
                acc = event.values.clone();
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
