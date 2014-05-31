package JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import Decoder.PolylineDecoder;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class direction {
	Context mContecxt;
	public ArrayList<LatLng> direct (String url, Context c) {
		// this to make marker in your location and mosque in radius one
		// kilometer from your location
		mContecxt = c;
//		ArrayList<Double>lat = new ArrayList<Double>();
//		ArrayList<Double>lng = new ArrayList<Double>();
		ArrayList<LatLng> data = new ArrayList<LatLng>();
		InputStream is = null;
		String result = "";
		HttpPost httppost = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			httppost = new HttpPost(url);
			// httppost.setEntity(new UrlEncodedFormEntity(null));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.i("asdfg", result);
		} catch (Exception e) {
			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
		}

		try {

			JSONObject JObject = new JSONObject(result);
			JSONArray routes = JObject.getJSONArray("routes");
			JSONObject routes_data = routes.getJSONObject(0);
			JSONObject overview_polyline = routes_data.getJSONObject("overview_polyline");
			String point  = overview_polyline.getString("points");
			PolylineDecoder pld = new PolylineDecoder();
			data = pld.decodePoly(point);
//			JSONArray legs = routes_data.getJSONArray("legs");
//			JSONObject legs_data = legs.getJSONObject(0);
//			JSONArray steps = legs_data.getJSONArray("steps");
//			JSONObject steps_data = steps.getJSONObject(0);
//			JSONObject start_location = steps_data.getJSONObject("start_location");
//			lat.add(start_location.getDouble("lat"));lng.add(start_location.getDouble("lng"));
//			JSONObject end_location = steps_data.getJSONObject("end_location");
//			lat.add(end_location.getDouble("lat"));lng.add(end_location.getDouble("lng"));
//			
//			for(int i = 1 ; i<steps.length() ; i++){
//				steps_data = steps.getJSONObject(i);
//				end_location = steps_data.getJSONObject("end_location");
//				lat.add(end_location.getDouble("lat"));lng.add(end_location.getDouble("lng"));
//			}
			
		} catch (Exception e) {
			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
		}
//		data.add(lat);data.add(lng);
		return data;
	}
}
