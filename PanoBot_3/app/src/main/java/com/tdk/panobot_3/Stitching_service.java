package com.tdk.panobot_3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.io.Writer;
import java.util.Date;

import org.json.*;


public class Stitching_service extends Service
{
    private static final String TAG = "Stitching service";
    WebView pic_to_save;

    public  String ip_cam="http://192.168.0.104:8080";
    public  String arduino="http://192.168.0.106:8080";
    Picture pictures_to_stitch[];
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    public final String sdCard = Environment.getExternalStorageDirectory().toString();


    private void OpenCV_Stitch_images()
    {
        File dir = new File(sdCard, "STITCHER"); //getting all the pictures i need from storage

          if (dir.exists())
          {
              File[] photos = dir.listFiles();
              WCFClient mywcfclient = new WCFClient(photos);
              String result_string = mywcfclient.doJSONPost();
              ////////////////////***********kép mentése********

              String imageName = "pano_x.jpg";
              File panoDir = new File(sdCard, "STITCHER_result"); // making a directory in sdcard

              if (!panoDir.exists())// if folder not exists, create new
              {
                  panoDir.mkdir();
                  Log.v("", " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CREATED NEW FOLDER : STITCHER_result");
              }
              File pano = new File(panoDir, imageName);// checks the file and if it already exist delete
              if (pano.exists()) {
                  pano.delete();
              }
              FileOutputStream fos=null;
              try
               {
                  if (result_string != null)
                  {
                      fos =new FileOutputStream(pano);//driving the stream into the freshly created file
                      // fos = openFileOutput(imageName, Context.MODE_PRIVATE);
                      byte[] decodedString = android.util.Base64.decode(result_string, android.util.Base64.DEFAULT);
                      fos.write(decodedString);
                      fos.flush();
                      fos.close();
                  }
              }
              catch (Exception e) {}
              finally
              {
                  if (fos != null)
                  {
                      fos = null;
                  }
              }
//
    }
    else
        {
            Log.i("....ERROR ....", ">>>>>>>>>>>>>>>>stitcher folder does not exists");
        }

    }


    public String Rotate_servo(String server) //webrequest to arduino: rotate the servo with 30 degrees
    {
        String degrees = "30";
        final String p = arduino + "?command=" + degrees;
        String serverResponse = "";
        try {
            URL url = new URL(p);
            InputStream is = null;
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setDoOutput(true);
            uc.connect();
            is = uc.getInputStream();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(is));
            serverResponse = bufferedReader.readLine();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = e.getMessage();
        }
        return serverResponse;
    }

    public void Shoot_and_Save_photo(int i)
    {
        String imageName = "stitch_" + i;
        try {

            File myDir = new File(sdCard, "STITCHER"); // making a directory in sdcard
            Log.i("sdcard  >>>>>>>>>>>>>>",sdCard);
            String fname = imageName + ".jpg";
            if (!myDir.exists())// if folder not exists, create new
            {
                myDir.mkdir();
                Log.v("", " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CREATED NEW FOLDER : STITCHER");
            }
            File file = new File(myDir, fname);// checks the file and if it already exist delete
            if (file.exists())
            {
                file.delete();
            }

            URL url = new URL(ip_cam + "/photo.jpg"); //Open url connection
            URLConnection ucon = url.openConnection();
            InputStream inputStream = null;
            HttpURLConnection httpConn = (HttpURLConnection) ucon;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                inputStream = httpConn.getInputStream();
            }

            FileOutputStream fos = new FileOutputStream(file); //driving the stream into the freshly created file
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0)
            {
                fos.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i(".....Progress:", "downloaded Size:" + downloadedSize);
            }
            fos.close();
            inputStream.close();
            Log.i("....SAVING IMAGE....", i + ". IMAGE SAVED TO SDcard...............................");
        } catch (IOException io)
        {
            io.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @Override
    public void onCreate() {
        //running the service on another thread then the main process
        HandlerThread thread = new HandlerThread("ServiceStartArguments");
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "STITCHING STARTED...", Toast.LENGTH_LONG).show();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
//        Bundle b=  intent.getExtras();
//        b.getString("ardu", this.arduino);
//        b.getString("cam",this.ip_cam);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, " DO_STITCH();\nTITCHER is  running...");
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            //
            DO_STITCH();
            stopSelf(msg.arg1);
        }
    }
    //******************making panoramic image from ip-cam photos************/////////////
    public void DO_STITCH()
    {
        try {
            for (int i = 0; i < 4; i++)//: shoot photo / save photo / rotate servo :// 4times
            {
                Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> STITCHER service is  running...");
                //Shoot_and_Save_photo(i + 1);
                //Rotate_servo(ardu_server);
               Thread.sleep(1000); //i am sleeping this ...for no reason just want to watch the log, cuz its too fast..i need to see the log
            }
            OpenCV_Stitch_images(); //calling OpenCv to do the hardwork on images
        } catch (InterruptedException e) {
            Log.i(TAG, e.toString());
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "STITCHING DONE !!!", Toast.LENGTH_SHORT).show();
    }
}