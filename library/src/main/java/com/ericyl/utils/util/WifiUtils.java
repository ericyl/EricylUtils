package com.ericyl.utils.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;

import java.util.List;

public class WifiUtils {

    public static final int WIFI_CIPHER_NOPASSWORD = 1;
    public static final int WIFI_CIPHER_WEP = 2;
    public static final int WIFI_CIPHER_WPA = 3;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public WifiUtils(Context context) {
        wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
    }

    public WifiManager getWifiManager() {
        return wifiManager;
    }

    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean openCloseWifi(boolean isOpen) {
        boolean flag = true;
        if (isOpen) {
            if (!wifiManager.isWifiEnabled())
                flag = wifiManager.setWifiEnabled(true);
        } else {
            if (wifiManager.isWifiEnabled())
                flag = wifiManager.setWifiEnabled(false);
        }
        return flag;
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public int getState() {
        return wifiManager.getWifiState();
    }

    public void acquireWifiLock(WifiManager.WifiLock wifiLock) {
        if (wifiLock == null)
            return;
        wifiLock.acquire();
    }

    public void releaseWifiLock(WifiManager.WifiLock wifiLock) {
        if (wifiLock == null)
            return;
        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    public WifiManager.WifiLock createWifiLock(String lockName) {
        if (lockName == null)
            return null;
        return wifiManager.createWifiLock(lockName);
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public List<WifiConfiguration> getConfiguration() {
        return wifiManager.getConfiguredNetworks();
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean connectConfiguration(WifiConfiguration wifiConfiguration) {
        return wifiConfiguration != null && wifiManager.enableNetwork(wifiConfiguration.networkId, true);
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean startScan() {
        return wifiManager.startScan();
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    public List<ScanResult> getScanWifiList() {
        return wifiManager.getScanResults();
    }

    @SuppressLint("HardwareIds")
    public String getMacAddress() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.getBSSID();
    }

    public String getSSID() {
        return (wifiInfo == null) ? "NULL" : wifiInfo.getSSID();
    }

    public int getIPAddress() {
        return (wifiInfo == null) ? 0 : wifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return (wifiInfo == null) ? 0 : wifiInfo.getNetworkId();
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean addNetwork(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null)
            return false;
        int wcgID = wifiManager.addNetwork(wifiConfiguration);
        return wifiManager.enableNetwork(wcgID, true);
    }

    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public boolean disconnectWifi(WifiConfiguration wifiConfiguration) {
        if (wifiConfiguration == null)
            return false;
        wifiManager.disableNetwork(wifiConfiguration.networkId);
        return wifiManager.disconnect();
    }

    @RequiresPermission(allOf = {Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.ACCESS_WIFI_STATE})
    public WifiConfiguration createWifiInfo(String ssid, String password,
                                            int type) {
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "/" + ssid + "/";

        WifiConfiguration tempConfig = isExists(ssid);
        if (tempConfig != null)
            wifiManager.removeNetwork(tempConfig.networkId);

        switch (type) {
            case WIFI_CIPHER_NOPASSWORD:
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case WIFI_CIPHER_WEP:
                config.hiddenSSID = true;
                config.wepKeys[0] = "/" + password + "/";
                config.allowedAuthAlgorithms
                        .set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers
                        .set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
                break;
            case WIFI_CIPHER_WPA:
                config.preSharedKey = "/" + password + "/";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms
                        .set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers
                        .set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers
                        .set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
                break;
            default:
                config = null;
                break;
        }

        return config;
    }

    @RequiresPermission(Manifest.permission.ACCESS_WIFI_STATE)
    private WifiConfiguration isExists(String ssid) {
        List<WifiConfiguration> existingConfigs = wifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("/" + ssid + "/")) {
                return existingConfig;
            }
        }
        return null;
    }
}