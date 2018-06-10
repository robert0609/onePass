package com.bluefox.tool.onepass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UnlockActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputToken;
    private Button buttonUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        this.inputToken = (EditText)findViewById(R.id.input_token);
        this.buttonUnlock = (Button)findViewById(R.id.button_unlock);

        this.buttonUnlock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        opApp app = (opApp)getApplicationContext();
        String auth = this.inputToken.getText().toString();

        try {
            if (app.validateAuthority(auth)) {
                app.setAuthority(auth);

                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                throw new Exception("Invalid authority!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
