package com.tdk.panobot_3;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.AppCompatButton;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Car_control_frag extends Fragment {

    Button forw;
    Button backw;
    Button right;
    Button left;
    Button panohere;
    private View root;

    HttpURLConnection urlconn=null;
    String value;
    InputStream is;
    String url_string="https://www.google.hu/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=how+to+make+http+client+android+studio";



    static public Car_control_frag newInstance()
    {
      return new Car_control_frag();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_car_control, container, false);
        return root;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        forw=(Button) root.findViewById(R.id.button_forw);
        backw=(Button) root.findViewById(R.id.button_backw);
        right=(Button) root.findViewById(R.id.butt_right);
        left=(Button) root.findViewById(R.id.butt_left);
        panohere=(Button) root.findViewById(R.id.button_pano_here);

        forw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try
                {   URL url = new URL(url_string);
                    urlconn=(HttpURLConnection) url.openConnection();
                    urlconn.setDoOutput(true);
                    urlconn.connect();
                   is = urlconn.getInputStream();

                  //  urlconn.setChunkedStreamingMode(0);
                   // urlconn.addRequestProperty(" ", " ");

                  //  OutputStream out = new BufferedOutputStream(urlconn.getOutputStream());

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        backw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        panohere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016.03.13.  csináld meg a sticher servicet

            }
        });

        View_stream_frag stream=new View_stream_frag();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(View_stream_frag.class.toString());
        ft.replace(R.id.stream_car_fragment_container, stream);
        ft.commit();
    }

}
