package com.ericyl.utils.util;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.ericyl.utils.R;

public class DialogUtils {

    public static Dialog showDialog(@NonNull Context context, @StringRes int titleIdRes,
                                    @StringRes int msgIdRes, @StringRes int btnStrIdRes,
                                    @Nullable DialogInterface.OnClickListener btnListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(titleIdRes);
        builder.setMessage(msgIdRes);
        DialogInterface.OnClickListener listener = (btnListener == null) ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        } : btnListener;
        builder.setPositiveButton((btnStrIdRes == 0 ? R.string.ok : btnStrIdRes), listener);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static Dialog showDialog(@NonNull Context context, @NonNull String title,
                                    @NonNull String msg, @Nullable String btnStr,
                                    @Nullable DialogInterface.OnClickListener btnListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(msg);
        String str = (btnStr == null) ? context.getString(R.string.ok) : btnStr;
        DialogInterface.OnClickListener listener = (btnListener == null) ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        } : btnListener;
        builder.setPositiveButton(str, listener);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static Dialog showDialogWith2Btn(@NonNull Context context, @StringRes int titleIdRes,
                                            @StringRes int msgIdRes, @StringRes int positiveBtnStrIdRes,
                                            @NonNull DialogInterface.OnClickListener positiveBtnListener, @StringRes int negativeBtnStrIdRes,
                                            @Nullable DialogInterface.OnClickListener negativeBtnListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(titleIdRes);
        builder.setMessage(msgIdRes);
        builder.setPositiveButton((positiveBtnStrIdRes == 0 ? R.string.ok : positiveBtnStrIdRes), positiveBtnListener);

        DialogInterface.OnClickListener negativeListener = (negativeBtnListener == null) ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        } : negativeBtnListener;
        builder.setNegativeButton((negativeBtnStrIdRes == 0 ? R.string.cancel : negativeBtnStrIdRes), negativeListener);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    public static Dialog showDialogWith2Btn(@NonNull Context context, @NonNull String title,
                                            @NonNull String msg, @Nullable String positiveBtnStr,
                                            @NonNull DialogInterface.OnClickListener positiveBtnListener, @Nullable String negativeBtnStr,
                                            @Nullable DialogInterface.OnClickListener negativeBtnListener, boolean cancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(msg);
        String positiveStr = (positiveBtnStr == null) ? context.getString(R.string.ok) : positiveBtnStr;
        builder.setPositiveButton(positiveStr, positiveBtnListener);

        String negativeStr = (negativeBtnStr == null) ? context.getString(R.string.cancel) : negativeBtnStr;
        DialogInterface.OnClickListener negativeListener = (negativeBtnListener == null) ? new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        } : negativeBtnListener;
        builder.setNegativeButton(negativeStr, negativeListener);
        Dialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

}
