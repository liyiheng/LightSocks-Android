package com.liyiheng.lightsocks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mPasswd;
    private EditText mRemote;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        mPasswd = ((EditText) findViewById(R.id.editText_password));
        mRemote = ((EditText) findViewById(R.id.editText_remote));
        mText = ((TextView) findViewById(R.id.text));
    }

    @Override
    public void onClick(View v) {
        String args = "-password " + mPasswd.getText().toString() + " -remote " + mRemote.getText().toString();
        Command.run(this, R.raw.lightsocks_local_linux_arm, args, new Command.CommandListener() {
            @Override
            public void lineOut(final String line) {
                if (TextUtils.isEmpty(line)) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Output", line);
                        mText.append(line);
                        if (line.contains("成功")) {
                            System.setProperty("http.proxyHost", "127.0.0.1");
                            System.setProperty("http.proxyPort", "7448");
                            System.setProperty("https.proxyHost", "127.0.0.1");
                            System.setProperty("https.proxyPort", "7448");
                        }
                    }
                });
            }

            @Override
            public void done(final int exit) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Done", exit + "");
                        //Toast.makeText(MainActivity.this, String.valueOf(exit), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


//    System.clearProperty("http.proxyHost");
//System.clearProperty("http.proxyPort");
//System.clearProperty("https.proxyHost");
//System.clearProperty("https.proxyPort");
}
