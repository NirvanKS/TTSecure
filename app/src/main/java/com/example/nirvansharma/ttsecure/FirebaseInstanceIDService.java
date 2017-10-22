package com.example.nirvansharma.ttsecure;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by Nirvan Sharma on 10/21/2017.
 */

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseInstanceIDService";
    private static final  String PROJECT_ID = "ttsecure-e1f8e";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String authorizedEntity = PROJECT_ID; // Project id from Google Developer Console
        String scope = "GCM"; // e.g. communicating using GCM, but you can use any
        // URL-safe characters up to a maximum of 1000, or
        // you can also leave it blank.
       // String token = InstanceID.getInstance(context).getToken(authorizedEntity,scope);
        String refreshedToken = null;
        try {
            refreshedToken = FirebaseInstanceId.getInstance(FirebaseApp.initializeApp(getApplicationContext())).getToken(authorizedEntity,scope);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("RegIDs");
        ref.push().setValue(token);
    }
}
