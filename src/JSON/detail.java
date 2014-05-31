package JSON;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class detail {
	Context mContecxt;
	public String phone (String url, Context c) {
		// this to make marker in your location and mosque in radius one
		// kilometer from your location
		mContecxt = c;
		InputStream is = null;
		String results = "", data = "-";
		HttpPost httppost = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();
			httppost = new HttpPost(url);
			// httppost.setEntity(new UrlEncodedFormEntity(null));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			
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
			results = sb.toString();
			Log.i("asdfg", results);
		} catch (Exception e) {
			
		}

		try {
			JSONObject jObject = new JSONObject(results);
			JSONObject result = jObject.getJSONObject("result");
			data= result.getString("international_phone_number").replace(" ", "");
			
			
		} catch (Exception e) {
		}
		return data;
	}
}
