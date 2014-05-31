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

import android.content.Context;
import android.util.Log;

public class FindMosque {
	Context mContecxt;
	public ArrayList<ArrayList<String>> Search(String url, Context c) {
		// this to make marker in your location and mosque in radius one
		// kilometer from your location
		mContecxt = c;
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String>lat = new ArrayList<String>();
		ArrayList<String>lng = new ArrayList<String>();
		ArrayList<String>vicinity = new ArrayList<String>();
		ArrayList<String>reference = new ArrayList<String>();
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
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
//			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
//			name.add("wrong");
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
//			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
//			name.add("wrong");
		}

		try {

			JSONObject jObject = new JSONObject(result);
			JSONArray results = jObject.getJSONArray("results");

			for (int i = 0; i < results.length(); i++) {
				JSONObject data_results = results.getJSONObject(i);
				name.add(data_results.getString("name"));
				vicinity.add(data_results.getString("vicinity").replace("%2F", "/"));
				JSONObject geometry = data_results.getJSONObject("geometry");
				JSONObject location = geometry.getJSONObject("location");
				lat.add(location.getString("lat"));
				lng.add(location.getString("lng"));
				reference.add(data_results.getString("reference"));
			}
			
		} catch (Exception e) {
//			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
//			name.add("wrong");
		}
		data.add(name);data.add(lat);data.add(lng);data.add(vicinity);data.add(reference);
		return data;
	}
}
