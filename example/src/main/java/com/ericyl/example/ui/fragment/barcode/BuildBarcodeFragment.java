package com.ericyl.example.ui.fragment.barcode;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ericyl.example.R;
import com.ericyl.example.event.InitFabEvent;
import com.ericyl.example.model.ui.FabButton;
import com.ericyl.example.ui.fragment.BaseFragment;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.ColorUtils;
import com.ericyl.utils.util.PermissionUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.EncodeHintType;
import com.squareup.otto.Subscribe;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class BuildBarcodeFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.btn_build_barcode)
    Button btnBuildBarcode;
    @BindView(R.id.til_str)
    TextInputLayout tilStr;
    @BindView(R.id.et_str)
    EditText etStr;
    @BindView(R.id.img_barcode)
    ImageView imgBarcode;
    @BindView(R.id.layout_loading)
    View layoutLoading;
    @BindView(R.id.layout_content)
    FrameLayout layoutContent;

    private View rootView;
    private FloatingActionMenu fabMoreMenu;
    private boolean flag = false;
    private String path;
    private static final int REQUEST_PERMISSION_READ_STORAGE = 1;

    public void setFabMoreMenu(FloatingActionMenu fabMoreMenu) {
        this.fabMoreMenu = fabMoreMenu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_barcode, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        rootView = view;
        bindFloatingActionButton();
    }


    @Subscribe
    public void initFab(InitFabEvent event) {
        if (event.getIdRes() != R.id.tab_build_barcode) {
            this.fabMoreMenu = null;
        } else {
            this.fabMoreMenu = event.getFloatingActionMenu();
            bindFloatingActionButton();
        }
    }

    private void bindFloatingActionButton() {
        if (fabMoreMenu == null)
            return;
        if (!flag)
            fabMoreMenu.hideMenuButton(true);
        else
            fabMoreMenu.showMenuButton(true);
        FabButton save = new FabButton(R.id.fab_save, R.drawable.ic_file_download_white_24dp, R.string.save, this);
        FabButton share = new FabButton(R.id.fab_share, R.drawable.ic_share_white_24dp, R.string.share, this);
        List<FloatingActionButton> fabs = BarcodeFragment.createFloatingActionButton(getContext(), share, save);
        for (FloatingActionButton fab :
                fabs) {
            fabMoreMenu.addMenuButton(fab);
        }
    }


    @OnClick({R.id.btn_build_barcode})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_build_barcode:
                layoutLoading.setVisibility(View.VISIBLE);
                Observable.just(etStr.getText().toString()).map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
//
                        return QRCode.from(s).withHint(EncodeHintType.CHARACTER_SET, "UTF-8").withColor(ContextCompat.getColor(getContext(), R.color.blue), ColorUtils.COLOR_WHITE).withSize(layoutContent.getWidth(), layoutContent.getHeight()).to(ImageType.PNG).file().getAbsolutePath();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String path) {
                        BuildBarcodeFragment.this.path = path;
                        imgBarcode.setImageURI(Uri.fromFile(new File(path)));
                        layoutLoading.setVisibility(View.GONE);
                        if (fabMoreMenu != null)
                            fabMoreMenu.showMenuButton(true);
                        flag = true;
                    }
                });
                break;
            case R.id.fab_save:
                if (PermissionUtils.checkPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    saveToFile();
                else
                    PermissionUtils.requestPermissions(getActivity(), REQUEST_PERMISSION_READ_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                fabMoreMenu.close(true);
                break;
            case R.id.fab_share:
                fabMoreMenu.close(true);
                break;
        }
    }

    @OnTextChanged(value = R.id.et_str,
            callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterEmailInput(Editable editable) {
        btnBuildBarcode.setEnabled(!TextUtils.isEmpty(editable));
    }

    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_STORAGE:
                boolean flag = true;
                if (grantResults.length > 0) {
                    for (int i : grantResults) {
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    }
                    if (flag)
                        saveToFile();
                    else
                        Snackbar.make(rootView, R.string.error_per, Snackbar.LENGTH_SHORT).show();
                    break;
                }
        }
    }

    private void saveToFile() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String fileName = ("img_" + new Date().getTime() + ".jpg");
                String name = null;
                try {
                    name = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), path, fileName, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(name);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (s == null)
                    return;
                Cursor cursor = null;
                try {
                    cursor = getContext().getContentResolver().query(Uri.parse(s), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String index = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        Log.v("index", index);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }
}
