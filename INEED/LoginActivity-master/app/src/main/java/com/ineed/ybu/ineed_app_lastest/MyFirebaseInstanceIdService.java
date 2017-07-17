package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by SERPİL on 16.05.2017.
 */

import android.os.StrictMode;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cz.msebera.android.httpclient.protocol.HTTP.CONTENT_TYPE;

/**
 * Created by tahakirca on 29/09/16.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    public final static String AUTH_KEY_FCM =
            "AAAAeQj7Ud0:APA91bHZeqOi96N0zC36obW1kWv7SigN-zcokRaM9_GgQ6DWenUomvu2w8cHiMY_lsfxB4373N9Sj2I6-2yRzQdJF4BuA515tztRIfIdLJ7FhC9z4_pqCophzpTb8leYt6stFGfXrXKz";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";


    // @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token: " + token);
    }

        // userDeviceIdKey is the device id you will query from your database

    public void sendAndroidNotification(String deviceToken, String message, String title) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject obj = new JSONObject();
        JSONObject msgObject = new JSONObject();
        try {
            msgObject.put("body", message);
            Log.d("Body", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            msgObject.put("title", title);
            Log.d("Title", title);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            msgObject.put("icon", R.drawable.need_logo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  msgObject.put("color", );

        try {
            obj.put("to", "/topics/ineed");
            Log.d("to", "/topics/ineed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("notification", msgObject);
            Log.d("Body", msgObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, obj.toString());
        Request request = new Request.Builder().url(API_URL_FCM).post(body)
                .addHeader("content-type", CONTENT_TYPE)
                .addHeader("authorization", "key=" + AUTH_KEY_FCM).build();
        Log.d("Request", body.toString());
        Response response = client.newCall(request).execute();
        Log.d("Notification response", response.body().string());


    }
}

