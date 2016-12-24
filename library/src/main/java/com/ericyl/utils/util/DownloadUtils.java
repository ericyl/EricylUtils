package com.ericyl.utils.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ericyl.utils.R;
import com.ericyl.utils.annotation.DownloadAllowedNetworkTypes;
import com.ericyl.utils.annotation.DownloadNotificationVisibility;
import com.ericyl.utils.model.DownloadCurrentInfo;
import com.ericyl.utils.model.DownloadCurrentStatus;
import com.ericyl.utils.model.DownloadInfo;

import java.text.DecimalFormat;


public class DownloadUtils {

    private static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat(
            "0.##");
    private static final double GB_2_BYTE = Math.pow(1024, 3);
    private static final double MB_2_BYTE = Math.pow(1024, 2);
    private static final double KB_2_BYTE = 1024;

    public static long download(@NonNull Context context, @NonNull String name, @NonNull String url, String title, String description, @Nullable String filepath, boolean isInDownloadUi, @DownloadNotificationVisibility int notificationVisibility, @DownloadAllowedNetworkTypes int allowedNetworkTypes, @Nullable String mimeType, boolean allowScanningByMediaScanner) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request downloadRequest = getDownloadRequest(context, name, url, title, description, filepath, isInDownloadUi, notificationVisibility, allowedNetworkTypes, mimeType, allowScanningByMediaScanner);
        return downloadManager.enqueue(downloadRequest);
    }

    public static long download(@NonNull Context context, @NonNull DownloadManager.Request downloadRequest) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager.enqueue(downloadRequest);
    }

    public static void stopDownload(@NonNull Context context, long... downloadIDs) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.remove(downloadIDs);
    }

    /**
     * @param context                     getContext()
     * @param name                        the name of the file
     * @param url                         download Url
     * @param title                       title
     * @param description                 description
     * @param filepath                    the local path to save the file
     * @param isInDownloadUi              in download ui
     * @param notificationVisibility      notification visibility
     * @param allowedNetworkTypes         network types
     * @param mimeType                    the type with the file like{applicaiton/mcword, applicaiton/pdf, application/vnd.android.package-archive ...}
     * @param allowScanningByMediaScanner allow MediaScanner Scanning the file
     * @return DownloadManager.Request
     */
    public static DownloadManager.Request getDownloadRequest(@NonNull Context context, @NonNull String name, @NonNull String url, String title, String description, @Nullable String filepath, boolean isInDownloadUi, @DownloadNotificationVisibility int notificationVisibility, @DownloadAllowedNetworkTypes int allowedNetworkTypes, @Nullable String mimeType, boolean allowScanningByMediaScanner) {
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(url));
        downloadRequest.setVisibleInDownloadsUi(isInDownloadUi);
        if (filepath != null)
            downloadRequest.setDestinationInExternalFilesDir(context, filepath, name);
        else
            downloadRequest.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, name);
        downloadRequest.setTitle(title);
        downloadRequest.setDescription(description);
        downloadRequest.setNotificationVisibility(notificationVisibility);
        downloadRequest.setAllowedNetworkTypes(allowedNetworkTypes);
        if (mimeType != null)
            downloadRequest.setMimeType(mimeType);
        if (allowScanningByMediaScanner)
            downloadRequest.allowScanningByMediaScanner();
        return downloadRequest;
    }

    public static DownloadInfo[] getDownloadInfo(@NonNull Context context, long... downloadIDs) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadInfo[] downloadInfos = new DownloadInfo[downloadIDs.length];
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadIDs);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                int index = -1;
                for (int i = 0; i < downloadIDs.length; i++) {
                    if (downloadIDs[i] == id)
                        index = i;
                }
                if (index == -1)
                    continue;
                String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION));
                String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI));
                String fileLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                String minType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                String mediaProviderUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI));
                DownloadInfo downloadInfo = new DownloadInfo(id, title, description, uri, fileLocalUri, minType, mediaProviderUri);
                downloadInfos[index] = downloadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return downloadInfos;
    }

    public static DownloadCurrentInfo[] getDownloadCurrentInfo(@NonNull Context context, long... downloadIDs) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadCurrentInfo[] downloadCurrentInfos = new DownloadCurrentInfo[downloadIDs.length];
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadIDs);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
                int index = -1;
                for (int i = 0; i < downloadIDs.length; i++) {
                    if (downloadIDs[i] == id)
                        index = i;
                }
                if (index == -1)
                    continue;
                long lastModifiedTimestamp = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP));
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                long fileBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                long downloadBytes = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                DownloadCurrentInfo downloadCurrentInfo = new DownloadCurrentInfo(id, lastModifiedTimestamp, status, reason, downloadBytes, fileBytes);
                downloadCurrentInfos[index] = downloadCurrentInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return downloadCurrentInfos;
    }

    public static DownloadCurrentStatus getDownloadCurrentStatus(@NonNull Context context, DownloadCurrentInfo downloadCurrentInfo) {
        int nowC, maxC = 100;
        String statusMsg;

        switch (downloadCurrentInfo.getStatus()) {
            case DownloadManager.STATUS_FAILED:
                switch (downloadCurrentInfo.getReason()) {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        //some possibly transient error occurred but we can't resume the download
                        statusMsg = context.getString(R.string.download_failed_error_cannot_resume);
                        break;
                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        //no external storage device was found. Typically, this is because the SD card is not mounted
                        statusMsg = context.getString(R.string.download_failed_error_device_not_found);
                        break;
                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        //the requested destination file already exists (the download manager will not overwrite an existing file)
                        statusMsg = context.getString(R.string.download_failed_error_file_already_exists);
                        break;
                    case DownloadManager.ERROR_FILE_ERROR:
                        //a storage issue arises which doesn't fit under any other error code
                        statusMsg = context.getString(R.string.download_failed_error_file_error);
                        break;
                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        //an error receiving or processing data occurred at the HTTP level
                        statusMsg = context.getString(R.string.download_failed_error_http_data_error);
                        break;
                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        //here was insufficient storage space. Typically, this is because the SD card is full
                        statusMsg = context.getString(R.string.download_failed_error_insufficient_space);
                        break;
                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        //there were too many redirects
                        statusMsg = context.getString(R.string.download_failed_error_too_many_redirects);
                        break;
                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        //an HTTP code was received that download manager can't handle
                        statusMsg = context.getString(R.string.download_failed_error_unhandled_http_code);
                        break;
                    case DownloadManager.ERROR_UNKNOWN:
                        //he download has completed with an error that doesn't fit under any other error code
                        statusMsg = context.getString(R.string.download_failed_error_unknown);
                        break;
                    default:
                        statusMsg = context.getString(R.string.download_failed);
                        break;
                }
                nowC = maxC = 0;
                break;
            case DownloadManager.STATUS_PAUSED:
                switch (downloadCurrentInfo.getReason()) {
                    case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                        //the download exceeds a size limit for downloads over the mobile network and the download manager is waiting for a Wi-Fi connection to proceed
                        statusMsg = context.getString(R.string.download_paused_paused_queued_for_wifi);
                        break;
                    case DownloadManager.PAUSED_UNKNOWN:
                        //the download is paused for some other reason
                        statusMsg = context.getString(R.string.download_paused_paused_unknown);
                        break;
                    case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                        //the download is waiting for network connectivity to proceed
                        statusMsg = context.getString(R.string.download_paused_paused_waiting_for_network);
                        break;
                    case DownloadManager.PAUSED_WAITING_TO_RETRY:
                        //the download is paused because some network error occurred and the download manager is waiting before retrying the request
                        statusMsg = context.getString(R.string.download_paused_paused_waiting_to_retry);
                        break;
                    default:
                        statusMsg = context.getString(R.string.download_paused);
                        break;
                }
                nowC = (int) (((double) downloadCurrentInfo.getDownloadBytes() / downloadCurrentInfo.getFileBytes()) * maxC);
                break;
            case DownloadManager.STATUS_PENDING:
                statusMsg = context.getString(R.string.download_pending);
                nowC = maxC = 0;
                break;
            case DownloadManager.STATUS_RUNNING:
                statusMsg = context.getString(R.string.download_running);
                nowC = (int) (((double) downloadCurrentInfo.getDownloadBytes() / downloadCurrentInfo.getFileBytes()) * maxC);
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                statusMsg = context.getString(R.string.download_successful);
                nowC = maxC;
                break;
            default:
                nowC = maxC = 0;
                statusMsg = null;
                break;
        }
        return new DownloadCurrentStatus(downloadCurrentInfo.getId(), downloadCurrentInfo.getStatus(), nowC, maxC, getSize(downloadCurrentInfo.getDownloadBytes()), getSize(downloadCurrentInfo.getFileBytes()), statusMsg);
    }

    public static CharSequence getSize(long size) {
        if (size <= 0)
            return "0M";
        if (size >= GB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / MB_2_BYTE))
                    .append("G");
        } else if (size >= MB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / MB_2_BYTE))
                    .append("M");
        } else if (size >= KB_2_BYTE) {
            return new StringBuilder(16).append(
                    DOUBLE_DECIMAL_FORMAT.format(size / KB_2_BYTE))
                    .append("K");
        } else {
            return new StringBuilder(16).append(size).append("B");
        }
    }

}
