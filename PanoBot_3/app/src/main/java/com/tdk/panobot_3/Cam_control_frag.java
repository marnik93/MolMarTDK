package com.tdk.panobot_3;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Cam_control_frag extends Fragment {

    Button up;
    Button down;
    Button right;
    Button left;
    Button panohere;
    Button navigate;
    private View root;
    public  String ip_cam;
    public  String arduino;
    public String servo_dir;
Arduino_request ardu_req;

    static public Cam_control_frag newInstance(String ardu,String cam)
    {
        Bundle bundle = new Bundle();
        bundle.putString("ardu_server", ardu);
        bundle.putString("ip_cam", cam);
        Cam_control_frag fragment = new Cam_control_frag();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        root= inflater.inflate( R.layout.fragment_cam_control, container, false);
        return root;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            arduino = bundle.getString("ardu_server");
            ip_cam=bundle.getString("ip_cam");
        }

        super.onActivityCreated(savedInstanceState);
        right=(Button) root.findViewById(R.id.rotate_right);
        left=(Button) root.findViewById(R.id.rotate_left);
        panohere=(Button) root.findViewById(R.id._pano_here);
        navigate=(Button) root.findViewById(R.id.nav_elsewhere);

        View_stream_frag stream=View_stream_frag.newInstance(arduino,ip_cam);
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(View_stream_frag.class.toString());
        ft.replace(R.id.stream_cam_fragment_container, stream);
        ft.commit();

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                servo_dir = "r";
                ardu_req.do_request(arduino, servo_dir);

            }
        });
        left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                servo_dir = "l";
                ardu_req.do_request(arduino, servo_dir);
            }
        });
        panohere.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
                {// TODO: 2016.03.13. STITCHER SERVICE
                    Intent intent = new Intent(getActivity(), Stitching_service.class);
                    intent.putExtra("ardu", arduino);
                    intent.putExtra("cam", ip_cam);
                    getActivity().startService(intent);
                }
        });

        /////*********KÉPillesztés háttérfolyamat indítása*******/////

        navigate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Car_control_frag car_cont=Car_control_frag.newInstance(arduino, ip_cam);
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(Car_control_frag.class.toString());
                ft.replace(R.id.fragment_container, car_cont);
                ft.commit();
            }
        });


    }


    /////////*********************************///// CAM CONTROLLING ////********************/////////

    private class Task_Ardu_request extends AsyncTask<String, Void, String> // ez egy párhuzamos feladatot indít
    {
        String arduino;
        String serverResponse = "";
        Task_Ardu_request(String server) {
            this.arduino = server;
        }
        @Override
        protected String doInBackground(String... params) // ebben a függvényben mondjuk meg, hogy ez a Task mit csináljona a háttérben
        {
            String val = params[0];
            final String p = arduino+"?command="+val;

            try
            {
                URL url = new URL(p);
                InputStream is=null;
                HttpURLConnection uc =(HttpURLConnection)url.openConnection();
                uc.setDoOutput(true);
                uc.connect();
                is = uc.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                serverResponse = bufferedReader.readLine();
                is.close();
            }  catch (IOException e)
            {
                e.printStackTrace();
                serverResponse = e.getMessage();
                Log.i("background", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> error:  " + serverResponse);
            }
            return serverResponse;
        }
        @Override
        protected void onPostExecute(String s)
        {
            Toast.makeText(getActivity(), "BAckground", Toast.LENGTH_SHORT).show();
        }
    }

}
