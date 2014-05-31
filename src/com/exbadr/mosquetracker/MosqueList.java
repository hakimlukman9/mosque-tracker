package com.exbadr.mosquetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;

import JSON.FindMosque;
import JSON.destination;
import JSON.detail;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MosqueList extends SherlockActivity {
	LocationManager lm;
	Location l;
	TextView addrs;
	ListView lv;
	Button b;
	String provider, address;
	ArrayList<ArrayList<String>> data2 = new ArrayList<ArrayList<String>>();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> lat = new ArrayList<String>();
	ArrayList<String> lng = new ArrayList<String>();
	ArrayList<String> vicinity = new ArrayList<String>();
	ArrayList<String> reference = new ArrayList<String>();
	ArrayList<String> distance = new ArrayList<String>();
	ArrayList<String> phone = new ArrayList<String>();
	ArrayList<Integer> distancemeter = new ArrayList<Integer>();
	ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
	SimpleAdapter adapter;
	ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mosqlist);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		lv = (ListView) findViewById(R.id.listView1);
		addrs = (TextView) findViewById(R.id.address);
		b = (Button) findViewById(R.id.setalarm);
		Intent i = getIntent();
		address = i.getStringExtra("address");
		provider = i.getStringExtra("provider");
		name = i.getStringArrayListExtra("name");
		lat = i.getStringArrayListExtra("lat");
		lng = i.getStringArrayListExtra("lng");
		distance = i.getStringArrayListExtra("distance");
		vicinity = i.getStringArrayListExtra("vicinity");
		phone = i.getStringArrayListExtra("phone");
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		addrs.setText("My location : " + address.toString());
		
		if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			provider = LocationManager.GPS_PROVIDER;
			lm.requestLocationUpdates(provider, 1000, 1, listener2);
		}else{
			provider = LocationManager.NETWORK_PROVIDER;
			lm.requestLocationUpdates(provider, 1000, 1, listener2);
		}

		if (name.isEmpty()) {
			Toast.makeText(getApplicationContext(),
					"There are no mosque detected near you", Toast.LENGTH_SHORT)
					.show();
			b.setEnabled(false);
		} else {
			for (int j = 0; j < name.size(); j++) {
				Map<String, String> datum = new HashMap<String, String>(2);
				datum.put("mosque", name.get(j));
				datum.put("distance", vicinity.get(j) + "\n" + distance.get(j)
						+ " phone : " + phone.get(j));
				data.add(datum);
			}
			adapter = new SimpleAdapter(this, data,
					android.R.layout.simple_list_item_2, new String[] {
							"mosque", "distance" }, new int[] {
							android.R.id.text1, android.R.id.text2 });
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					final int x = arg2;
					new AlertDialog.Builder(MosqueList.this)
							.setTitle("Question..!!")
							.setMessage("what do you want to do..?")
							.setNeutralButton("call phone",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											if (!phone.get(x).equals("-")) {
												Intent i = new Intent(
														Intent.ACTION_CALL);
												i.setData(Uri.parse("tel:"
														+ phone.get(x)));
												Log.i("phn", phone.get(x));
												startActivity(i);
											} else {
												Toast.makeText(
														getApplicationContext(),
														"Invalid phone number",
														Toast.LENGTH_SHORT)
														.show();
											}
										}
									})
							.setPositiveButton("see on map",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Intent i = new Intent();
											i.setClass(MosqueList.this,
													maps.class);
											i.putExtra("namee", name.get(x));
											i.putExtra("latt", lat.get(x));
											i.putExtra("lngg", lng.get(x));
											i.putExtra("provider", provider);
											startActivity(i);
										}
									}).show();
				}
			});
		}
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				i.setClass(MosqueList.this, maps.class);
				i.putExtra("name", name);
				i.putExtra("lat", lat);
				i.putExtra("lng", lng);
				i.putExtra("provider", provider);
				startActivity(i);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent i = new Intent();
		switch (item.getItemId()) {
		case android.R.id.home:
			MosqueList.this.finish();
			lm.removeUpdates(listener);
			lm.removeUpdates (listener2);
			break;
		case R.id.menuprayertimes:
			lm.removeUpdates(listener);
			lm.removeUpdates(listener2);
			i.setClass(MosqueList.this, praytime.class);
			i.putExtra("location", addrs.getText().toString().replace("My Location : ", ""));
			i.putExtra("provider", provider);
			startActivity(i);
			break;
		case R.id.menuqibla :
			lm.removeUpdates(listener);
			lm.removeUpdates(listener2);
			i.setClass(MosqueList.this, qibla.class);
			i.putExtra("provider", provider);
			startActivity(i);
			break;
		case R.id.about :
			lm.removeUpdates(listener);
			lm.removeUpdates(listener2);
			i.setClass(MosqueList.this, About.class);
			startActivity(i);
			break;
		case R.id.refresh:
			lm.removeUpdates (listener2);
			pd = new ProgressDialog(MosqueList.this);
			pd.setMessage("Please wait..");
			pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					lv.setAdapter(null);
					lm.removeUpdates (listener);
					b.setEnabled(false);
				}
			});
			provider = LocationManager.GPS_PROVIDER;
			pd.show();
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
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
			if (provider.equals(LocationManager.NETWORK_PROVIDER)){
				lm.removeUpdates(listener);
				Toast.makeText(MosqueList.this, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
				MosqueList.this.finish();
			}
			pd.dismiss();
			lm.removeUpdates(listener);
			AlertDialog.Builder adb= new AlertDialog.Builder(MosqueList.this);
			adb.setTitle("warning")
				.setMessage("Your GPS Provider is disable, what do you want to do..?")
				.setPositiveButton("Use Network Provider", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						MosqueList.this.provider = LocationManager.NETWORK_PROVIDER;
						lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, listener);
						pd.show();
					}
				});
			adb.setNeutralButton("Try againt", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MosqueList.this.provider = LocationManager.GPS_PROVIDER;
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
					pd.show();
				}
			});
			adb.setNegativeButton("exit", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					lm.removeUpdates(listener);
					lm.removeUpdates(listener2);
					MosqueList.this.finish();
				}
			});
			adb.setCancelable(true);
			
			AlertDialog ad = adb.create();
			ad.show();
		}

		public void onLocationChanged(Location location) {
			lm.removeUpdates(listener);
			
			pd.setCancelable(false);
			address = "";
			GeoPoint gp = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			address = ConvertPointToLocation(gp);
			l= location;
			new background().execute();
		}
	};
	
	LocationListener listener2 = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			if(provider.equals(LocationManager.GPS_PROVIDER)){
				lm.removeUpdates(listener2);
				MosqueList.this.provider = LocationManager.NETWORK_PROVIDER;
				lm.requestLocationUpdates(MosqueList.this.provider, 1000, 1, listener2);
			}
		}

		public void onLocationChanged(Location location) {
			address = "";
			GeoPoint gp = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			address = ConvertPointToLocation(gp);
			addrs.setText("My Location : " + address);
			if(provider.equals(LocationManager.NETWORK_PROVIDER)){
				if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
					lm.removeUpdates(listener2);
					provider = LocationManager.GPS_PROVIDER;
					lm.requestLocationUpdates(provider, 1000, 1, listener2);
				}
			}
		}
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
		return address;
	}

	public void sort(ArrayList<Integer> distancemeter,
			ArrayList<String> distance,
			ArrayList<String> name,
			ArrayList<String> lat,
			ArrayList<String> lng,
			ArrayList<String> vicinity, 
			ArrayList<String> phone) {
		for (int i = 0; i < distancemeter.size(); i++) {
			int min = i;
			for (int j = i + 1; j < distancemeter.size(); j++) {
				if (distancemeter.get(j) < distancemeter.get(min)) {
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
			lng.set(i, lng.get(min));
			vicinity.set(i, vicinity.get(min));
			phone.set(i, phone.get(min));

			distancemeter.set(min, x);
			distance.set(min, d);
			name.set(min, n);
			lat.set(min, l1);
			lng.set(min, l2);
			vicinity.set(min, v);
			phone.set(min, p);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		MosqueList.this.finish();
		lm.removeUpdates(listener);
		lm.removeUpdates (listener2);
	}

	private class background extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			data.clear();
			data2.clear();
			name.clear();
			lat.clear();
			lng.clear();
			vicinity.clear();
			reference.clear();
			distance.clear();
			phone.clear();
			distancemeter.clear();

			String url = "https://maps.googleapis.com/maps/api/place/search/json?types=masjid%7Cmosque&location="
					+ l.getLatitude()
					+ ","
					+ l.getLongitude()
					+ "&radius=2000&sensor=true&key=AIzaSyDmqB8f0LEGk6jUgBnwHeJbJJUvpk7t2l0";
			FindMosque fm = new FindMosque();
			data2 = fm.Search(url, getApplicationContext());
			name = data2.get(0);
			lat = data2.get(1);
			lng = data2.get(2);
			vicinity = data2.get(3);
			reference = data2.get(4);
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
					distance.add((String) datadistance.get(0));
					distancemeter.add((Integer) datadistance.get(1));
				}

				detail dtl = new detail();
				for (int i = 0; i < reference.size(); i++) {
					url = "https://maps.googleapis.com/maps/api/place/details/json?reference="
							+ reference.get(i)
							+ "&sensor=true"
							+ "&key=AIzaSyDmqB8f0LEGk6jUgBnwHeJbJJUvpk7t2l0";
					phone.add(dtl.phone(url, getApplicationContext()));
				}
				Log.i("sort1", "" + distancemeter);
				sort(distancemeter, distance, name, lat, lng, vicinity, phone);
				Log.i("sort2", "" + distancemeter);
				for (int j = 0; j < name.size(); j++) {
					Map<String, String> datum = new HashMap<String, String>(2);
					datum.put("mosque", name.get(j));
					datum.put("distance",
							vicinity.get(j) + "\n" + distance.get(j)
									+ " phone : " + phone.get(j));
					MosqueList.this.data.add(datum);
				}
				adapter = new SimpleAdapter(MosqueList.this,
						MosqueList.this.data,
						android.R.layout.simple_list_item_2, new String[] {
								"mosque", "distance" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(name.isEmpty()){
				if(address.equals("")){
					Toast.makeText(getApplicationContext(),	"Your internet connection maybe lost", Toast.LENGTH_SHORT).show();
					
				
				} else {
					Toast.makeText(getApplicationContext(),	"There are no Mosque detected near you", Toast.LENGTH_LONG).show();
					addrs.setText("My Location : " + address);
				}
				lv.setAdapter(null);
				b.setEnabled(false);
			}else{
				addrs.setText("My Location : " + address);
				lv.setAdapter(adapter);
				b.setEnabled(true);
			}
			MosqueList.this.pd.dismiss();
			super.onPostExecute(result);
			if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
				provider = LocationManager.GPS_PROVIDER;
				lm.requestLocationUpdates(provider, 1000, 1, listener2);
			}else{
				provider = LocationManager.NETWORK_PROVIDER;
				lm.requestLocationUpdates(provider, 1000, 1, listener2);
			}
		}
	}
	
	@Override
	protected void onPause() {
		lm.removeUpdates(listener2);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			provider = LocationManager.GPS_PROVIDER;
			lm.requestLocationUpdates(provider, 1000, 1, listener2);
		}else{
			provider = LocationManager.NETWORK_PROVIDER;
			lm.requestLocationUpdates(provider, 1000, 1, listener2);
		}
		super.onResume();
	}

}
