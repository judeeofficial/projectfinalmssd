package project.mssd2.mssd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mssd.com.project.UserHelper;

/**
 * Created by judeeofficial on 13/4/2559.
 */
public class signup_activity extends Activity {

    EditText firstname, lastname, username, password, checkpassword,address,email,tel ;
    Button register;
    UserHelper user;
    AlertDialog.Builder ad;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);
        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        firstname = (EditText) findViewById(R.id.txtfirstname);
        lastname = (EditText) findViewById(R.id.txtlastname);
        username = (EditText) findViewById(R.id.txtUsername);
        password = (EditText) findViewById(R.id.txtPassword);
        checkpassword = (EditText) findViewById(R.id.txtConPassword);
        address = (EditText)findViewById(R.id.txtaddress);
        email = (EditText)findViewById(R.id.txtEmail);
        tel = (EditText)findViewById(R.id.txtTel);

        ad = new AlertDialog.Builder(this);
user = new UserHelper();
        register = (Button)findViewById(R.id.btnregister);
        register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(getregister()){
                    Intent newActivity = new Intent(signup_activity.this,loginActivity.class);
                    startActivity(newActivity);
                }
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  }
    public boolean getregister() {
        ad.setTitle("Error! ");
        ad.setIcon(android.R.drawable.btn_star_big_on);
        ad.setPositiveButton("Close", null);
        if (username.getText().length() == 0) {
            ad.setMessage("Please input [Username] ");
            ad.show();
            username.requestFocus();
            return false;
        }
        // Check Password
        if (password.getText().length() == 0 || checkpassword.getText().length() == 0) {
            ad.setMessage("Please input [Password/Confirm Password] ");
            ad.show();
            password.requestFocus();
            return false;
        }
        // Check Password and Confirm Password (Match)
        if (!password.getText().toString().equals(checkpassword.getText().toString())) {
            ad.setMessage("Password and Confirm Password Not Match! ");
            ad.show();
            checkpassword.requestFocus();
            return false;
        }
        // Check Name
        if (firstname.getText().length() == 0) {
            ad.setMessage("Please input firstname ");
            ad.show();
            firstname.requestFocus();
            return false;
        }
        // Check Email
        if (lastname.getText().length() == 0) {
            ad.setMessage("Please input lastname ");
            ad.show();
            lastname.requestFocus();
            return false;
        }
        if (address.getText().length() == 0) {
            ad.setMessage("Please input address ");
            ad.show();
            lastname.requestFocus();
            return false;
        }
        if (email.getText().length() == 0) {
            ad.setMessage("Please input email ");
            ad.show();
            lastname.requestFocus();
            return false;
        }
        if (tel.getText().length() == 0) {
            ad.setMessage("Please input tel ");
            ad.show();
            lastname.requestFocus();
            return false;
        }
        String urladddata = "http://192.168.1.102/mobile/register.ashx";
        String strStatusID = "0";
        String strError = "Unknow Status!";
        try {
            String Firstname = firstname.getText().toString();
            String Lastname = lastname.getText().toString();
            String Username = username.getText().toString();
            String Password = password.getText().toString();
            String Address = address.getText().toString();
            String Email = email.getText().toString();
            String Tel = tel.getText().toString();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Username", Username));
            params.add(new BasicNameValuePair("password", Password));
            params.add(new BasicNameValuePair("firstname", Firstname));
            params.add(new BasicNameValuePair("lastname", Lastname));
            params.add(new BasicNameValuePair("address", Address));
            params.add(new BasicNameValuePair("email", Email));
            params.add(new BasicNameValuePair("tel", Tel));

            String resultServer = user.getHttpPost(urladddata, params);
            JSONObject c;

            c = new JSONObject(resultServer);
            strStatusID = c.getString("statusID");
            strError = c.getString("status");
            if (strStatusID.equals("0")) {
                ad.setMessage(strError);
                ad.show();
            } else {
                Toast.makeText(signup_activity.this, "Save Data Successfully", Toast.LENGTH_SHORT).show();

            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return true;
    }
}
