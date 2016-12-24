package com.ericyl.utils.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;


public class BarcodeDecoderUtils {

    private static final List<BarcodeFormat> ALL_FORMATS;
    private List<BarcodeFormat> formats;
    private Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);

    static {
        ALL_FORMATS = new ArrayList<BarcodeFormat>() {{
            add(BarcodeFormat.AZTEC);
            add(BarcodeFormat.MAXICODE);
            add(BarcodeFormat.RSS_EXPANDED);
            add(BarcodeFormat.UPC_EAN_EXTENSION);
            add(BarcodeFormat.UPC_A);
            add(BarcodeFormat.UPC_E);
            add(BarcodeFormat.EAN_13);
            add(BarcodeFormat.EAN_8);
            add(BarcodeFormat.RSS_14);
            add(BarcodeFormat.CODE_39);
            add(BarcodeFormat.CODE_93);
            add(BarcodeFormat.CODE_128);
            add(BarcodeFormat.ITF);
            add(BarcodeFormat.CODABAR);
            add(BarcodeFormat.QR_CODE);
            add(BarcodeFormat.DATA_MATRIX);
            add(BarcodeFormat.PDF_417);
        }};

    }


    public BarcodeDecoderUtils() {
    }

    public void setFormats(List<BarcodeFormat> formats) {
        this.formats = formats;
    }

    public Collection<BarcodeFormat> getFormats() {
        return formats == null ? ALL_FORMATS : formats;
    }

    public void addHints(Map<DecodeHintType, Object> hints) {
        this.hints.putAll(hints);
    }

    public void addHints(DecodeHintType hintType, Object hint) {
        this.hints.put(hintType, hint);
    }

    private MultiFormatReader getMultiFormatReader() {
        hints.put(DecodeHintType.POSSIBLE_FORMATS, getFormats());
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        multiFormatReader.setHints(hints);
        return multiFormatReader;
    }

    public Result getResult(@NonNull Bitmap bitmap) throws Exception {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        return getMultiFormatReader().decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));

    }

}