package project.mssd2.mssd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mssd.com.project.UserHelper;

import mssd.com.project.myCalendarView;

/**
 * Created by pingpongofficial on 30/7/2559.
 */
public class requestwork extends Activity {
    Spinner work_type,work_time,district;
    EditText date_work,work_detail;
    Button datecalender,senddata;
    private int mYear;
    private int mMonth;
    private int mDay;
    Calendar c;
    static final int CALENDAR_VIEW_ID = 1;
    ArrayList<HashMap<String, String>> MyArrList,worktypearray;
    UserHelper user;
    AlertDialog.Builder dialog;
    String[] time = {"8:00","9:00","10:00","11:00","13:00","14:00","15:00","16:00"};
    ArrayAdapter<String> adt;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_request);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        work_type = (Spinner)findViewById(R.id.work_type);
        work_time = (Spinner)findViewById(R.id.work_time);
        district = (Spinner)findViewById(R.id.district);
        date_work = (EditText) findViewById(R.id.date_time);

        work_detail = (EditText)findViewById(R.id.work_detail);
        datecalender = (Button)findViewById(R.id.opencalender);
        senddata = (Button)findViewById(R.id.searchlocation);
        district = (Spinner)findViewById(R.id.district);
        c = Calendar.getInstance();
        user = new UserHelper();
        datecalender.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requestwork.this, myCalendarView.class);
                startActivityForResult(intent,CALENDAR_VIEW_ID);
            }
        });
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateCurrentDate();
        selectdistrict();
        selecttime();
        selectworktype();
        senddata.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            int districtpos = district.getSelectedItemPosition();
                int worktypepos = work_type.getSelectedItemPosition();
                String worktypename = worktypearray.get(worktypepos).get("type_name");
                String districtname = MyArrList.get(districtpos).get("district_name");
                 String time_work = work_time.getSelectedItem().toString();
            /*    Toast.makeText(requestwork.this,districtname,Toast.LENGTH_SHORT).show();
                Toast.makeText(requestwork.this,time_work,Toast.LENGTH_SHORT).show();
               Toast.makeText(requestwork.this,worktypename,Toast.LENGTH_SHORT).show(); */
                Intent it = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(it);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    private void selectworktype(){
        String url = "http://10.10.92.91/mobile/Work_type.ashx";
        try {
            JSONArray data = new JSONArray(user.getHttpGet(url));
            worktypearray = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);
                map = new HashMap<String, String>();
                map.put("type_name", c.getString("type_name"));
                worktypearray.add(map);
            }
            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(requestwork.this, worktypearray, R.layout.worktype,
                    new String[] {"type_name"}, new int[] { R.id.Colworktype});
            work_type.setAdapter(sAdap);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
private void selecttime(){
    adt  = new ArrayAdapter<String>(this, R.layout.time,R.id.Coltime,time);
    work_time.setAdapter(adt);
}
private void selectdistrict(){
    String url = "http://10.10.92.91/mobile/District.ashx";
    try {
        JSONArray data = new JSONArray(user.getHttpGet(url));

        MyArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        for(int i = 0; i < data.length(); i++){
            JSONObject c = data.getJSONObject(i);
            map = new HashMap<String, String>();
            map.put("district_name", c.getString("district_name"));
            MyArrList.add(map);
        }

        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(requestwork.this, MyArrList, R.layout.districtlist,
                new String[] {"district_name"}, new int[] { R.id.ColName});
        district.setAdapter(sAdap);


    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}
    private void updateCurrentDate() {
        date_work.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(mMonth + 1).append("-")
                        .append(mDay).append("-")
                        .append(mYear).append(" "));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case CALENDAR_VIEW_ID:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    date_work.setText(bundle.getString("dateSelected"));
                    break;
                }
        }

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
