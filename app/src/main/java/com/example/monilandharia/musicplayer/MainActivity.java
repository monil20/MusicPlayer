package com.example.monilandharia.musicplayer;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Path;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ohoussein.playpause.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mLayout;
    private CardView cardView;
    private PlayPauseView playPauseView;
    private TextView trackName, album_artist, startTime, endTime;
    private NowPlayingFragment nowPlayingFragment;
    private float playPauseOriX, playPauseOriY, trackX, trackY, albumArtistX, albumArtistY, cardViewX, cardViewY, x, y, tempY, tempY1;
    private AHBottomNavigation bottomNavigation;
    private ImageView next, prev, repeat, shuffle;
    private boolean flag = true, flag1 = false;
    private SeekBar seekBar;
    private SlidingUpPanelLayout slidingLayout;
    private RelativeLayout relativeLayout;
    private Fragment fraggy, fraggy2, fraggy3, fraggy4;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStorageAndMicroPhonePermissions();
    }

    private void requestStorageAndMicroPhonePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            initComponents();
                            initFragments();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }).onSameThread().check();
    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void initComponents() {
        setContentView(R.layout.activity_main);
        mLayout = findViewById(R.id.sliding_layout);
        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_now_playing);
        cardView = findViewById(R.id.songCardView);
        playPauseView = findViewById(R.id.play_pause);
        trackName = findViewById(R.id.album_track);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        album_artist = findViewById(R.id.album_artist_name);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        repeat = findViewById(R.id.repeat);
        shuffle = findViewById(R.id.shuffle);
        seekBar = findViewById(R.id.seekBar);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
//        slidingLayout = findViewById(R.id.sliding_layout);
        relativeLayout = findViewById(R.id.relativelayout);
    }

    private void initFragments() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.house_outline, android.R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Me", R.drawable.avatar, android.R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Cloud", R.drawable.cloud, android.R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Search", R.drawable.search_white, android.R.color.white);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setAccentColor(R.color.colorPrimaryDark);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setCurrentItem(0);

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();

        fraggy = new HomeFragment();
        fraggy2 = new MeFragment();
        fraggy3 = new CloudFragment();
        fraggy4 = new SearchFragment();
        fragmentTransaction.add(R.id.fragment_home, fraggy);
        fragmentTransaction.add(R.id.fragment_home, fraggy2);
        fragmentTransaction.add(R.id.fragment_home, fraggy3);
        fragmentTransaction.add(R.id.fragment_home, fraggy4);
        fragmentTransaction.hide(fraggy2);
        fragmentTransaction.hide(fraggy3);
        fragmentTransaction.hide(fraggy4);
        fragmentTransaction.commit();


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0: {
                        if (!wasSelected) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fraggy);
                            fragmentTransaction.hide(fraggy2);
                            fragmentTransaction.hide(fraggy3);
                            fragmentTransaction.hide(fraggy4);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 1: {
                        if (!wasSelected) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fraggy2);
                            fragmentTransaction.hide(fraggy);
                            fragmentTransaction.hide(fraggy3);
                            fragmentTransaction.hide(fraggy4);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 2: {
                        if (!wasSelected) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fraggy3);
                            fragmentTransaction.hide(fraggy);
                            fragmentTransaction.hide(fraggy2);
                            fragmentTransaction.hide(fraggy4);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                    case 3: {
                        if (!wasSelected) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.show(fraggy4);
                            fragmentTransaction.hide(fraggy);
                            fragmentTransaction.hide(fraggy2);
                            fragmentTransaction.hide(fraggy3);
                            fragmentTransaction.commit();
                        }
                        break;
                    }
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        playPauseView.post(new Runnable() {
            @Override
            public void run() {
                playPauseOriX = playPauseView.getX();
                playPauseOriY = playPauseView.getY();
                playPauseView.setX(playPauseOriX + playPauseOriX + 10);
                playPauseView.setY(-30);
                trackX = trackName.getX();
                trackY = trackName.getY();
                trackName.setX(250);
                trackName.setY(30);
                albumArtistX = album_artist.getX();
                albumArtistY = album_artist.getY();
                album_artist.setX(250);
                tempY = y;
                album_artist.setY(y + trackName.getHeight() + 26);
                cardViewX = cardView.getX();
                cardViewY = cardView.getY();
                cardView.setScaleX(0.28f);
                cardView.setScaleY(0.23f);
                cardView.setX(trackName.getY());
                cardView.setY(trackName.getY());
                tempY1 = trackName.getY();
                cardView.setPivotX(0);
                cardView.setPivotY(0);

                flag1 = true;
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Slide", "onPanelSlide, offset " + slideOffset);
//                if (slideOffset >= 0 && slideOffset <= 0.1) {
//                    cardView.setAlpha(0);
//                } else if (slideOffset > 0.1 && slideOffset <= 0.2) {
//                    cardView.setAlpha(0.1f);
//                } else if (slideOffset > 0.2 && slideOffset <= 0.3) {
//                    cardView.setAlpha(0.2f);
//                } else if (slideOffset > 0.3 && slideOffset <= 0.4) {
//                    cardView.setAlpha(0.3f);
//                } else if (slideOffset > 0.4 && slideOffset <= 0.5) {
//                    cardView.setAlpha(0.4f);
//                } else if (slideOffset > 0.5 && slideOffset <= 0.6) {
//                    cardView.setAlpha(0.5f);
//                } else if (slideOffset > 0.6 && slideOffset <= 0.7) {
//                    cardView.setAlpha(0.6f);
//                } else if (slideOffset > 0.7 && slideOffset <= 0.8) {
//                    cardView.setAlpha(0.7f);
//                } else if (slideOffset > 0.8 && slideOffset <= 0.9) {
//                    cardView.setAlpha(0.8f);
//                } else if (slideOffset > 0.9 && slideOffset <= 1.0) {
//                    cardView.setAlpha(0.9f);
//                }
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i("State", "onPanelStateChanged " + newState);
                if (newState.toString().equals("DRAGGING")) {
                    if (previousState.toString().equals("EXPANDED")) {
                        flag = false;
                    }
                }
                if (newState.toString().equals("EXPANDED")) {
//                    bottomNavigation.setVisibility(View.INVISIBLE);
//                    SlidingUpPanelLayout.LayoutParams layoutParams = (SlidingUpPanelLayout.LayoutParams) slidingLayout.getLayoutParams();
//                    layoutParams.add

                    x = playPauseView.getX();
                    y = playPauseView.getY();
                    Path path = new Path();
                    path.moveTo(x, y);
                    path.lineTo(playPauseOriX, playPauseOriY);
                    ObjectAnimator objectAnimator =
                            ObjectAnimator.ofFloat(playPauseView, View.X,
                                    View.Y, path);
                    objectAnimator.setDuration(300);

                    Path path1 = new Path();
                    x = trackName.getX();
                    y = trackName.getY();
                    path1.moveTo(x, y);
                    path1.lineTo(trackX, trackY);
                    ObjectAnimator objectAnimator1 =
                            ObjectAnimator.ofFloat(trackName, View.X,
                                    View.Y, path1);
                    objectAnimator1.setDuration(220);

                    Path path2 = new Path();
                    x = album_artist.getX();
                    y = album_artist.getY();
                    path2.moveTo(x, y);
                    path2.lineTo(albumArtistX, albumArtistY);
                    ObjectAnimator objectAnimator2 =
                            ObjectAnimator.ofFloat(album_artist, View.X,
                                    View.Y, path2);
                    objectAnimator2.setDuration(250);

                    Path path3 = new Path();
                    x = cardView.getX();
                    y = cardView.getY();
                    path3.moveTo(x, y);
                    path3.lineTo(cardViewX, cardViewY);
                    ObjectAnimator objectAnimator3 =
                            ObjectAnimator.ofFloat(cardView, View.X,
                                    View.Y, path3);
                    objectAnimator2.setDuration(200);

                    Animation animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);

                    if (flag) {
                        objectAnimator.start();
                        objectAnimator1.start();
                        objectAnimator2.start();
                        objectAnimator3.start();
                        cardView.setScaleX(1);
                        cardView.setScaleY(1);
                        prev.startAnimation(animationFadeIn);
                        next.startAnimation(animationFadeIn);
                        shuffle.startAnimation(animationFadeIn);
                        repeat.startAnimation(animationFadeIn);
                        cardView.startAnimation(animationFadeIn);
                        seekBar.setAnimation(animationFadeIn);
                        startTime.setAnimation(animationFadeIn);
                        endTime.setAnimation(animationFadeIn);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 900);
                        repeat.setAlpha(1.0f);
                        shuffle.setAlpha(1.0f);
                        next.setAlpha(1.0f);
                        prev.setAlpha(1.0f);
                        cardView.setAlpha(1.0f);
                        seekBar.setAlpha(1.0f);
                        startTime.setAlpha(1.0f);
                        endTime.setAlpha(1.0f);

                    }

                    flag = true;
                    flag1 = false;

                }
                if (newState.toString().equals("COLLAPSED")) {
                    if (!flag1) {
                        x = playPauseView.getX();
                        playPauseOriX = x;
                        y = playPauseView.getY();
                        playPauseOriY = y;
                        Path path = new Path();
                        path.moveTo(x, y);
                        path.lineTo(x + x + 10, -30);
                        ObjectAnimator objectAnimator =
                                ObjectAnimator.ofFloat(playPauseView, View.X,
                                        View.Y, path);
                        objectAnimator.setDuration(0);

                        Log.i("height", playPauseView.getHeight() + "");

                        x = trackName.getX();
                        trackX = x;
                        y = trackName.getY();
                        trackY = y;
                        Path path1 = new Path();
                        path1.moveTo(x, y);
                        path1.lineTo(250, 30);
                        ObjectAnimator objectAnimator1 =
                                ObjectAnimator.ofFloat(trackName, View.X,
                                        View.Y, path1);
                        objectAnimator1.setDuration(0);

                        x = album_artist.getX();
                        albumArtistX = x;
                        y = album_artist.getY();
                        albumArtistY = y;
                        Path path2 = new Path();
                        path2.moveTo(x, y);
                        path2.lineTo(250, tempY + trackName.getHeight() + 26);
                        ObjectAnimator objectAnimator2 =
                                ObjectAnimator.ofFloat(album_artist, View.X,
                                        View.Y, path2);
                        objectAnimator2.setDuration(0);

                        x = cardView.getX();
                        cardViewX = x;
                        y = cardView.getY();
                        cardViewY = y;
                        cardView.setScaleX(0.28f);
                        cardView.setScaleY(0.23f);
                        Path path3 = new Path();
                        path3.moveTo(x, y);
                        path3.lineTo(tempY1, tempY1);
                        ObjectAnimator objectAnimator3 =
                                ObjectAnimator.ofFloat(cardView, View.X,
                                        View.Y, path3);
                        objectAnimator3.setDuration(0);


                        objectAnimator.start();
                        objectAnimator1.start();
                        objectAnimator2.start();
                        objectAnimator3.start();
                        next.setAlpha(0.0f);
                        prev.setAlpha(0.0f);
                        repeat.setAlpha(0.0f);
                        shuffle.setAlpha(0.0f);
//                        cardView.setAlpha(1.0f);
                        seekBar.setAlpha(0.0f);
                        startTime.setAlpha(0.0f);
                        endTime.setAlpha(0.0f);

                        flag = true;
                        flag1 = true;
                    }
                }
            }
        });

        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
            mLayout.setPanelState(PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }



}
