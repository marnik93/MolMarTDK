package com.tdk.panobot_3;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main_menu_activity extends Activity {

    public String ip_cam = "http://192.168.0.104:8080";
    public String arduino = "http://192.168.0.106:8080";
    Button go;
    EditText arduip;
    EditText camip;
    public Bundle sIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.sIS = savedInstanceState;
        setContentView(R.layout.activity_main_menu);
        arduip = (EditText) findViewById(R.id.ardu_ip);
        camip = (EditText) findViewById(R.id.cam_ip);

        if (!(arduip.getText().toString().isEmpty()) && !(camip.getText().toString().isEmpty())) {
            arduino = "http://" + arduip.getText().toString();
            ip_cam = "http://" + camip.getText().toString();
        }

            go = (Button) findViewById(R.id.start);
            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(">>>>>GO clicked", arduino);
                    Log.i(">>>>>GO clicked", ip_cam);
                    RelativeLayout login = (RelativeLayout) findViewById(R.id.start_cont);
                    login.setVisibility(View.INVISIBLE);
                    login.setEnabled(false);
                    if (sIS == null) {
                        Menu_frag mf = Menu_frag.newInstance(arduino, ip_cam);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction t = fm.beginTransaction();
                        t.replace(R.id.fragment_container, mf); // kicseréljük a konténer tartalmát
                        t.commit();
                    }
                }
            });
        }

    }

