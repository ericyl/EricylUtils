package com.ericyl.example.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.utils.util.PermissionUtils;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.ericyl.example.util.AppProperties.getContext;

public class ScanBarcodeActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.scan_view)
    ZXingScannerView scannerView;

    private static final int REQUEST_PERMISSION_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        unbinder = ButterKnife.bind(this);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else {
            if (!PermissionUtils.checkPermission(getContext(), Manifest.permission.CAMERA))
                PermissionUtils.requestPermission(this, REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA);

        }
    }

    @Override
    void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                boolean flag = true;
                if (grantResults.length > 0) {
                    for (int i : grantResults) {
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    }
                    if (!flag) {
                        Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    break;
                }
        }
    }


    @Override
    public void handleResult(Result rawResult) {
        if (rawResult.getText().equals("")) {
            Toast.makeText(this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("result", rawResult.getText());
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}