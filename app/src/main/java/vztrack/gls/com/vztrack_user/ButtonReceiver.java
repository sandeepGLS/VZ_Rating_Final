package vztrack.gls.com.vztrack_user;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sandeep on 14/12/16.
 */

public class ButtonReceiver {
    public static PendingIntent getDismissIntent(int notificationId, Context context) {
        Log.e("IN DISMISS INTENT "," "+notificationId);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("NOTIFICATION_ID", notificationId);
        intent.putExtra("NOTIFICATION_DISMISS", true);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
    }
}
