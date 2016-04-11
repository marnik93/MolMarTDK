package com.tdk.panobot_3;

/**
 * Created by John on 2016.04.02..
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
 import java.io.InputStreamReader;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONStringer;
import org.xmlpull.v1.XmlSerializer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.ResponseCache;


public class WCFClient
{
    private final static String SERVICE_URI = "http://stitcherservice.azurewebsites.net/service1.svc";
    private static final String METHOD_NAME = "/CreatePano";
    public   String errorMessage;
    File [] pics_tosend;
    String[] str_tosend;
    public WCFClient(File photos[])
    {
        pics_tosend=photos;
        str_tosend=new String[4];
        Convert_pic_to_string();
    }

   public void Convert_pic_to_string()
   {
       for (int i = 0; i < 4; i++)
       {
           File kep=pics_tosend[i];
           if (kep.exists())
           {
               byte[] byteArray=null;
               try
               {
                   InputStream inputStream = new FileInputStream(kep);
                   ByteArrayOutputStream bos = new ByteArrayOutputStream();
                   byte[] b = new byte[1024*8 ];
                   int bytesRead = 0;
                   while ((bytesRead = inputStream.read(b)) >0)
                   {
                       bos.write(b, 0, bytesRead);
                   }
                  byteArray = new byte[bos.size()];;
                   byteArray = bos.toByteArray();
               } catch (IOException e)
               {
                   e.printStackTrace();
               }
               str_tosend[i] = Base64.encodeToString(byteArray, Base64.DEFAULT);// képből lett string
           }
       }
   }
 public String doJSONPost()
 {
  // Do a JSON Post called from inside of service()
  //  Build JSON message to post.
  Integer intResult = 0;
  HttpURLConnection urlConnection;
  StringBuilder result = new StringBuilder();

  String returnString = "";
  String jsonString = "";

  JSONObject jsonObject = new JSONObject();
  try
  {
   JSONStringer jsonStringer = new JSONStringer()
           .object()
           .key("im_1").value(str_tosend[0])
           .key("im_2").value(str_tosend[1])
           .key("im_3").value(str_tosend[2])
           .key("im_4").value(str_tosend[3])
           .endObject()
           ;
   jsonString = jsonStringer.toString();
  }
  catch (JSONException jex)
  {
   jsonString = "";
  }

  try
  {
   // Build URL web service
   StringBuilder sb = new StringBuilder();
   sb.append(SERVICE_URI);
   sb.append(METHOD_NAME);

   String httpConnect = sb.toString();
   Log.d("TRAFFIC", "Post to: " + httpConnect);

   // Connect to web service
   URL url = new URL(httpConnect);
   urlConnection = (HttpURLConnection) url.openConnection();
   urlConnection.setDoOutput(true);
   urlConnection.setRequestMethod("POST");

   urlConnection.setRequestProperty("Content-Type", "application/json");
   urlConnection.setRequestProperty("Accept", "application/json");
   urlConnection.setRequestProperty("Content-Length", "" + Integer.toString(jsonString.getBytes().length));
   urlConnection.setUseCaches(false);
   urlConnection.setConnectTimeout(10000*60);
   urlConnection.setReadTimeout(10000*60);

   urlConnection.connect();

   // Post Json
   OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
   outputStreamWriter.write(jsonString);
   outputStreamWriter.close();

   // Receive Response from server
   int statusCode = urlConnection.getResponseCode();
   String stat=urlConnection.getResponseMessage();
   Log.d("TRAFFIC", "StatusCode: " + stat);

            /* 200 represents HTTP OK */
   if (statusCode == 200)
   {
     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
     String line;
     sb.setLength(0);    // clear stringbuilder
     while ( (line=bufferedReader.readLine()) != null)
     {
       sb.append(line + "\n");
     }
        returnString = sb.toString();
       JSONObject jObj = new JSONObject(returnString);
       returnString = jObj.getString("CreatePanoResult");
   }
   else
   {
    intResult = 0; //"Failed to fetch data!";
    returnString = "";
   }

  } catch (Exception ex)
  {
    errorMessage = ex.toString();
    Log.d("TRAFFIC", "JSON Post ERROR: " + errorMessage);
   returnString = "";
  }
  return returnString; //ebben vagyon a kép csak stringben :D

 }   // end doJSONPost()
}
