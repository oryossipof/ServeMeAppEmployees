package com.example.employeesapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestActivity extends AppCompatActivity implements Serializable {
    ArrayList<String> idArr = new ArrayList<>();
    String login_url = "http://servemeapp.000webhostapp.com//androidDataBaseQueries.php";
    private AASync AASync;
    private BackgroundWorker backgroundWorkerForUpdate;
    private BroadcastReceiver receiver;
    private String updateResult;
    private BroadcastReceiver updateReceiver;
    ArrayList<Request>  result = new ArrayList<>();
    ArrayList<Request> newUsers =new ArrayList<>();
    String type = "getRequest";
    String workerNum;
    RequestAdapter adapter;
    private ProgressDialog progress ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
         workerNum = getIntent().getStringExtra("workerNum");
        // Construct the data source
        ArrayList<Request> arrayOfUsers = new ArrayList<Request>();
        // Create the adapter to convert the array to views
        adapter = new RequestAdapter(this, arrayOfUsers);
        progress= new ProgressDialog(RequestActivity.this);
        // Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.lv1);


        // Add item to adapter
        listView.setAdapter(adapter);

        AASync = new AASync(RequestActivity.this);
        AASync.execute(type,workerNum);
        registerReceiver(receiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                result = (ArrayList<Request>)intent.getSerializableExtra("result");
                adapter.clear();
                Log.e("result",result.size()+"");
                newUsers = new ArrayList<Request>();
                for(int i=0;i <result.size();i++)
                {
                    newUsers.add(result.get(i));
                }
                unregisterReceiver(receiver);
                adapter.addAll(newUsers);
                listView.setAdapter(adapter);
            }


        }, new IntentFilter("resultIntent2"));


        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
               // newUsers.clear();
                registerReceiver(receiver,new IntentFilter("resultIntent2"));
                AASync = new AASync(RequestActivity.this);
                AASync.execute(type,workerNum);
                adapter.notifyDataSetChanged();

                handler.postDelayed( this, 3 * 1000 );
            }
        }, 3 * 1000 );


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                backgroundWorkerForUpdate = new BackgroundWorker(RequestActivity.this);
                backgroundWorkerForUpdate.execute("removeRequest",newUsers.get(position).id);
                newUsers.remove(position);
                adapter.notifyDataSetChanged();
                progress.setMessage("Updating...");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIndeterminate(false);
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
                progress.setProgress(0);
                progress.show();

                registerReceiver(updateReceiver =new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        updateResult = intent.getStringExtra("result");

                        progress.setProgress(100);
                        progress.dismiss();
                        Toast.makeText(RequestActivity.this, updateResult, Toast.LENGTH_SHORT).show();
                        unregisterReceiver(updateReceiver);

                    }


                }, new IntentFilter("removeIntent"));



                /*Intent intent = new Intent(MainActivity.this, SendMessage.class);
                String message = "abc";
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);*/
            }
        });
    }



}


