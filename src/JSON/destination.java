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

public class destination {
	Context mContecxt;
	public ArrayList<Object> dest (String url, Context c) {
		// this to make marker in your location and mosque in radius one
		// kilometer from your location
		mContecxt = c;
		InputStream is = null;
		String result = "";
		ArrayList <Object> destination = new ArrayList<Object>();
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
		}

		try {

			JSONObject jObject = new JSONObject(result);
			JSONArray rows = jObject.getJSONArray("rows");
			JSONObject rows_object = rows.getJSONObject(0);
			JSONArray elements = rows_object.getJSONArray("elements");
			JSONObject elements_Object = elements.getJSONObject(0);
			JSONObject distance = elements_Object.getJSONObject("distance");
			destination.add(distance.get("text"));
			destination.add(distance.getInt("value"));
			
		} catch (Exception e) {
//			Toast.makeText(mContecxt, "pleace cek your internet connection and try again", Toast.LENGTH_SHORT).show();
		}
		
		return destination;
	}
}
