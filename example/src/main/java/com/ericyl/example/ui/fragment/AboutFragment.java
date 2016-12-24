package com.ericyl.example.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.ericyl.example.R;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.RichType;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class AboutFragment extends DialogFragment implements DialogInterface.OnKeyListener, View.OnClickListener {

    @BindView(R.id.tv_content)
    TextView tvContent;


    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

    }

    private void init() {
        Observable.just("about.md")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        String content = null;
                        BufferedReader br = null;
                        try {
                            StringBuilder sb = new StringBuilder();
                            String tempString;
                            br = new BufferedReader(new InputStreamReader(getResources().getAssets().open(s)));
                            while ((tempString = br.readLine()) != null) {
                                sb.append(tempString).append("\n");
                            }
                            content = sb.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (br != null)
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                        return content;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        RichText.from(s).type(RichType.MARKDOWN).autoFix(true).urlClick(new OnUrlClickListener() {
                            @Override
                            public boolean urlClicked(String url) {
                                new WebView(getActivity()).loadUrl(url);
                                return false;
                            }
                        }).into(tvContent);
                    }
                });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new AboutDialog(getActivity(), R.style.AppTheme);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setDimAmount(0.0F);
        }
        dialog.setOnKeyListener(this);
        return dialog;
    }

    @OnClick({R.id.btn_ok})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }


    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.ACTION_DOWN:
            case KeyEvent.KEYCODE_BACK:
                AboutFragment.this.dismiss();
                return true;
            default:
                return false;
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }


    class AboutDialog extends Dialog {
        public AboutDialog(Context context) {
            super(context);
        }

        public AboutDialog(Context context, int theme) {
            super(context, theme);
        }

        protected AboutDialog(Context context, boolean flag, OnCancelListener onCancelListener) {
            super(context, flag, onCancelListener);
        }

        @Override
        public void onBackPressed() {
            AboutFragment.this.dismiss();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
