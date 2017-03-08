package com.ericyl.example.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ericyl.example.R;
import com.ericyl.utils.util.ActivityUtils;
import com.ericyl.utils.util.DateUtils;
import com.ericyl.utils.util.PermissionUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ericyl.example.util.AppProperties.getContext;

public class TakePhotoActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_take_photo)
    Button btnTakePhoto;
    @BindView(R.id.btn_get_photo_from_gallery)
    Button btnGetPhotoFromGallery;
    @BindView(R.id.img_photo)
    SimpleDraweeView imgPhoto;

    private boolean savePhoto = false;
    private boolean addToGallery = false;
    private File takePhotoStorage;
    private boolean takePhotoPrivate = true;
    private boolean cropPhoto = false;

    private String currentPhotoPath;
    private String tmpPhotoPath;

    private static final int REQUEST_IMAGE_CAPTURE_WITHOUT_SAVE = 1;
    private static final int REQUEST_IMAGE_CAPTURE_WITH_SAVE_PRIVATE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int REQUEST_GET_PHOTO_FROM_GALLERY = 4;
    private static final int REQUEST_PERMISSION_CAMERA = 1;
    private static final int REQUEST_PERMISSION_CAMERA_SAVE_PRIVATE = 2;
    private static final int REQUEST_PERMISSION_CAMERA_WRITE_STORAGE = 3;
    private static final int REQUEST_PERMISSION_READ_STORAGE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savePhoto = sharedPreferences.getBoolean(getString(R.string.key_save_photo), false);
        addToGallery = sharedPreferences.getBoolean(getString(R.string.key_add_to_gallery), false);
        int radioValue = Integer.parseInt(sharedPreferences.getString(getString(R.string.key_save_storage), "1"));
        switch (radioValue) {
            default:
            case 1:
                takePhotoStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                takePhotoPrivate = true;
                break;
            case 2:
                takePhotoStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                takePhotoPrivate = false;
                break;
        }
        cropPhoto = sharedPreferences.getBoolean(getString(R.string.key_crop_photo), false);
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);
        initActionBar(toolbar);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
                } else {
                    if (!savePhoto) {
                        if (PermissionUtils.checkPermission(getContext(), Manifest.permission.CAMERA))
                            dispatchTakePictureIntentWithOutSave();
                        else
                            PermissionUtils.requestPermission(TakePhotoActivity.this, REQUEST_PERMISSION_CAMERA, Manifest.permission.CAMERA);
                    } else {
                        if (takePhotoPrivate) {
                            if (PermissionUtils.checkPermission(getContext(), Manifest.permission.CAMERA))
                                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_WITH_SAVE_PRIVATE);
                            else
                                PermissionUtils.requestPermission(TakePhotoActivity.this, REQUEST_PERMISSION_CAMERA_SAVE_PRIVATE, Manifest.permission.CAMERA);
                        } else {
                            if (PermissionUtils.checkPermissions(getContext(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                            else
                                PermissionUtils.requestPermissions(TakePhotoActivity.this, REQUEST_PERMISSION_CAMERA_WRITE_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                }
            }
        });
        btnGetPhotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtils.checkPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ActivityUtils.openGallery(TakePhotoActivity.this, REQUEST_GET_PHOTO_FROM_GALLERY);
                } else {
                    PermissionUtils.requestPermission(TakePhotoActivity.this, REQUEST_PERMISSION_READ_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        });

    }

//    private void openGallery() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_GET_PHOTO_FROM_GALLERY);
//    }

    private void dispatchTakePictureIntentWithOutSave() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_WITHOUT_SAVE);
        }
    }


    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                if (cropPhoto)
                    photoFile = createTmpImageFile();
                else
                    photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.ericyl.example.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = DateUtils.getString(new Date(), DateUtils.DATETIME_YMD_OTHER);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", takePhotoStorage);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createTmpImageFile() throws IOException {
        String timeStamp = DateUtils.getString(new Date(), DateUtils.DATETIME_YMD_OTHER);
        String imageFileName = "TMP_" + timeStamp + "_";
        File image = File.createTempFile(imageFileName, ".jpg", getExternalCacheDir());
        tmpPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_take_photo_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                ActivityUtils.jumpActivity(this, TakePhotoSettingActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean flag = true;
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                if (grantResults.length > 0) {
                    for (int i : grantResults)
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    if (flag)
                        dispatchTakePictureIntentWithOutSave();
                    else
                        Snackbar.make(getWindow().getDecorView(), R.string.error_per, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_CAMERA_SAVE_PRIVATE:
                if (grantResults.length > 0) {
                    for (int i : grantResults)
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    if (flag)
                        dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE_WITH_SAVE_PRIVATE);
                    else
                        Snackbar.make(getWindow().getDecorView(), R.string.error_per, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_CAMERA_WRITE_STORAGE:
                if (grantResults.length > 0) {
                    for (int i : grantResults)
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    if (flag)
                        dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                    else
                        Snackbar.make(getWindow().getDecorView(), R.string.error_per, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_READ_STORAGE:
                if (grantResults.length > 0) {
                    for (int i : grantResults)
                        flag = flag && (i == PackageManager.PERMISSION_GRANTED);
                    if (flag)
                        ActivityUtils.openGallery(this, REQUEST_GET_PHOTO_FROM_GALLERY);
                    else
                        Snackbar.make(getWindow().getDecorView(), R.string.error_per, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE_WITHOUT_SAVE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imgPhoto.setImageBitmap(imageBitmap);
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
            case REQUEST_IMAGE_CAPTURE_WITH_SAVE_PRIVATE:
                if (resultCode == RESULT_OK) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            "com.ericyl.example.fileprovider",
                            new File(cropPhoto ? tmpPhotoPath : currentPhotoPath));
                    if (cropPhoto) {
                        try {
                            File image = createImageFile();
                            UCrop.of(photoURI, Uri.fromFile(image))
                                    .withAspectRatio(16, 9)
                                    .start(this);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        imgPhoto.setImageURI(photoURI);
                        if (addToGallery)
                            galleryAddPic();
                    }


                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    imgPhoto.setImageURI(resultUri);
                    if (addToGallery)
                        galleryAddPic();
                }
                break;
            case REQUEST_GET_PHOTO_FROM_GALLERY:
                final Uri selectedUri = data.getData();
                if (selectedUri != null && cropPhoto) {
                    try {
                        File image = createImageFile();
                        UCrop.Options options = new UCrop.Options();
                        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        options.setCircleDimmedLayer(true);
                        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        UCrop uCrop = UCrop.of(selectedUri, Uri.fromFile(image)).withOptions(options).withAspectRatio(16, 9);
                        uCrop.start(this);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //    private void setPic() {
//        // Get the dimensions of the View
//        int targetW = mImageView.getWidth();
//        int targetH = mImageView.getHeight();
//
//        // Get the dimensions of the bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
//        mImageView.setImageBitmap(bitmap);
//    }
}
