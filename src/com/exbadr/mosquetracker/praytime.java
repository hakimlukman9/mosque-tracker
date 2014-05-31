package com.exbadr.mosquetracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import reciver.alarm;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import JSON.Pray;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class praytime extends SherlockActivity {
	TextView s, z, a, m, i, title;
	Button setalarm;
	ArrayList<String> time;
	LocationManager lm;
	Location l;
	String provider;
	ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.praytime);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		provider = getIntent().getStringExtra("provider");
		time = new ArrayList<String>();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		l = lm.getLastKnownLocation(provider);
		title = (TextView) findViewById(R.id.title);
		s = (TextView) findViewById(R.id.subuhtime);
		z = (TextView) findViewById(R.id.dzuhurtime);
		a = (TextView) findViewById(R.id.Ashrtime);
		m = (TextView) findViewById(R.id.magribtime);
		i = (TextView) findViewById(R.id.ishatime);
		setalarm = (Button) findViewById(R.id.setalarm);
		pd = new ProgressDialog(this);
		pd.setMessage("Loading..");
		pd.setCancelable(false);
		pd.show();
		new praytimedownload().execute();
//		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
//				listener);

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
//			Toast.makeText(getApplicationContext(),
//					"Your connection maybe lost", Toast.LENGTH_SHORT).show();
//		}
//
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//			lm.removeUpdates(listener);
//			l = location;
//			praytimedownload ptd = new praytimedownload();
//			ptd.execute();
//		}
//	};

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			praytime.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	class praytimedownload extends AsyncTask<String, String, String> {
		Date date = new Date();
		
		@Override
		protected String doInBackground(String... params) {
			Log.i("lat",""+l.getLongitude()/15.0);
			
			Log.i("gmt", "" + -date.getTimezoneOffset());
			String url = "http://praytime.info/getprayertimes.php?lat="
					+ l.getLatitude() + "&lon=" + l.getLongitude() + "&d="
					+ date.getDate() + "&m=" + (date.getMonth() + 1) + "&y="
					+ (date.getYear() + 1900) + "&school=3" + "&gmt=" + -date.getTimezoneOffset()
					+ "&format=json";
			Pray p = new Pray();
			time = p.time(url, praytime.this);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			title.setText("Prayer times for " + date.getDate() + "-"
					+ (date.getMonth() + 1) + "-" + (date.getYear() + 1900)
					+ "\nYour Location :"
					+ getIntent().getStringExtra("location"));
			if (!time.isEmpty()) {
				s.setText("" + time.get(0));
				z.setText("" + time.get(1));
				a.setText("" + time.get(2));
				m.setText("" + time.get(3));
				i.setText("" + time.get(4));
				pd.dismiss();
				setalarm.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						boolean flag = false;
						for (int i = 0; i < 5; i++) {
							Calendar now = Calendar.getInstance();
							Calendar setcal = (Calendar) now.clone();
							setcal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time.get(i).charAt(0) + ""+ time.get(i).charAt(1)));
							Log.i("jam", ""+Integer.parseInt(time.get(i).charAt(0) + ""+ time.get(i).charAt(1)));
							setcal.set(Calendar.MINUTE,Integer.parseInt(time.get(i).charAt(3) + ""+ time.get(i).charAt(4)));
							Log.i("menit", ""+Integer.parseInt(time.get(i).charAt(3) + ""+ time.get(i).charAt(4)));
							setcal.set(Calendar.SECOND, 0);
							setcal.set(Calendar.MILLISECOND, 0);
							if (!(setcal.compareTo(now) <= 0)) {
								flag = true;
								Intent intent;
								PendingIntent pendingIntent;
								AlarmManager alarmManager;
								switch (i) {
								case 0:
									intent = new Intent(praytime.this, alarm.class);
									intent.putExtra("code", i);
									pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intent, 0);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.cancel(pendingIntent);
									Log.i("pesan", "dicencel dulu"+i);
									alarmManager.set(AlarmManager.RTC_WAKEUP,setcal.getTimeInMillis(),pendingIntent);
									Log.i("pesan", "sukses"+i);
									break;
								case 1:
									intent = new Intent(praytime.this,alarm.class);
									intent.putExtra("code", i);
									pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intent, 0);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.cancel(pendingIntent);
									Log.i("pesan", "dicencel dulu"+i);
									alarmManager.set(AlarmManager.RTC_WAKEUP,setcal.getTimeInMillis(),pendingIntent);
									Log.i("pesan", "sukses"+i);
									break;
								case 2:
									intent = new Intent(praytime.this,alarm.class);
									intent.putExtra("code", i);
									pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intent, 0);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.cancel(pendingIntent);
									Log.i("pesan", "dicencel dulu"+i);
									alarmManager.set(AlarmManager.RTC_WAKEUP,setcal.getTimeInMillis(),pendingIntent);
									Log.i("pesan", "sukses"+i);
									break;
								case 3:
									intent = new Intent(praytime.this,alarm.class);
									intent.putExtra("code", i);
									pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intent, 0);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.cancel(pendingIntent);
									Log.i("pesan", "dicencel dulu"+i);
									alarmManager.set(AlarmManager.RTC_WAKEUP,setcal.getTimeInMillis(),pendingIntent);
									Log.i("pesan", "sukses"+i);
									break;
								case 4:
									intent = new Intent(praytime.this,alarm.class);
									intent.putExtra("code", i);
									pendingIntent = PendingIntent.getBroadcast(getBaseContext(), i, intent, 0);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.cancel(pendingIntent);
									Log.i("pesan", "dicencel dulu"+i);
									alarmManager.set(AlarmManager.RTC_WAKEUP,setcal.getTimeInMillis(),pendingIntent);
									Log.i("pesan", "sukses"+i);
									break;
								default:
									break;
								}
							}
						}
						if(flag==false){
							Toast.makeText(praytime.this,"prayer times was over for this day", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(praytime.this,"prayer times has been set", Toast.LENGTH_SHORT).show();
						}
					}
				});
			} else {
				pd.dismiss();
				Toast.makeText(getApplicationContext(),"Your connection maybe lost", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

}
