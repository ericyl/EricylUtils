package com.ericyl.utils.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ericyl.utils.R;
import com.ericyl.utils.annotation.LoadStatus;
import com.ericyl.utils.util.DisplayUtils;

public class LoadingLayout extends LinearLayout {

    @ColorInt
    private int failedImageColor;
    private boolean showProgressBar;
    private boolean showFailedImage;
    private Drawable failedImage;
    private String loadingMessage;
    private String failedMessage;
    private String finishMessage;

    private ProgressBar pbLoading;
    private ImageView imgFailed;
    private TextView tvMsg;

    private OnClickListener onLoadingClickListener;
    private OnClickListener onFailedClickListener;
    private OnClickListener onFinishClickListener;

    public ProgressBar getPbLoading() {
        return pbLoading;
    }

    public ImageView getImgFailed() {
        return imgFailed;
    }

    public TextView getTvMsg() {
        return tvMsg;
    }

    public void setFinishMessage(String finishMessage) {
        this.finishMessage = finishMessage;
    }

    public void setFailedMessage(String failedMessage) {
        this.failedMessage = failedMessage;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;
    }

    public void setFailedImage(Drawable failedImage) {
        this.failedImage = failedImage;
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
        pbLoading.setVisibility(showProgressBar ? VISIBLE : GONE);
    }

    public void setShowFailedImage(boolean showFailedImage) {
        this.showFailedImage = showFailedImage;
        imgFailed.setVisibility(showFailedImage ? VISIBLE : GONE);
    }

    public void setOnLoadingClickListener(OnClickListener onLoadingClickListener) {
        this.onLoadingClickListener = onLoadingClickListener;
    }

    public void setOnFailedClickListener(OnClickListener onFailedClickListener) {
        this.onFailedClickListener = onFailedClickListener;
    }

    public void setOnFinishClickListener(OnClickListener onFinishClickListener) {
        this.onFinishClickListener = onFinishClickListener;
    }

    public LoadingLayout(Context context) {
        super(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.defaultStyle_LoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.defaultStyle_LoadingLayout);
        try {
            failedImageColor = a.getColor(R.styleable.LoadingLayout_failedImageColor, Color.TRANSPARENT);
            showProgressBar = a.getBoolean(R.styleable.LoadingLayout_showProgressBar, true);
            showFailedImage = a.getBoolean(R.styleable.LoadingLayout_showFailedImage, true);
            failedImage = a.getDrawable(R.styleable.LoadingLayout_failedImage);
            loadingMessage = a.getString(R.styleable.LoadingLayout_loadingMessage);
            failedMessage = a.getString(R.styleable.LoadingLayout_failedMessage);
            finishMessage = a.getString(R.styleable.LoadingLayout_finishMessage);
        } finally {
            a.recycle();
        }
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        int padding = (int) DisplayUtils.dp2px(context, 8.0f, 0f);
        setPadding(padding, padding, padding, padding);
        LayoutInflater.from(context).inflate(R.layout.layout_loading, this, true);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        imgFailed = (ImageView) findViewById(R.id.img_error);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        pbLoading.setVisibility(GONE);
        imgFailed.setVisibility(GONE);
        tvMsg.setVisibility(GONE);
        imgFailed.setImageDrawable(failedImage);
        imgFailed.setColorFilter(failedImageColor);
        setStatus(LoadStatus.LOADING);
    }

    public void setStatus(@LoadStatus int status) {
        switch (status) {
            case LoadStatus.LOADING:
                showLoading();
                break;
            case LoadStatus.FAILED:
                showFailed();
                break;
            case LoadStatus.FINISH:
                showFinish();
                break;
        }
    }

    private void showLoading() {
        if (showProgressBar)
            pbLoading.setVisibility(VISIBLE);
        imgFailed.setVisibility(GONE);
        tvMsg.setVisibility(VISIBLE);
        tvMsg.setText(loadingMessage);
        if (onLoadingClickListener != null)
            setOnClickListener(onLoadingClickListener);
        else
            setClickable(false);

    }

    private void showFailed() {
        if (showFailedImage)
            imgFailed.setVisibility(VISIBLE);
        pbLoading.setVisibility(GONE);
        tvMsg.setVisibility(VISIBLE);
        tvMsg.setText(failedMessage);
        if (onFailedClickListener != null)
            setOnClickListener(onFailedClickListener);
        else
            setClickable(false);

    }

    private void showFinish() {
        imgFailed.setVisibility(GONE);
        pbLoading.setVisibility(GONE);
        tvMsg.setVisibility(VISIBLE);
        tvMsg.setText(finishMessage);
        if (onFinishClickListener != null)
            setOnClickListener(onFinishClickListener);
        else
            setClickable(false);
    }


}
