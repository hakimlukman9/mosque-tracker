package com.exbadr.mosquetracker;

import java.util.ArrayList;

import JSON.direction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class route extends SherlockFragmentActivity {
	private GoogleMap mymap;
	ArrayList<LatLng> data;
//	ArrayList<Double> lat, lng;
	double l1, l2;
	LocationManager lm;
	SensorManager sm;
	Location l;
	String provider, name;
	PolylineOptions po;
	Marker myposition;

	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.direction);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mymap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map2)).getMap();
		po = new PolylineOptions();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		Intent i = getIntent();
		provider = i.getStringExtra("provider");
		l1 = i.getDoubleExtra("lat", 0);
		l2 = i.getDoubleExtra("lng", 0);
		Log.i("longlat", "" + l1);
		name = i.getStringExtra("name");

		l= lm.getLastKnownLocation(provider);
		
		Toast.makeText(getApplicationContext(), "Please wait ..",
				Toast.LENGTH_LONG).show();

		data = new ArrayList<LatLng>();
//		lat = new ArrayList<Double>();
		new routedownload().execute();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			lm.removeUpdates(listener);
			sm.unregisterListener(slistener);
			this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	LocationListener listener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			if(provider.equals(LocationManager.GPS_PROVIDER)){
				lm.removeUpdates(listener);
				route.this.provider = LocationManager.NETWORK_PROVIDER;
				lm.requestLocationUpdates(route.this.provider, 1000, 1, listener);
			}
		}

		public void onLocationChanged(Location location) {
			myposition.setPosition(new LatLng(location.getLatitude(), location
					.getLongitude()));
			CameraPosition cp = new CameraPosition.Builder()
			.target(new LatLng(location.getLatitude(), location.getLongitude()))
			.tilt(mymap.getCameraPosition().tilt)
			.zoom(mymap.getCameraPosition().zoom)
			.bearing(mymap.getCameraPosition().bearing)
			.build();
			mymap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
			if(provider.equals(LocationManager.NETWORK_PROVIDER)){
				if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
					lm.removeUpdates(listener);
					provider = LocationManager.GPS_PROVIDER;
					lm.requestLocationUpdates(provider, 1000, 1, listener);
					
				}
			}
		}
	};

	SensorListener slistener = new SensorListener() {
		
		public void onSensorChanged(int sensor, float[] values) {
			// TODO Auto-generated method stub
			if (sensor != SensorManager.SENSOR_ORIENTATION)
				return;
			int orientation = (int) (0+values[0]);
			CameraPosition cp = new CameraPosition.Builder()
			.target(mymap.getCameraPosition().target).tilt(mymap.getCameraPosition().tilt).zoom(mymap.getCameraPosition().zoom)
			.bearing(orientation).build();
			mymap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
			
		}
		
		public void onAccuracyChanged(int sensor, int accuracy) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public void onBackPressed() {
		lm.removeUpdates(listener);
		sm.unregisterListener(slistener);
		this.finish();
		super.onBackPressed();
	}
	
	class routedownload extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... params) {
			String url = "http://maps.googleapis.com/maps/api/directions/json?origin="
					+ l.getLatitude()
					+ ","
					+ l.getLongitude()
					+ "&destination="
					+ l1 + "," + l2 + "&mode=walking&sensor=false";
			direction d = new direction();
			data = d.direct(url, getApplicationContext());
//			lat = data.get(0);
//			lng = data.get(1);
			Log.i("tes", ""+data);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if (data.isEmpty()) {
				Toast.makeText(getApplicationContext(), "your internet connection may be lost", Toast.LENGTH_SHORT).show();
				route.this.finish();
			} else {

				for (int j = 0; j < data.size(); j++) {
					po.add(data.get(j)).width(2)
							.color(Color.BLUE).geodesic(true);
				}
				mymap.addPolyline(po);

				mymap.addMarker(new MarkerOptions()
						.position(new LatLng(l1, l2))
						.title(name)
						.snippet("Please, follow the path to get here")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.mosquee)));

				myposition = mymap.addMarker(new MarkerOptions()
						.position(new LatLng(l.getLatitude(), l.getLongitude()))
						.title("hi..!!")
						.snippet("You are here..!!")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.marker)));

				CameraPosition cp = new CameraPosition.Builder()
						.target(new LatLng(l.getLatitude(), l.getLongitude()))
						.zoom(14)
						.build();
				mymap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
				if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					provider = LocationManager.GPS_PROVIDER;
					lm.requestLocationUpdates(provider, 1000, 1, listener);
				} else {
					provider = LocationManager.NETWORK_PROVIDER;
					lm.requestLocationUpdates(provider, 1000, 1,listener);
				}
				sm.registerListener(slistener, SensorManager.SENSOR_ORIENTATION);
			}
			super.onPostExecute(result);
		}
		
		
		
	}
}
