package com.example.monilandharia.musicplayer;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monilandharia.musicplayer.adapters.AlbumsPreviewAdapter;
import com.example.monilandharia.musicplayer.adapters.ArtistsPreviewAdapter;
import com.example.monilandharia.musicplayer.adapters.TracksPreviewAdapter;
import com.example.monilandharia.musicplayer.dataLoaders.AlbumLoader;
import com.example.monilandharia.musicplayer.dataLoaders.ArtistLoader;
import com.example.monilandharia.musicplayer.dataLoaders.TrackLoader;
import com.example.monilandharia.musicplayer.models.AlbumInfo;
import com.example.monilandharia.musicplayer.models.ArtistInfo;
import com.example.monilandharia.musicplayer.models.SongInfo;
import com.example.monilandharia.musicplayer.utilities.Utility;


public class HomeFragment extends Fragment {

    public static RecyclerView recyclerRecentlyAdded, recyclerArtists, recyclerAlbums, recyclerGenres;
    public static TracksPreviewAdapter tracksPreviewAdapter;
    public static AlbumsPreviewAdapter albumsPreviewAdapter;
    public static ArtistsPreviewAdapter artistsPreviewAdapter;
    private RecyclerView.LayoutManager layoutManager, layoutManager1, layoutManager2;
//    String[] genreProjection = {
//            MediaStore.Audio.Genres._ID,
//            MediaStore.Audio.Genres.NAME
//    };


    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        if (getActivity() != null) {
            new loadAlbums().execute("");
            new loadArtists().execute("");
            new loadTracks().execute("");
        }
        return view;
    }

//    private void hello() {
//        int index;
//        long genreId;
//        Uri uri;
//        Cursor genrecursor;
//        Cursor tempcursor;
//        String[] proj1 = {MediaStore.Audio.Genres.NAME, MediaStore.Audio.Genres._ID};
//        String[] proj2 = {MediaStore.Audio.Media.DISPLAY_NAME};
//
//        genrecursor = getContext().getContentResolver().query(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, proj1, null, null, null);
//        if (genrecursor.moveToFirst()) {
//            do {
//                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME);
//                Log.i("GENRENAME", genrecursor.getString(index));
//
//                index = genrecursor.getColumnIndexOrThrow(MediaStore.Audio.Genres._ID);
//                genreId = Long.parseLong(genrecursor.getString(index));
//                uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreId);
//
//                tempcursor = getContext().getContentResolver().query(uri, proj2, null,null,null);
//                Log.i("NUMBERGENRE", tempcursor.getCount() + "");
//                if (tempcursor.moveToFirst()) {
//                    do {
//                        index = tempcursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//                        Log.i("Tag-Song name", tempcursor.getString(index));
//                    } while(tempcursor.moveToNext());
//                }
//            } while(genrecursor.moveToNext());
//        }
//    }

    private void initView(View view) {
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager1 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager2 = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerAlbums = view.findViewById(R.id.recyclerAlbums);
        recyclerAlbums.setHasFixedSize(true);
        recyclerAlbums.setLayoutManager(layoutManager1);
        recyclerAlbums.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recyclerArtists = view.findViewById(R.id.recyclerArtists);
        recyclerArtists.setHasFixedSize(true);
        recyclerArtists.setLayoutManager(layoutManager2);
        recyclerArtists.setOverScrollMode(View.OVER_SCROLL_NEVER);

        recyclerRecentlyAdded = view.findViewById(R.id.recyclerRecentlyAdded);
        recyclerRecentlyAdded.setHasFixedSize(true);
        recyclerRecentlyAdded.setLayoutManager(layoutManager);
        recyclerRecentlyAdded.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private class loadAlbums extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null) {
                albumsPreviewAdapter = new AlbumsPreviewAdapter(getActivity(), MainActivity.albumsPreview, new AlbumsPreviewAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(AlbumInfo albumInfo, int position) {
                        FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putLong("albumId", albumInfo.getId());
                        bundle.putString("albumName", albumInfo.getTitle());
                        bundle.putString("artistName", albumInfo.getArtistName());
                        AlbumDetailsFragment frag = new AlbumDetailsFragment();
                        frag.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_home, frag).addToBackStack("ALBUMS").commit();

                    }

                }, true);
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if (albumsPreviewAdapter != null) {
                recyclerAlbums.setAdapter(albumsPreviewAdapter);
            }
        }
    }

    private class loadArtists extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null)
                artistsPreviewAdapter = new ArtistsPreviewAdapter(getActivity(), MainActivity.artistsPreview, new ArtistsPreviewAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(ArtistInfo artistInfo, int position) {
                        FragmentTransaction fragmentTransaction = MainActivity.fragmentManager.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putInt("artistAlbumId", artistInfo.getSongId());
                        bundle.putLong("artistId", artistInfo.get_id());
                        bundle.putString("artistName", artistInfo.getArtistName());
                        ArtistDetailsFragment frag = new ArtistDetailsFragment();
                        frag.setArguments(bundle);
                        fragmentTransaction.replace(R.id.fragment_home, frag).addToBackStack("ALBUMS").commit();

                    }
                }, true);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if (artistsPreviewAdapter != null) {
                recyclerArtists.setAdapter(artistsPreviewAdapter);
            }
        }
    }

    private class loadTracks extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (getActivity() != null) {
                tracksPreviewAdapter = new TracksPreviewAdapter(getActivity(), MainActivity.songsPreview, new TracksPreviewAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(SongInfo song, int position) {
                        Utility.playSong(song, MainActivity.songs, position, getActivity());
                    }
                });
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            if (tracksPreviewAdapter != null) {
                recyclerRecentlyAdded.setAdapter(tracksPreviewAdapter);
            }
        }
    }
}

