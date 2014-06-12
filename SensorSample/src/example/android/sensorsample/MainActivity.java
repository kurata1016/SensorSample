package example.android.sensorsample;

import example.android.temperaturesample.R;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
	public final static String TAG = "SensorTest2";
	protected final static double RAD2DEG = 180 / Math.PI;

	SensorManager mgr;

	float[] rotationMatrix = new float[9];
	float[] gravity = new float[3];
	float[] geomagnetic = new float[3];
	float[] attitude = new float[3];

	TextView azimuthText;
	TextView pitchText;
	TextView rollText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViews();
		initSensor();
	}

	@Override
	public void onResume() {
		super.onResume();
		mgr.registerListener(this, mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
		mgr.registerListener(this, mgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onPause() {
		super.onPause();
		mgr.unregisterListener(this);
	}

	private void findViews() {
		azimuthText = (TextView) findViewById(R.id.azimuth);
		pitchText = (TextView) findViewById(R.id.pitch);
		rollText = (TextView) findViewById(R.id.roll);
	}

	private void initSensor() {
		mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case Sensor.TYPE_MAGNETIC_FIELD:
			geomagnetic = event.values.clone();
			break;
		case Sensor.TYPE_ACCELEROMETER:
			gravity = event.values.clone();
			break;
		}

		if (geomagnetic != null && gravity != null) {

			SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic);

			SensorManager.getOrientation(rotationMatrix, attitude);

			azimuthText.setText(Integer.toString((int) (attitude[0] * RAD2DEG)));
			pitchText.setText(Integer.toString((int) (attitude[1] * RAD2DEG)));
			rollText.setText(Integer.toString((int) (attitude[2] * RAD2DEG)));

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
