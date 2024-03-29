package com.example.myapplication;

import android.content.Context;

public class GetTimeAgo {
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60000;
    private static final int HOUR_MILLIS = 3600000;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time, Context ctx) {

        if (time < 1000000000000L) {

            // If timestamp given in seconds, convert to millis
            time *= 1000;

        }

        long now = System.currentTimeMillis();

        if (time > now || time <= 0) {

            return null;

        }

        // TODO: localize
        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "şimdi";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "bir dakika önce";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return (diff / MINUTE_MILLIS + " dakika önce");
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "bir saat önce";
        } else if (diff < 24 * HOUR_MILLIS) {
            return (diff / HOUR_MILLIS + " saat önce");
        } else if (diff < 48 * HOUR_MILLIS) {
            return "dün";
        } else {
            return diff / DAY_MILLIS + " gün önce";
        }

    }
}
