package com.example.monilandharia.musicplayer.utilities;

import java.util.concurrent.TimeUnit;

public class Utility {

    public static String getTime(int d)
    {
        int millis = d;
        String time = null;
        if(millis>=3600000) {
            time = String.format("%01d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
        else
        {
            time = String.format("%01d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis), // The change is in this line
                    TimeUnit.MILLISECONDS.toSeconds(millis) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }

        return time;
    }
}
