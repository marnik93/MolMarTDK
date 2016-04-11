package com.tdk.panobot_3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class Car_control_frag extends Fragment
{
    Button forw;
    Button backw;
    Button right;
    Button left;
    Button panohere;
    private View root;
    public String car_dir_r_l_b_f;
    public  String ip_cam;
    public  String arduino;
    Arduino_request ardu_request;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            arduino = bundle.getString("ardu_server");
            ip_cam=bundle.getString("ip_cam");
        }

        super.onActivityCreated(savedInstanceState);
        ardu_request=new Arduino_request();
        forw=(Button) root.findViewById(R.id.button_forw);
        backw=(Button) root.findViewById(R.id.button_backw);
        right=(Button) root.findViewById(R.id.butt_right);
        left=(Button) root.findViewById(R.id.butt_left);
        panohere=(Button) root.findViewById(R.id.button_pano_here);

        View_stream_frag stream=View_stream_frag.newInstance(arduino,ip_cam);
        FragmentManager fm = getActivity().getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(View_stream_frag.class.toString());
        ft.replace(R.id.stream_car_fragment_container, stream);
        ft.commit();

            //****Mozgatás irányának átadása az Arduinot hívó Async Tasknak******//////

        backw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),"onCLICK back",Toast.LENGTH_SHORT).show();
                car_dir_r_l_b_f = "b";
                ardu_request.do_request(arduino, car_dir_r_l_b_f);
//               Task_Ardu_request taskEsp = new Task_Ardu_request(arduino);
//               taskEsp.execute(car_dir_r_l_b_f);
            }
        });
        forw.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                car_dir_r_l_b_f = "f";
                ardu_request.do_request(arduino,car_dir_r_l_b_f);
            }
        });
        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                car_dir_r_l_b_f = "r";
                ardu_request.do_request(arduino,car_dir_r_l_b_f);
            }
        });
        left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                car_dir_r_l_b_f = "l";
                ardu_request.do_request(arduino,car_dir_r_l_b_f);
            }
        });

           /////*********KÉPillesztés háttérfolyamat indítása*******/////

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
    }

  ////////*********************************///// CAR CONTROLLING ////********************/////////


/////////////////////////////////////////////

    static public Car_control_frag newInstance(String ardu,String cam)
    {
        Bundle bundle = new Bundle();
        bundle.putString("ardu_server", ardu);
        bundle.putString("ip_cam", cam);
        Car_control_frag fragment = new Car_control_frag();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_car_control, container, false);
        return root;
    }

}


