package project.mssd2.mssd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by judeeofficial on 16/4/2559.
 */
public class output extends FragmentActivity {
    TextView loc,dis;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.output_activity);
loc = (TextView)findViewById(R.id.loc);
        dis = (TextView)findViewById(R.id.dis);


        Intent it = getIntent();

        String locwork = it.getStringExtra("loc");

        String diswork = it.getStringExtra("dis");



        loc.setText(locwork);
        dis.setText(diswork);
    }



}
