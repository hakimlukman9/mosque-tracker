package com.exbadr.mosquetracker;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import Compass.Compass;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class qibla extends SherlockActivity{
	double qiblaa;
	String provider;
	LocationManager lm;
	SensorManager sm;
	ImageView qibl;
	ProgressDialog pd;
	Compass compas;
	Bitmap bmp;
	Location l, m;
	boolean sukses;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		qibl = (ImageView)findViewById(R.id.qibla);
		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		pd = new ProgressDialog(this);
		pd.setMessage("Loading..");
		pd.setCancelable(false);
		pd.show();	
		provider = getIntent().getStringExtra("provider");
		l = lm.getLastKnownLocation(provider);
		m = new Location("qibla");
		m.setLatitude(21.422518);
		m.setLongitude(39.82620);
		qiblaa = l.bearingTo(m);
		if(qiblaa<0){
			qiblaa = 360 + qiblaa;
		}
		new qibladownload().execute();
//		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
	}
	
//	LocationListener listener = new LocationListener() {
//		
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		public void onProviderEnabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		public void onProviderDisabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//			lm.removeUpdates(listener);
//			l = location;
//			m.setLatitude(21.422518);
//			m.setLongitude(39.82620);
//			qibla = l.bearingTo(m);
//			if(qibla<0){
//				qibla = 360 + qibla;
//			}
//			double atas = Math.sin(location.getLongitude()-Mlon);
//			double bawah = ( Math.cos(location.getLatitude()) * Math.tan(Mlat) ) - ( Math.sin(location.getLatitude()) * Math.cos(location.getLongitude()-Mlon));
//			qibla = Math.atan(atas/bawah);
//			if(location.getLatitude()<=Mlat && location.getLongitude()<=Mlon){
//	            qibla = Math.abs(Math.toDegrees(qibla));
//	        }else if(location.getLatitude()>=Mlat && location.getLongitude()<=Mlon){
//	            qibla = 180-Math.abs(Math.toDegrees(qibla));
//	        }else if(location.getLatitude()>=Mlat && location.getLongitude()>=Mlon){
//	            qibla = 180+Math.abs(Math.toDegrees(qibla));
//	        }else if(location.getLatitude()<=Mlat && location.getLongitude()>=Mlon){
//	            qibla = 360-Math.abs(Math.toDegrees(qibla));
//	        }
//			new qibladownload().execute();
//		}
//	};
	
	SensorListener slistener = new SensorListener(){

		public void onAccuracyChanged(int sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(int sensor, float[] values) {
			// TODO Auto-generated method stub
			if (sensor != SensorManager.SENSOR_ORIENTATION)
				return;
			if(sukses == true){
				int orientation = (int) (0-values[0]);
				compas.setDirection(orientation);
			}else{
				int orientation = (int) (qiblaa-values[0]);
				compas.setDirection(orientation);
			}
			
			
		}
		
	};
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			sm.unregisterListener(slistener);
			qibla.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onBackPressed() {
		sm.unregisterListener(slistener);
		qibla.this.finish();
	};
	
	class qibladownload extends AsyncTask<String, String, String>{
		byte [] b = null;
		Object content = null;
		@Override
		protected String doInBackground(String... params) {
			
			
				try {
					String urll = "http://muslimsalat.com/qibla_compass/200/"+qiblaa+".png";
					URL url = new URL(urll);
					Log.i("URLkiblat", urll);
					content = url.getContent();
					InputStream is = (InputStream)content;
					Bitmap bitmap = BitmapFactory.decodeStream(is);     
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); 
					b = baos.toByteArray();
					Log.i("b", ""+b);
					bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
					compas = new Compass(qibla.this, bmp);
					sukses = true;
				} catch (Exception e) {
					bmp = BitmapFactory.decodeResource(getResources(), R.drawable.kiblat);
					compas = new Compass(qibla.this, bmp);
					qiblaa = l.bearingTo(m);
					sukses = false;
				}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			qibla.this.setContentView(compas);
			pd.dismiss();
			sm.registerListener(slistener, SensorManager.SENSOR_ORIENTATION);
			super.onPostExecute(result);
		}
		
	}
}
