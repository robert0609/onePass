package com.bluefox.tool.onepass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (this.checkAuth()) {
            super.onCreate(savedInstanceState);//TODO:test
        }
    }

    private boolean checkAuth() {
        try {
            opApp app = (opApp) getApplicationContext();
            String auth = app.getAuthority();
            if (auth == null || auth.equals("")) {
                this.gotoUnlock();
                return false;
            } else if (!app.validateAuthority(auth)) {
                this.gotoUnlock();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.gotoUnlock();
            return false;
        }
    }

    private void gotoUnlock() {
        Intent intent = new Intent(this, UnlockActivity.class);
        startActivity(intent);
        finish();
    }
}
