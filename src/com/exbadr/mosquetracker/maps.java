package com.exbadr.mosquetracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class maps extends SherlockFragmentActivity {
	Location l;
	LocationManager lm;
	String provider, namee, latt, lngg, mylocation;
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> lat = new ArrayList<String>();
	ArrayList<String> lng = new ArrayList<String>();
	private GoogleMap mymap;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maps);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mymap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		Intent i = getIntent();
		provider = i.getStringExtra("provider");
		namee = i.getStringExtra("namee");
		latt = i.getStringExtra("latt");
		lngg = i.getStringExtra("lngg");
		name = i.getStringArrayListExtra("name");
		lat = i.getStringArrayListExtra("lat");
		lng = i.getStringArrayListExtra("lng");

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		l = lm.getLastKnownLocation(provider);
		
		GeoPoint gp = new GeoPoint((int) (l.getLatitude() * 1E6),
				(int) (l.getLongitude() * 1E6));
		mylocation = ConvertPointToLocation(gp);
		mymap.addMarker(new MarkerOptions()
				.position(new LatLng(l.getLatitude(), l.getLongitude()))
				.title(mylocation)
				.snippet("Click this to get option")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

		if (namee != null) {

			mymap.addMarker(new MarkerOptions()
					.position(
							new LatLng(Double.parseDouble(latt), Double
									.parseDouble(lngg)))
					.title("This is "+namee)
					.snippet("Please click this to get option")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.mosquee)));

//			LatLngBounds llb = new LatLngBounds.Builder()
//					.include(new LatLng(l.getLatitude(), l.getLongitude()))
//					.include(new LatLng(Double.parseDouble(latt), Double.parseDouble(lngg)))
//					.build();

			CameraPosition cp = new CameraPosition.Builder()
					.target(new LatLng((l.getLatitude() + Double.parseDouble(latt)) / 2.0,(l.getLongitude() + Double.parseDouble(lngg)) / 2.0))
					.zoom(14)
					.build();

			mymap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
		
		} else {
			for (int j = 0; j < name.size(); j++) {
				mymap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.parseDouble(lat.get(j)),
										Double.parseDouble(lng.get(j))))
						.title("This is "+name.get(j))
						.snippet("Please click this to get option")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.mosquee)));
//				builder.include(new LatLng(Double.parseDouble(lat.get(j)), Double.parseDouble(lng.get(j))));
			}
//			LatLngBounds llb = builder.build();

			CameraPosition cp = new CameraPosition.Builder()
				.target(new LatLng(l.getLatitude(), l.getLongitude()))
				.zoom(13)
				.build();

			mymap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
			

			mymap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				public void onInfoWindowClick(Marker arg0) {
					if (!arg0.getTitle().equals("This is your position")) {
						Intent i = new Intent(maps.this, route.class);
						i.putExtra("provider", provider);
						i.putExtra("lat", arg0.getPosition().latitude);
						i.putExtra("lng", arg0.getPosition().longitude);
						i.putExtra("name", arg0.getTitle());
						startActivity(i);
					}
				}
			});
		}
		
		mymap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			public void onInfoWindowClick(Marker arg0) {
				final Marker arg = arg0;
				if (!arg0.getTitle().equals(mylocation)) {
					new AlertDialog.Builder(maps.this)
						.setTitle("Question")
						.setMessage("what do you want to do..?")
						.setNegativeButton("share", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
//								GeoPoint gp = new GeoPoint((int) (arg.getPosition().latitude * 1E6),
//										(int) (arg.getPosition().longitude * 1E6));
//								String mypoint = ConvertPointToLocation(gp);
								 Intent share = new Intent(Intent.ACTION_SEND);
								 share.putExtra(Intent.EXTRA_TEXT, arg.getTitle().replace("This is ", "")+" https://maps.google.com/maps?q="+arg.getPosition().latitude+"%2C"+arg.getPosition().longitude+" By #MosqueTrackerApp ");
								 share.setType("text/plain");
								 startActivity(Intent.createChooser(share, "Share this via"));
							}
						})
						.setPositiveButton("see the route", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								Intent i = new Intent(maps.this, route.class);
								i.putExtra("provider", provider);
								i.putExtra("lat", arg.getPosition().latitude);
								i.putExtra("lng", arg.getPosition().longitude);
								i.putExtra("name", arg.getTitle());
								startActivity(i);
							}
						})
						.show();
				}else{
					new AlertDialog.Builder(maps.this)
					.setTitle("Question")
					.setMessage("what do you want to do..?")
					.setNegativeButton("share", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							 Intent share = new Intent(Intent.ACTION_SEND);
							 GeoPoint gp = new GeoPoint((int) (arg.getPosition().latitude * 1E6),
										(int) (arg.getPosition().longitude * 1E6));
							 String mypoint = ConvertPointToLocation(gp);
							 share.putExtra(Intent.EXTRA_TEXT, "I'm at "+mypoint+" https://maps.google.com/maps?q="+arg.getPosition().latitude+"%2C"+arg.getPosition().longitude+" By #MosqueTrackerApp");
							 
							 share.setType("text/plain");
							 startActivity(Intent.createChooser(share, "Share this via"));
						}
					})
					.show();
				}
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			maps.this.finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// this method to get your location name
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

}
