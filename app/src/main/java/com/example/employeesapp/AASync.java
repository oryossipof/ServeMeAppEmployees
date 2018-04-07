package com.example.employeesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class AASync extends AsyncTask<String, Void,  ArrayList<Request>> {


    private static String typeToCheck = "";
    Context context;
    AlertDialog alertDialog;
    String result = null;
    ArrayList<Request> newUsers = new ArrayList<Request>();

    AASync(Context ctx) {
        context = ctx;
    }
    @Override
    protected  ArrayList<Request>  doInBackground(String... params) {

        String type = params[0];

        // String login_url = "http://10.0.2.2/security/fcm_insert.php";
        // String login_url = "http://192.168.14.157/ServerMeApp/login.php";
        String login_url = "http://servemeapp.000webhostapp.com//androidDataBaseQueries.php";
        // String notification_url = "http://securitymanagementapp.000webhostapp.com//send_notiofication.php";

         if (type.equals("getRequest")) {
             Log.e("orkelev","2");
            typeToCheck = "getRequest";
            try {
                String workerNumber = params[1];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                /*********/
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                /************/

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_date = URLEncoder.encode("worker_number", "UTF-8") + "=" + URLEncoder.encode(workerNumber, "UTF-8")
                        + "&"+ URLEncoder.encode("todo", "UTF-8") + "=" + URLEncoder.encode("getRequest", "UTF-8");

                bufferedWriter.write(post_date);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line+"\n");

                }
                result =sb.toString();

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                for(int i = 0 ; i <ja.length();i++)
                {
                    jo= ja.getJSONObject(i);
                    newUsers.add(new Request(jo.getString("id"),jo.getString("roomNum"),jo.getString("service"),jo.getString("department"),jo.getString("time")));
                }
            }
            catch (Exception e)
            {

            }
                return newUsers;
        }


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Request> strings) {
        super.onPostExecute(strings);
        newUsers = strings;
        if(typeToCheck == "getRequest")
        {
            Intent intent1 = new Intent("resultIntent2");
            intent1.putExtra("result", newUsers);

            context.sendBroadcast(intent1);

        }
    }
}