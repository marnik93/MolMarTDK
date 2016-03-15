package com.tdk.panobot_3;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.VideoView;


/**
 * A simple {@link Fragment} subclass.
 */
public class View_stream_frag extends Fragment {

   private View root;
    WebView webview;
    VideoView stream;
    WebChromeClient client;

    public View_stream_frag() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_view_stream, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webview=(WebView)root.findViewById(R.id.webpage);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setDisplayZoomControls(true);
        webview.loadUrl("http://192.168.0.106:8080/browserfs.html");

//        Uri streamURL = Uri.parse("http://192.168.0.106:8080/jsfs.html");
//       Intent streamIntent = new Intent(Intent.ACTION_VIEW);
//       streamIntent.setData(streamURL);
////    streamIntent.setDataAndType(streamURL,"video/*");
//       startActivity(streamIntent);
    }
}
