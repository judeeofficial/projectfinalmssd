package project.mssd2.mssd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by judeeofficial on 21/4/2559.
 */
public class indexActivity extends Activity {
Button work,contect,logout;
    TextView username;
    AlertDialog.Builder dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
work = (Button)findViewById(R.id.activitymap);
        contect = (Button)findViewById(R.id.contect);
        logout = (Button)findViewById(R.id.logout);
        username = (TextView)findViewById(R.id.username);

        Intent it = getIntent();
        String Username = it.getStringExtra("MemberID");
        username.setText(Username);
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),requestwork.class);
                startActivity(it);
            }
        });

        contect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),districtactivity.class);
                startActivity(it);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),loginActivity.class);
                startActivity(it);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprofile, menu);
        return true;
    }
    public void onBackPressed() {
        dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");

        dialog.setCancelable(true);
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }



}


