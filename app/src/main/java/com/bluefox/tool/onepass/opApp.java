package com.bluefox.tool.onepass;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import java.io.IOException;

public class opApp extends Application {
    private HttpServer httpServer;
    private boolean httpIsStart;
    private String authority;

    public boolean getHttpIsStart() {
        return this.httpIsStart;
    }
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


    public boolean startHttpServer() {
        if (httpServer == null) {
            this.httpServer = new HttpServer(this);
        }
        if (this.httpIsStart) {
            return this.httpIsStart;
        }
        try {
            this.httpServer.start();
            this.httpIsStart = true;
        } catch (IOException e) {
            e.printStackTrace();
            this.httpIsStart = false;
        }
        return this.httpIsStart;
    }

    public void stopHttpServer() {
        if (httpServer == null) {
            return;
        }
        if (this.httpIsStart) {
            this.httpServer.stop();
            this.httpIsStart = false;
        }
    }
}
