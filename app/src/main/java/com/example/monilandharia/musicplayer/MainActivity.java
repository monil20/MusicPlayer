package com.example.monilandharia.musicplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Path;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.sliding_layout);
//        nowPlayingFragment = (NowPlayingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_now_playing);
        cardView = findViewById(R.id.cardView);
        playPauseView = findViewById(R.id.play_pause);
        trackName = findViewById(R.id.album_track);

        playPauseView.post(new Runnable() {
            @Override
            public void run() {
                playPauseOriX = playPauseView.getX();
                playPauseOriY = playPauseView.getY();
                playPauseView.setX(playPauseOriX+playPauseOriX+10);
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
