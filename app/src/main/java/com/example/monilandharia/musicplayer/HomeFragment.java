package com.example.monilandharia.musicplayer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.monilandharia.musicplayer.adapters.AlbumsAdapter;
import com.example.monilandharia.musicplayer.adapters.ArtistsAdapter;
import com.example.monilandharia.musicplayer.adapters.RecentlyAddedAdapter;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerRecentlyAdded, recyclerArtists, recyclerAlbums, recyclerTracks, recyclerGenres;
    private RecentlyAddedAdapter adapterRecentlyAdded;
    private AlbumsAdapter adapterAlbums;
    private ArtistsAdapter artistsAdapter;
    private Cursor trackCursor, albumCursor, artistCursor;
    private ArrayList recentlyAdded, albums, tracks, genres, songs, artists;
    private RecyclerView.LayoutManager layoutManager, layoutManager1, layoutManager2;

    String[] trackProjection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DURATION
    };

    String[] albumProjection = {
            "_id",
            "album",
            "artist",
            "artist_id",
            "numsongs",
            "minyear"
    };

    String[] artistProjection = {
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
    };

    String[] genreProjection = {
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
    };

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView();
//        hello2();
        initializeRecentlyAdded(view);
        initializeAlbums(view);
        initializeArtists(view);
        return view;
    }

    private void hello() {
        int index;
        long genreId;
        Uri uri;
        Cursor genrecursor;
        Cursor tempcursor;
        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};

        genrecursor = getContext().getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, proj1, null, null, null);
        if (genrecursor.moveToFirst()) {
            do {
                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
                Log.i("GENRENAME", genrecursor.getString(index));

                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
                genreId = Long.parseLong(genrecursor.getString(index));
                uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);

                tempcursor = getContext().getContentResolver().query(uri, proj2, null,null,null);
                Log.i("NUMBERGENRE", tempcursor.getCount() + "");
                if (tempcursor.moveToFirst()) {
                    do {
                        index = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                        Log.i("Tag-Song name", tempcursor.getString(index));
                    } while(tempcursor.moveToNext());
                }
            } while(genrecursor.moveToNext());
        }
    }

    private void hello2(){

    }

    private void initView() {
        trackCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                trackProjection,
                null,
                null,
                null);
        albumCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                albumProjection,
                null,
                null,
                null);
        artistCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                artistProjection,
                null,
                null,
                null
        );
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    }

    private void initializeRecentlyAdded(View view) {
        recyclerRecentlyAdded = view.findViewById(R.id.recyclerRecentlyAdded);
        recyclerRecentlyAdded.setHasFixedSize(true);
        recyclerRecentlyAdded.setLayoutManager(layoutManager);
        recyclerRecentlyAdded.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recentlyAdded = prepareSongs();
        adapterRecentlyAdded = new RecentlyAddedAdapter(getActivity().getApplicationContext(), recentlyAdded, new RecentlyAddedAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(SongInfo song, int position) {
//                firstLaunch = false;
//                changeSelectedSong(position);
//                prepareSong(song);
            }
        });
        recyclerRecentlyAdded.setAdapter(adapterRecentlyAdded);
    }

    private void initializeAlbums(View view) {
        recyclerAlbums = view.findViewById(R.id.recyclerAlbums);
        recyclerAlbums.setHasFixedSize(true);
        recyclerAlbums.setLayoutManager(layoutManager1);
        recyclerAlbums.setOverScrollMode(View.OVER_SCROLL_NEVER);
        albums = prepareAlbums();
        adapterAlbums = new AlbumsAdapter(getActivity().getApplicationContext(), albums, new AlbumsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(AlbumInfo albumInfo, int position) {

            }
        });
        recyclerAlbums.setAdapter(adapterAlbums);
    }

    private void initializeArtists(View view) {
        recyclerArtists = view.findViewById(R.id.recyclerArtists);
        recyclerArtists.setHasFixedSize(true);
        recyclerArtists.setLayoutManager(layoutManager2);
        recyclerArtists.setOverScrollMode(View.OVER_SCROLL_NEVER);
        artists = prepareArtists();
        artistsAdapter = new ArtistsAdapter(getActivity().getApplicationContext(), artists, new ArtistsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(ArtistInfo album, int position) {

            }
        });
        recyclerArtists.setAdapter(artistsAdapter);
    }


//    private void prepareSong(SongInfo song)
    private ArrayList prepareSongs() {
        int count = 0;
        songs = new ArrayList();
        ArrayList temp = new ArrayList();
        if (trackCursor.moveToLast()) {
            do {
                try {
                    int song_id = trackCursor.getInt(trackCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    String song_title = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String song_artist = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String song_duration = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    int song_album_id = trackCursor.getInt(trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    String song_data = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String song_album_name = trackCursor.getString(trackCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    SongInfo songInfo = new SongInfo(song_id, song_title, song_artist, Integer.parseInt(song_duration), song_album_id, song_data, song_album_name);
                    songs.add(songInfo);
                    if (count++ < 6) {
                        temp.add(songInfo);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } while (trackCursor.moveToPrevious());
        }
        Toast.makeText(getActivity().getApplicationContext(), songs.size() + "", Toast.LENGTH_SHORT).show();
        return temp;
    }

    //    }
//        }
//
//        {
//        catch (Exception e)
//        }
//            mMediaPlayer.prepareAsync();
//            mMediaPlayer.setDataSource(song.getData());
//        try{
//        mMediaPlayer.reset();
//
//        tv_duration.setText(Utility.getTime(current_song_duration));
//        iv_play.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.selector_play));
//        tv_title.setText(song.getSong_name());
//        current_song_duration = song.getSong_duration();
//    {
    private ArrayList prepareArtists() {
        int count = 0;
        artists = new ArrayList();
        ArrayList temp = new ArrayList();
        if (artistCursor.moveToLast()) {
            do {
                try {
                    int _id = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                    String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                    String artistKey = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
                    int songsCount = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
                    int albumsCount = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                    ArtistInfo artistInfo = new ArtistInfo(_id,albumsCount,songsCount,artistName,artistKey);
                    artists.add(artistInfo);
                    if (count++ < 6) {
                        temp.add(artistInfo);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } while (artistCursor.moveToPrevious());
        }
        Toast.makeText(getActivity().getApplicationContext(), artists.size() + "", Toast.LENGTH_SHORT).show();
        return artists;
    }

    private ArrayList prepareAlbums() {
//        Log.v("HERE!","I'm Here");
//        int count = 0;
//        albums = new ArrayList();
//        ArrayList temp = new ArrayList();
//        if (albumCursor.moveToFirst()) {
//            do {
//                try {
//                    long albumId = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                    String albumName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
//                    String albumArt = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                    String albumKey = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
//                    String albumArtist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
//                    int albumSongCount = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
//                    AlbumInfo albumInfo = new AlbumInfo(albumArt, albumName, albumArtist, albumKey, albumSongCount);
//                    albums.add(albumInfo);
//                    if (count++ < 6) {
//                        temp.add(albumInfo);
//                    }
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            } while (albumCursor.moveToNext());
//        }
//        Toast.makeText(getActivity().getApplicationContext(), albums.size() + "", Toast.LENGTH_SHORT).show();
//
//
//
//        return temp;

        ArrayList albums = new ArrayList();
        if ((albumCursor != null) && (albumCursor.moveToFirst()))
            do {
                albums.add(new AlbumInfo(albumCursor.getLong(0), albumCursor.getString(1), albumCursor.getString(2), albumCursor.getLong(3), albumCursor.getInt(4), albumCursor.getInt(5)));
            }
            while (albumCursor.moveToNext());
        if (albumCursor != null)
            albumCursor.close();
        return albums;

    }

}

