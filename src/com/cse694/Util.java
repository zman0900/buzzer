package com.cse694;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class Util {

	public static String postJson(String json, String url) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				10000);
		// Timeout Limit
		HttpResponse response;
		String responseStr = null;
		try {
			HttpPost post = new HttpPost(url);
			StringEntity se = new StringEntity(json);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			response = client.execute(post); // Checking response
			Log.d("buzzer", "Response content-type: " + response.getEntity().getContentType().getValue());
			if (response != null && response.getEntity().getContentType().getValue().contains("application/json")) {
				responseStr = readStreamAsString(response.getEntity().getContent());
				Log.d("buzzer", "Got response from POST: "
						+ responseStr);
			}
		} catch (Exception e) {
			Log.e("buzzer", "Error sending POST");
			e.printStackTrace();
		}
		
		return responseStr;
	}
	
	private static String readStreamAsString(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int count;
			do {
				count = in.read(buffer);
				if (count > 0) {
					out.write(buffer, 0, count);
				}
			} while (count >= 0);
			return out.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"The JVM does not support the compiler's default encoding.",
					e);
		} catch (IOException e) {
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException ignored) {
			}
		}
	}

}
