package project.mssd2.mssd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mssd.com.project.UserHelper;

/**
 * Created by pingpongofficial on 14/8/2559.
 */
public class districtactivity extends Activity {
    ListView district;
    UserHelper user;
    ArrayList<HashMap<String, String>> MyArrList;
   // AlertDialog.Builder viewDetail;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.districttel);
        district = (ListView)findViewById(R.id.lvlrequest);
    //    viewDetail = new AlertDialog.Builder(this);
        user = new UserHelper();
        getdistrict();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void getdistrict(){
        String url = "http://10.10.92.91/mobile/District.ashx";
        try {
            JSONArray data = new JSONArray(user.getHttpGet(url));
            MyArrList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map;
            for(int i = 0; i < data.length(); i++){
                JSONObject c = data.getJSONObject(i);

                map = new HashMap<String, String>();
                map.put("district_name", c.getString("district_name"));
                map.put("tel", c.getString("tel"));
                MyArrList.add(map);
            }
            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(districtactivity.this, MyArrList, R.layout.districtlist,
                    new String[] {"district_name", "tel"}, new int[] {R.id.ColName});
            district.setAdapter(sAdap);
district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dis_name = MyArrList.get(position).get("district_name").toString();
        String tel = MyArrList.get(position).get("tel").toString();
        Toast.makeText(districtactivity.this,dis_name,Toast.LENGTH_SHORT).show();
    Toast.makeText(districtactivity.this, tel ,Toast.LENGTH_SHORT).show();
/*

        viewDetail.setCancelable(true);
        viewDetail.setMessage("เขต : " + dis_name + "\n"
                + "เบอร์โทร : " + tel);
        viewDetail.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        viewDetail.show();
*/
    }
});
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
