package com.tdk.panobot_3;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.AppCompatButton;
import android.widget.Button;


public class Cam_control_frag extends Fragment {

    Button up;
    Button down;
    Button right;
    Button left;
    Button panohere;
    private View root;
    static public Cam_control_frag newInstance()
    {
        return new Cam_control_frag();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root= inflater.inflate( R.layout.fragment_cam_control, container, false);
        return root;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
