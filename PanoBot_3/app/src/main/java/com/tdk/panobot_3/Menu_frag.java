package com.tdk.panobot_3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Menu_frag extends Fragment
{
    public  String ip_cam;
    public  String arduino;
    AppCompatButton car_control;
    AppCompatButton cam_control;
    AppCompatButton make_pano;
    AppCompatButton call_wcf;
    private View root;

    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            arduino = bundle.getString("ardu_server");
            ip_cam=bundle.getString("ip_cam");
        }
        car_control=(AppCompatButton)root.findViewById(R.id.butt_control_car);
        cam_control=(AppCompatButton)root.findViewById(R.id.butt_control_cam);
        make_pano=(AppCompatButton)root.findViewById(R.id.butt_make_pano);
        call_wcf=(AppCompatButton)root.findViewById(R.id.wcf);

        call_wcf.setOnClickListener(new View.OnClickListener()
        { //TODO autó irányítása MÁRK
            @Override
            public void onClick(View v)
            {
//          WCFClient mywcfclient=new WCFClient();
//          String error= mywcfclient.doJSONPost();
//                Log.i("wcf returns ",error);

            }
        });

        car_control.setOnClickListener(new View.OnClickListener()
        { //TODO autó irányítása MÁRK
            @Override
            public void onClick(View v)
            {

                Car_control_frag car_cont=Car_control_frag.newInstance(arduino,ip_cam);
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(Car_control_frag.class.toString());
                ft.replace(R.id.fragment_container, car_cont);
                ft.commit();

            }
        });

        cam_control.setOnClickListener(new View.OnClickListener()
        { // TODO kamera irányítása külön
            @Override
            public void onClick(View v)
            {

                Cam_control_frag cam_cont = Cam_control_frag.newInstance(arduino,ip_cam);
                FragmentManager fm = getActivity().getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(Cam_control_frag.class.toString());
                ft.replace(R.id.fragment_container, cam_cont);
                ft.commit();
            }
        });

        make_pano.setOnClickListener(new View.OnClickListener()
        { //TODO panoráma stitch vagy masik helyre iranyitasa az autónak
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Stitching_service.class);
                intent.putExtra("ardu", arduino);
                intent.putExtra("cam", ip_cam);
                getActivity().startService(intent);
            }
        });

    }
   static public Menu_frag newInstance(String ardu,String cam)
    {
        Bundle bundle = new Bundle();
        bundle.putString("ardu_server", ardu);
        bundle.putString("ip_cam", cam);
        Menu_frag fragment = new Menu_frag();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        root = inflater.inflate(R.layout.fragment_menu, container, false);
        return  root;
    }
}
