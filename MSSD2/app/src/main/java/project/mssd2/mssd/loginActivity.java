package project.mssd2.mssd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mssd.com.project.UserHelper;


/**
 * Created by judeeofficial on 13/6/2559.
 */
public class loginActivity extends  Activity  {
    EditText txtUser,txtPass;
    Button btnLogin;
    AlertDialog.Builder ad;
    UserHelper user;
    AlertDialog.Builder dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
                txtUser = (EditText)findViewById(R.id.txtUsername);
                txtPass = (EditText)findViewById(R.id.txtPassword);

        // btnLogin
                btnLogin = (Button) findViewById(R.id.btnLogin);
        // Perform action on click
        user = new UserHelper();
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://10.10.92.91/mobile/login.ashx";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", txtUser.getText().toString()));
                params.add(new BasicNameValuePair("password", txtPass.getText().toString()));


                String resultServer  =  user.getHttpPost(url,params);


                String strStatusID = "0";
                String strMemberID = "0";
                String strError = "Unknow Status!";

                JSONObject c;
                try {
                    c = new JSONObject(resultServer);
                    strStatusID = c.getString("StatusID");
                    strMemberID = c.getString("customerid");
                    strError = c.getString("status");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



                // Prepare Login
                if(strStatusID.equals("0"))
                {
                    // Dialog
                    ad.setTitle("Error! ");
                    ad.setIcon(android.R.drawable.btn_star_big_on);
                    ad.setPositiveButton("Close", null);
                    ad.setMessage(strError);
                    ad.show();
                    txtUser.setText("");
                    txtPass.setText("");

                }
                else
                {
                    Toast.makeText(loginActivity.this, "Login OK", Toast.LENGTH_SHORT).show();
                    Intent newActivity = new Intent(loginActivity.this,indexActivity.class);
                    newActivity.putExtra("MemberID", strMemberID);
                    startActivity(newActivity);
                }


            }
        });
        final Button btnregister = (Button) findViewById(R.id.btnRegister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             register();
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void onBackPressed() {
        dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");

        dialog.setCancelable(true);
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            System.exit(0);
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

public void register(){
    Intent it = new Intent(getApplicationContext(),signup_activity.class);
    startActivity(it);
}





}