package com.example.myapplication.utilities;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.domain.Notification;

public final class NotificationsUtils {
    private static final NotificationsUtils INSTANCE = new NotificationsUtils();

    private NotificationsUtils() {} // Private constructor to prevent instantiation

    public static NotificationsUtils getInstance() {
        return INSTANCE;
    }

    public void showErrToast(Context context, String message) {
        showToast(context, message, R.drawable.baseline_error_24);
        vibratePhone(context);
    }

    public void showWarningToast(Context context, String message) {
        showToast(context, message, R.drawable.baseline_warning_24);
    }

    public void showSuccessToast(Context context, String message) {
        showToast(context, message, R.drawable.baseline_check_circle_24);
        vibratePhone(context);
    }

    public void showNotification(Context context, String message) {
        showToast(context, message, R.drawable.outline_notifications_none_24);
        vibratePhone(context);
    }

    public void showToast(Context context, String message, int iconRes) {
        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View layout = inflater.inflate(R.layout.toast_layout, null);

        ImageView imageView = layout.findViewById(R.id.toastIcon);
        TextView textView = layout.findViewById(R.id.toastText);

        imageView.setImageResource(iconRes);
        textView.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public void showServerNotification(Context context,Notification notification){
        showNotificationToast(context,notification);
        vibratePhone(context);
    }

    public void showNotificationToast(Context context, Notification notification) {
        LayoutInflater inflater = LayoutInflater.from(context);
        android.view.View layout = inflater.inflate(R.layout.server_notification_item, null);

        TextView textTitle = layout.findViewById(R.id.text_title);
        TextView textBody = layout.findViewById(R.id.text_body);
        textBody.setText(notification.getBody());
        textTitle.setText(notification.getTitle());

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.show();
    }

    public void vibratePhone(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            // Deprecated in API 26
            vibrator.vibrate(200);
        }
    }

    public void playSound() {
        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60);
        toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 150);
    }
}