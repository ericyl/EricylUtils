// IDownloadManagerService.aidl
package com.ericyl.example;

// Declare any non-default types here with import statements

interface IDownloadManagerService {

    void invokeDownload(String name, String title, String url, String info);

    void invokeStopDownload();
}
