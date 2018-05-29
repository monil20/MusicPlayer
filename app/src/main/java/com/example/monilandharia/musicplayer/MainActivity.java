package com.example.monilandharia.musicplayer;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ohoussein.playpause.PlayPauseView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

public class MainActivity extends AppCompatActivity {

    private SlidingUpPanelLayout mLayout;
    private CardView cardView;
    private PlayPauseView playPauseView;
    private TextView trackName;
    //    private NowPlayingFragment nowPlayingFragment;
    private float playPauseOriX, playPauseOriY, trackX, trackY, x, y;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.house_outline, android.R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Me", R.drawable.avatar, android.R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Cloud", R.drawable.cloud, android.R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Search", R.drawable.search, android.R.color.white);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setTranslucentNavigationEnabled(true);
        bottomNavigation.setAccentColor(R.color.colorPrimaryDark);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setCurrentItem(0);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fraggy = new HomeFragment();
        fragmentTransaction.add(R.id.fragment_home, fraggy);
        fragmentTransaction.commit();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0: {
                        if (!wasSelected) {
                            Fragment fragment = new HomeFragment();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home, fragment)
                                    .commit();
                        }
                        break;
                    }
                    case 1: {
                        if (!wasSelected) {
                            Fragment fragment = new MeFragment();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home, fragment)
                                    .commit();
                        }
                        break;
                    }
                    case 2: {
                        if (!wasSelected) {
                            Fragment fragment = new CloudFragment();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home, fragment)
                                    .commit();
                        }
                        break;
                    }
                    case 3: {
                        if (!wasSelected) {
                            Fragment fragment = new SearchFragment();
                            android.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_home, fragment)
                                    .commit();
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
                trackName.setX(30);
                trackName.setY(30);
            }
        });

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i("Slide", "onPanelSlide, offset " + slideOffset);
                if (slideOffset >= 0 && slideOffset <= 0.1) {
                    cardView.setAlpha(0);
                } else if (slideOffset > 0.1 && slideOffset <= 0.2) {
                    cardView.setAlpha(0.1f);
                } else if (slideOffset > 0.2 && slideOffset <= 0.3) {
                    cardView.setAlpha(0.2f);
                } else if (slideOffset > 0.3 && slideOffset <= 0.4) {
                    cardView.setAlpha(0.3f);
                } else if (slideOffset > 0.4 && slideOffset <= 0.5) {
                    cardView.setAlpha(0.4f);
                } else if (slideOffset > 0.5 && slideOffset <= 0.6) {
                    cardView.setAlpha(0.5f);
                } else if (slideOffset > 0.6 && slideOffset <= 0.7) {
                    cardView.setAlpha(0.6f);
                } else if (slideOffset > 0.7 && slideOffset <= 0.8) {
                    cardView.setAlpha(0.7f);
                } else if (slideOffset > 0.8 && slideOffset <= 0.9) {
                    cardView.setAlpha(0.8f);
                } else if (slideOffset > 0.9 && slideOffset <= 1.0) {
                    cardView.setAlpha(0.9f);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {
                Log.i("State", "onPanelStateChanged " + newState);
                if (newState.toString().equals("EXPANDED")) {
                    x = playPauseView.getX();
                    y = playPauseView.getY();
                    Path path = new Path();
                    path.moveTo(x, y);
                    path.lineTo(playPauseOriX, playPauseOriY);
                    ObjectAnimator objectAnimator =
                            ObjectAnimator.ofFloat(playPauseView, View.X,
                                    View.Y, path);
                    objectAnimator.setDuration(400);

                    Path path1 = new Path();
                    x = trackName.getX();
                    y = trackName.getY();
                    path1.moveTo(x, y);
                    path1.lineTo(trackX, trackY);
                    ObjectAnimator objectAnimator1 =
                            ObjectAnimator.ofFloat(trackName, View.X,
                                    View.Y, path1);
                    objectAnimator1.setDuration(220);

                    objectAnimator.start();
                    objectAnimator1.start();
                }
                if (newState.toString().equals("COLLAPSED")) {
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

                    x = trackName.getX();
                    trackX = x;
                    y = trackName.getY();
                    trackY = y;
                    Path path1 = new Path();
                    path1.moveTo(x, y);
                    path1.lineTo(30, 30);
                    ObjectAnimator objectAnimator1 =
                            ObjectAnimator.ofFloat(trackName, View.X,
                                    View.Y, path1);
                    objectAnimator1.setDuration(0);

                    objectAnimator.start();
                    objectAnimator1.start();
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

    private void initComponents() {
        mLayout = findViewById(R.id.sliding_layout);
//        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_now_playing);
        cardView = findViewById(R.id.cardView);
        playPauseView = findViewById(R.id.play_pause);
        trackName = findViewById(R.id.album_track);
        bottomNavigation = findViewById(R.id.bottom_navigation);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

}
