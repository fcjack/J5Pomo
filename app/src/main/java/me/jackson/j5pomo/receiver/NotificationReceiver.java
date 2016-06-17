package me.jackson.j5pomo.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.jackson.j5pomo.R;
import me.jackson.j5pomo.activities.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private static final Integer NOTIFICATION_ID = 1;
    private static final Integer NOTIFICATION_PENDING_INTENT = 2;


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Notification.Builder builder = null;

        if ("END_POMODORO".equals(action)) {
            builder = new Notification.Builder(context)
                    .setContentTitle("Pomodoro changed")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("One Pomodoro has finished!!")
                    .setAutoCancel(true);
        } else if ("COMPLETE_POMODORO".equals(action)) {
            builder = new Notification.Builder(context)
                    .setContentTitle("Pomodoro completed")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("All Pomodoro to task has finished!!")
                    .setAutoCancel(true);
        }

        Intent it = new Intent(context, MainActivity.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(it);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(NOTIFICATION_PENDING_INTENT, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
