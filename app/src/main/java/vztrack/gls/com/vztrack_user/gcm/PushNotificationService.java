package vztrack.gls.com.vztrack_user.gcm;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by sandeep on 26/4/16.
 */
public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);
    }
}