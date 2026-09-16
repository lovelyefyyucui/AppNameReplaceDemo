package com.name.replace.domain;

/**
 * Created by Ye on 2017/9/7.
 */

public class ClientInfo {


    /**
     * deviceModel : iphone5s
     * deviceId : imei3333
     * os : 1
     * osVersion : 8.4
     * width : 640
     * height : 960
     */

    private String deviceModel;
    private String deviceId;
    private int os;
    private String osVersion;
    private int width;
    private int height;

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getOs() {
        return os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
