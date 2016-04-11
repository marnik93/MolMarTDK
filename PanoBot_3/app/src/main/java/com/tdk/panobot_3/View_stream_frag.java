package com.tdk.panobot_3;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class View_stream_frag extends Fragment {


    public  String ip_cam;
    public  String arduino;
    private View root;
    WebView webview;
    WebChromeClient client;
    Button getpic;
    Button savepic;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            arduino = bundle.getString("ardu_server");
            ip_cam=bundle.getString("ip_cam");
            Log.i(">>>>>get bundle ardu ",arduino);
            Log.i(">>>>>get bundle ipcam ",ip_cam);

        }

        webview=(WebView)root.findViewById(R.id.webpage);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setDisplayZoomControls(true);
        webview.loadUrl(ip_cam + "/browserfs.html");

//        Uri streamURL = Uri.parse("http://192.168.0.106:8080/jsfs.html");
//       Intent streamIntent = new Intent(Intent.ACTION_VIEW);
//       streamIntent.setData(streamURL);
////    streamIntent.setDataAndType(streamURL,"video/*");
//       startActivity(streamIntent);

    }


     static public View_stream_frag newInstance(String ardu,String cam)
    {
        Bundle bundle = new Bundle();
        bundle.putString("ardu_server", ardu);
        bundle.putString("ip_cam", cam);
        View_stream_frag fragment = new View_stream_frag();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_view_stream, container, false);
        return root;
    }
}
