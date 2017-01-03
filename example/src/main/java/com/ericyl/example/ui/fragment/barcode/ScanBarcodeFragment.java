package com.ericyl.example.ui.fragment.barcode;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ericyl.example.R;
import com.ericyl.example.event.InitFabEvent;
import com.ericyl.example.model.ui.FabButton;
import com.ericyl.example.ui.BaseFragment;
import com.ericyl.example.ui.activity.ScanBarcodeActivity;
import com.ericyl.example.util.BusProvider;
import com.ericyl.utils.util.BarcodeDecoderUtils;
import com.ericyl.utils.util.FileUtils;
import com.ericyl.utils.util.PermissionUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.zxing.Result;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnLongClick;

import static android.app.Activity.RESULT_OK;

public class ScanBarcodeFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.img_barcode)
    ImageView imgBarcode;

    private FloatingActionMenu fabMoreMenu;

    public void setFabMoreMenu(FloatingActionMenu fabMoreMenu) {
        this.fabMoreMenu = fabMoreMenu;
    }

    private static final int REQUEST_SCAN = 1;
    private static final int REQUEST_DISTINGUISH = 2;

    private static final int REQUEST_PERMISSION_READ_STORAGE = 2;



    @Override
    public int getContentViewId() {
        return R.layout.fragment_scan_barcode;
    }

    @Override
    protected void init(View view, @Nullable Bundle savedInstanceState) {
        super.init(view, savedInstanceState);
        bindFloatingActionButton();
    }

    @Subscribe
    public void initFab(InitFabEvent event) {
        if (event.getIdRes() != R.id.tab_scan_barcode) {
            this.fabMoreMenu = null;
        } else {
            this.fabMoreMenu = event.getFloatingActionMenu();
            bindFloatingActionButton();
        }

    }

    private void bindFloatingActionButton() {
        if (fabMoreMenu == null)
            return;
        fabMoreMenu.showMenuButton(true);
        FabButton save = new FabButton(R.id.fab_scan, R.drawable.ic_photo_camera_white_24dp, R.string.scanning, this);
        FabButton share = new FabButton(R.id.fab_distinguish, R.drawable.ic_photo_library_white_24dp, R.string.distinguish, this);
        List<FloatingActionButton> fabs = BarcodeFragment.createFloatingActionButton(getContext(), share, save);
        for (FloatingActionButton fab : fabs) {
            fabMoreMenu.addMenuButton(fab);
        }
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

    Uri selectedUri;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SCAN:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    etContent.setText(scanResult);
                }
                break;
            case REQUEST_DISTINGUISH:
                if (resultCode == RESULT_OK) {
                    selectedUri = data.getData();
                    setPic(FileUtils.getFileAbsolutePath(getActivity(), selectedUri));
                }
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_scan:
                Intent intent = new Intent();
                intent.setClass(getActivity(), ScanBarcodeActivity.class);
                getActivity().startActivityForResult(intent, REQUEST_SCAN);
                fabMoreMenu.close(true);
                break;
            case R.id.fab_distinguish:
                if (PermissionUtils.checkPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    openGallery();
                } else {
                    PermissionUtils.requestPermission(getActivity(), REQUEST_PERMISSION_READ_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                fabMoreMenu.close(true);
                break;

        }
    }

    @OnLongClick({R.id.img_barcode})
    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.img_barcode:
                Bitmap bitmap = ((BitmapDrawable) imgBarcode.getDrawable()).getBitmap();
                BarcodeDecoderUtils utils = new BarcodeDecoderUtils();
                try {
                    Result scanResult = utils.getResult(bitmap);
                    etContent.setText(scanResult.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_DISTINGUISH);
    }

    private void setPic(String fileName) {
        // Get the dimensions of the View
        int targetW = imgBarcode.getWidth();
        int targetH = imgBarcode.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fileName, bmOptions);
        imgBarcode.setImageBitmap(bitmap);
    }


}
