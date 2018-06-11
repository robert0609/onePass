package com.bluefox.tool.onepass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText inputToken;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputToken = (EditText)findViewById(R.id.register_input_token);
        buttonSubmit = (Button)findViewById(R.id.register_button_submit);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String auth = inputToken.getText().toString();
        if (auth == null || auth == "") {
            Toast.makeText(this, "Please input token!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            long id = Store.getInstance(this).saveUser(Md5.execute(auth));
            Intent intent = new Intent(this, UnlockActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
