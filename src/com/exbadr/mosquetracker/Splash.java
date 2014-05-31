package com.exbadr.mosquetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.maps.GeoPoint;

import JSON.FindMosque;
import JSON.destination;
import JSON.detail;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Splash extends SherlockActivity {
	LocationManager lm;
	Location l;
	String provider, address;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		provider = LocationManager.GPS_PROVIDER;
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);

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
			if(provider.equals(LocationManager.NETWORK_PROVIDER)){
				lm.removeUpdates(listener);
				Toast.makeText(Splash.this, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
				Splash.this.finish();
			}
			lm.removeUpdates(listener);
			new AlertDialog.Builder(Splash.this)
			.setTitle("Question..!!")
			.setMessage("your GPS provider is disable, what do you want to do..?")
			.setCancelable(false)
			.setNegativeButton("exit", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Splash.this.finish();
				}
			})
			.setPositiveButton("Use Network Provider", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Splash.this.provider = LocationManager.NETWORK_PROVIDER;
					lm.requestLocationUpdates(Splash.this.provider, 1000, 1, listener);
				}
			})
			.setNeutralButton("Try again", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Splash.this.provider = LocationManager.GPS_PROVIDER;
					lm.requestLocationUpdates(Splash.this.provider, 1000, 1, listener);
				}
			}).show();
			
			
		}

		public void onLocationChanged(Location location) {
			lm.removeUpdates(listener);
			address = "";
			GeoPoint gp = new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
			address = ConvertPointToLocation(gp);
			l = location;
			new background().execute();
		}
	};
	
	public void onBackPressed() {
		lm.removeUpdates(listener);
		this.finish();
	};
	
	public String ConvertPointToLocation(GeoPoint point) {
		String address = "";
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(
					point.getLatitudeE6() / 1E6, point.getLongitudeE6() / 1E6,
					1);
			
			
			for (int i = 0; i <= 2; i++)
				address += addresses.get(0).getAddressLine(i) + " ";

		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("aqwert", address );
		return address;
	}
	
	public void sort (ArrayList<Integer> distancemeter, 
			ArrayList<String> distance, 
			ArrayList<String> name, 
			ArrayList<String> lat, 
			ArrayList<String> lng, 
			ArrayList<String> vicinity, 
			ArrayList<String>phone){
		for (int i = 0 ; i<distancemeter.size() ; i++){
			int min = i;
			for (int j = i+1 ; j<distancemeter.size() ; j++ ){
				if (distancemeter.get(j) < distancemeter.get(min)){
					min = j;
				}
				
			}
			int x = distancemeter.get(i);
			String d = distance.get(i);
			String n = name.get(i); 
			String l1 = lat.get(i);
			String l2 = lng.get(i);
			String v = vicinity.get(i);
			String p = phone.get(i);
			
			distancemeter.set(i, distancemeter.get(min));
			distance.set(i, distance.get(min));
			name.set(i, name.get(min));
			lat.set(i, lat.get(min));
			lng.set(i,lng.get(min));
			vicinity.set(i, vicinity.get(min));
			phone.set(i, phone.get(min));
			
			distancemeter.set(min, x);
			distance.set(min, d);
			name.set(min, n);
			lat.set(min, l1);
			lng.set(min,l2);
			vicinity.set(min, v);
			phone.set(min, p);
		}
	}
	private class background extends AsyncTask<String, String, String>{
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> lat = new ArrayList<String>();
		ArrayList<String> lng = new ArrayList<String>();
		ArrayList<String> vicinity = new ArrayList<String>();
		ArrayList<String> reference = new ArrayList<String>();
		ArrayList<String> distance = new ArrayList<String>();
		ArrayList<String> phone = new ArrayList<String>();
		ArrayList<Integer> distancemeter = new ArrayList<Integer>();
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			String url = "https://maps.googleapis.com/maps/api/place/search/json?types=masjid%7Cmosque&location="
					+ l.getLatitude()
					+ ","
					+ l.getLongitude()
					+ "&radius=2000&sensor=true&key=AIzaSyDmqB8f0LEGk6jUgBnwHeJbJJUvpk7t2l0";
				
			FindMosque fm = new FindMosque();
			data = fm.Search(url, getApplicationContext());
			name = data.get(0);
			lat = data.get(1);
			lng = data.get(2);
			vicinity = data.get(3);
			reference = data.get(4);
			if (!name.isEmpty()) {
				destination d = new destination();
				for (int i = 0; i < lat.size(); i++) {
					url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
							+ l.getLatitude()
							+ ","
							+ l.getLongitude()
							+ "&destinations="
							+ lat.get(i) + "," + lng.get(i) + "&mode=walking&sensor=false";
					ArrayList<Object> datadistance = new ArrayList<Object>();
					datadistance = d.dest(url, getApplicationContext());
					distance.add((String)datadistance.get(0));
					distancemeter.add((Integer)datadistance.get(1));
				}
				
				detail dtl = new detail();
				for (int i = 0 ; i<reference.size() ; i++){
					url = "https://maps.googleapis.com/maps/api/place/details/json?reference="
							+ reference.get(i)
							+ "&sensor=true"
							+ "&key=AIzaSyDmqB8f0LEGk6jUgBnwHeJbJJUvpk7t2l0";
					phone.add(dtl.phone(url, getApplicationContext()));
				}
				Log.i("sort1", ""+distancemeter);
				sort (distancemeter, distance, name, lat, lng, vicinity, phone );
				Log.i("sort2", ""+distancemeter);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Log.i("bck", ""+name+"\n"+address);
			if(name.isEmpty()&&address.equals("")){
					Toast.makeText(Splash.this, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
					
			}
				Intent i = new Intent();
				i.putExtra("address", address);
				i.putExtra("name", name);
				i.putExtra("lat", lat);
				i.putExtra("lng", lng);
				i.putExtra("vicinity", vicinity);
				i.putExtra("distance", distance);
				i.putExtra("phone", phone);
				i.putExtra("provider", provider);
				i.setClass(Splash.this, MosqueList.class);
				startActivity(i);
				Splash.this.finish();
			
			super.onPostExecute(result);
		}
	}
}
