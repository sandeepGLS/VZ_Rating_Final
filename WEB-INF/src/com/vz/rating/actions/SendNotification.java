package com.vz.rating.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;



public class SendNotification {

	public final static String AUTH_KEY_FCM = "AAAA_A-ADr8:APA91bFpTDwMCvKtgjVuNOgMs9Lqt4jvPtpUAufsPi01opcT4m6LIsSju5TiuzfXvLJPfLFF-a4qQpAMwwI4YkjgO3Jc-8wzlJngm9dcDGVRLptJ1P1HxdLDhjCI_V0gzSC7Q3ii3zOtaR62uHDL1HCcLOZnMPJk2w";
	public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

	public static void pushFCMNotification(String DeviceIdKey) throws Exception {

	    String authKey = AUTH_KEY_FCM; // You FCM AUTH key
	    String FMCurl = API_URL_FCM;
System.out.println(authKey);
	    URL url = new URL(FMCurl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

	    conn.setUseCaches(false);
	    conn.setDoInput(true);
	    conn.setDoOutput(true);

	    conn.setRequestMethod("POST");
	    conn.setRequestProperty("Authorization", "key="+authKey);
	    conn.setRequestProperty("Content-Type", "application/json");

	    JSONObject data = new JSONObject();
	    data.put("to", DeviceIdKey.trim());
	    JSONObject info = new JSONObject();
	    info.put("title", "Please Rate Visited Plumber !!"); // Notification title
	    info.put("message", "We hope you had a good experience.Please take one minute to share your feedback"); // Notification body
	    data.put("data", info);
System.out.println(data);	    

	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.write(data.toString());
	    wr.flush();
	    wr.close();

	    int responseCode = conn.getResponseCode();
	    System.out.println("Response Code : " + responseCode);

	    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
	        response.append(inputLine);
	    }
	    in.close();

	}

	public static void main(String[] args) throws Exception {
		SendNotification objSendNitification = new SendNotification();
		objSendNitification.pushFCMNotification("eWVxZ6SMAX4:APA91bG_Djhhvs_5kqf1xWyKnh3_2dCVus9yCN-XtWnNw1JjsZnIiFSe_YGaQ9CUXiG6OFs00zKRCgdQtBmUV0YtZSGP8oloDGinpKNT1KlaACqb_DK5-mOkbNriPlBfxV3Ae9g3lrnk");
	}
}
