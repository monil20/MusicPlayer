package com.example.monilandharia.musicplayer.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.MainActivity;
import com.example.monilandharia.musicplayer.R;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.services.MyService;

import java.util.ArrayList;
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

    public static Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }

    public static Bitmap getAlbumArtFromData(SongInfo song, Context context)
    {
        Bitmap img;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(song.getData());
        byte[] art = mediaMetadataRetriever.getEmbeddedPicture();
        if(art!=null) {
            img = BitmapFactory.decodeByteArray(art, 0, art.length);
        }
        else
            img = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder1);
        return img;
    }

    public static void playSong(SongInfo song, ArrayList<SongInfo> songList, int position, Context context){
        MainActivity.myService.setList(songList);
        MainActivity.myService.setCurrSong(song);
        MainActivity.myService.setCurrentIndex(position);
        Intent intent = new Intent(context, MyService.class);
//        Toast.makeText(getActivity(), "hello", Toast.LENGTH_SHORT).show();
        context.startService(intent);
    }
}
