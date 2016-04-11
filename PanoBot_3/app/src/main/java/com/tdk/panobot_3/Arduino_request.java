package com.tdk.panobot_3;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by krumpli on 2016.04.06..
 */
public class Arduino_request {

    public Arduino_request(){}
    public void do_request(String address,String param)
    {
        Task_Ardu_request taskEsp = new Task_Ardu_request(address);
        taskEsp.execute(param);
    }
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
            final String p = "http://www.postoffice.co.uk/track-trace";//arduino+"?command="+val;

            try
            {
                URL url = new URL(p);
                InputStream is=null;
                HttpURLConnection uc =(HttpURLConnection)url.openConnection();
                uc.setRequestMethod("GET");
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
            Log.i("in ardu request","postExec");
        }
    }
}
