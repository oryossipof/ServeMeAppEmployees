package com.example.employeesapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
public class LoginActivity extends AppCompatActivity {

    private  BackgroundWorker backgroundWorker;
    private  BroadcastReceiver receiver;
    private  String worker;

    private static final String TAG = "LoginActivity";
        private static final int REQUEST_SIGNUP = 0;

        @Bind(R.id.input_worker) EditText _workerText;
        @Bind(R.id.input_password) EditText _passwordText;
        @Bind(R.id.btn_login) Button _loginButton;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

            _loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });


        }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
            return;
        }










        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();



        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        worker = _workerText.getText().toString();
                        String password = _passwordText.getText().toString();

                        String type = "login";
                        backgroundWorker = new BackgroundWorker(LoginActivity.this);
                        backgroundWorker.execute(type,worker,password);
                        registerReceiver(receiver =new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String result = (String)intent.getExtras().getString("result");

                                //alertDialog.show();
                                if(result.equals("login success"))
                                    onLoginSuccess();
                                else
                                {
                                    onLoginFailed (result,context);
                                }
                            }

                        }, new IntentFilter("resultIntent"));

                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        Intent intent1 = new Intent(LoginActivity.this, RequestActivity.class).putExtra("workerNum",worker);
        startActivity(intent1);
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        unregisterReceiver(receiver);
        finish();
    }

    public void onLoginFailed(String result , Context context) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Result");
        alertDialog.setMessage(result);
        alertDialog.show();
        unregisterReceiver(receiver);

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String worker = _workerText.getText().toString();
        String password = _passwordText.getText().toString();

        if (worker.isEmpty()) {
            _workerText.setError("enter a valid worker id");
            valid = false;
        } else {
            _workerText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("Must be more than one character");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}