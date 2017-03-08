package com.ericyl.example.ui.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ericyl.example.R;
import com.ericyl.example.ui.activity.HomeActivity;
import com.ericyl.utils.util.ActivityUtils;
import com.ericyl.utils.util.StringUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashFragment extends BaseFragment {

    @BindView(R.id.splash)
    SimpleDraweeView splash;
    @BindView(R.id.skip)
    TextView skip;
    @BindView(R.id.skip_layout)
    FrameLayout skipLayout;
    @BindView(R.id.frame)
    FrameLayout rootView;

    private View decorView;
    private Runnable runnable;

    private CountDownTimer timer;

    private static final String SPLASH = "splash/ic_splash_default.png";
    private static final String SPLASH_HAPPY_BIRTHDAY = "splash/ic_splash_happybirthday.png";
    private static final String IMAGE_URL = "http://ww3.sinaimg.cn/mw690/6504f962gw1f82hr0qrq7g20f605zjwx.gif";

    private boolean flagPause = false;
    private boolean flagResume = false;

    public static SplashFragment newInstance(int second) {
        SplashFragment splashFragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt("second", second);
        splashFragment.setArguments(args);
        return splashFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        Bundle args = getArguments();
        int second = args.getInt("second", 2);
        if (second < 3) {
            Uri uri = Uri.parse("asset:///" + SPLASH);
            splash.setImageURI(uri);
            decorView = getActivity().getWindow().getDecorView();
            decorView.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    if (flagPause)
                        flagResume = true;
                    else
                        jumpToHomeActivity();
                }
            }, second * 1000);

        } else {
            if (second >= 3 && second < 7) {
                Uri uri = Uri.parse("asset:///" + SPLASH_HAPPY_BIRTHDAY);
                splash.setImageURI(uri);

            } else {
//                ImageLoader imageLoader = ImageLoaderUtils.getInstance(getContext()).getImageLoader();
//                VolleyDraweeControllerBuilderSupplier mControllerBuilderSupplier
//                        = new VolleyDraweeControllerBuilderSupplier(getContext(), imageLoader);
//                SimpleDraweeView.initialize(mControllerBuilderSupplier);
//                DraweeController controller =  mControllerBuilderSupplier
//                        .get()
//                        .setUri(IMAGE_URL)
//                        .setAutoPlayAnimations(true)
//                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(Uri.parse(IMAGE_URL))
                        .setAutoPlayAnimations(true)
                        .build();
                GenericDraweeHierarchy genericDraweeHierarchy = splash.getHierarchy();
                genericDraweeHierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                splash.setController(controller);
            }
            skipLayout.setVisibility(View.VISIBLE);
            startCountDownTime(second * 1000);
        }


    }

    private void startCountDownTime(long time) {
        timer = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                skip.setText(StringUtils.getStringFormat(getContext(), R.string.second_skip, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (flagPause)
                    flagResume = true;
                else
                    jumpToHomeActivity();

            }
        };
        timer.start();
    }

    @OnClick(R.id.skip_layout)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.skip_layout:
                jumpToHomeActivity();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        flagPause = false;
        if (flagResume)
            jumpToHomeActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
        flagPause = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (decorView != null && runnable != null)
            decorView.removeCallbacks(runnable);
        if (timer != null)
            timer.cancel();
    }

    private void jumpToHomeActivity() {
        ActivityUtils.jumpActivity(getActivity(), HomeActivity.class);
        getActivity().finish();
    }

}
