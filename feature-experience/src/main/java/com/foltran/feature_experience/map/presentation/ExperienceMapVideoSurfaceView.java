package com.foltran.feature_experience.map.presentation;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.IOException;

public class ExperienceMapVideoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "INTRO_SF_VIDEO_CALLBACK";
    public MediaPlayer mp;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExperienceMapVideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public ExperienceMapVideoSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public ExperienceMapVideoSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public ExperienceMapVideoSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init (){
        mp = new MediaPlayer();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mp.setDataSource("TODO");
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        int screenHeight = getHeight();
        int screenWidth = getWidth();
        android.view.ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = screenWidth;
        lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);

        setLayoutParams(lp);
        mp.setDisplay(getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mp.stop();
    }

}