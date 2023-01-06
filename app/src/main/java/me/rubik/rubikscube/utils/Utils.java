package me.rubik.rubikscube.utils;

public class Utils {
    public static String formatTime(int time) {
        int minutes = ((time / 1000) / 60);
        time -= (minutes * 60) * 1000;
        int secs = (time / 1000);
        time -= secs * 1000;
        int miliSecs = time / 100;

        String formattedTime;

        if (minutes != 0) {
            String secsString = String.valueOf(secs);
            if (secsString.length() == 1) {
                secsString = "0" + secsString;
            }
            formattedTime = minutes + ":" + secsString + "." + miliSecs;
        } else {
            formattedTime = secs + "." + miliSecs;
        }

        return formattedTime;
    }
}
