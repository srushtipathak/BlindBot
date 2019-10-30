package com.androidtutorialshub.loginregister.models;

public class Apps {
    String appName;
    String packageName;

    public Apps(String appName, String packageName) {
        this.appName = appName;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
