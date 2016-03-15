package com.tdk.panobot_3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Menu_frag extends Fragment {

    AppCompatButton car_control;
    AppCompatButton cam_control;
    AppCompatButton make_pano;
    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_menu, container, false);
        return  root;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        car_control=(AppCompatButton)root.findViewById(R.id.butt_control_car);
        cam_control=(AppCompatButton)root.findViewById(R.id.butt_control_cam);
        make_pano=(AppCompatButton)root.findViewById(R.id.butt_make_pano);

        car_control.setOnClickListener(new View.OnClickListener() { //TODO autó irányítása MÁRK
            @Override
            public void onClick(View v) {
              //  Car_control_frag car_cont = Car_control_frag.newInstance();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.addToBackStack(Car_control_frag.class.toString());
//                ft.replace(R.id.fragment_container, car_cont);
//                ft.commit();

                Car_control_frag stream=new Car_control_frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(Car_control_frag.class.toString());
                ft.replace(R.id.fragment_container, stream);
                ft.commit();


            }
        });
        cam_control.setOnClickListener(new View.OnClickListener() { // TODO kamera irányítása külön FANNI
            @Override
            public void onClick(View v) {
                Cam_control_frag car_cont = Cam_control_frag.newInstance();

                View_stream_frag stream=new View_stream_frag();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(View_stream_frag.class.toString());
                ft.replace(R.id.fragment_container, stream);
                ft.commit();
            }
        });
        make_pano.setOnClickListener(new View.OnClickListener() { //TODO panoráma stitch vagy masik helyre iranyitasa az autónak FANNI
            @Override
            public void onClick(View v) {
            }
        });

    }
    public Menu_frag() {


    }
}
