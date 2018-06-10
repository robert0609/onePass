package com.bluefox.tool.onepass;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class opApp extends Application {
    private String authority;

    public String getAuthority() {
        return this.authority;
    }
    public void setAuthority(String value) {
        this.authority = value;
    }

    public boolean validateAuthority(String auth) throws Exception {
        String md5AuthInDB = Store.getInstance(this).getUser();
        if (md5AuthInDB == null) {
            throw new Exception("There is no auth in db!");
        }
        String md5Auth = Md5.execute(auth);
        return md5Auth.equals(md5AuthInDB);
    }
}
