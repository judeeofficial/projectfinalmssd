package mssd.com.project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import project.mssd2.mssd.R;

/**
 * Created by pingpongofficial on 30/7/2559.
 */
public class myCalendarView extends Activity {
    CalendarView calendar;
    private int mYear;
    private int mMonth;
    private int mDay;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = new CalendarView(this);
        setContentView(calendar);
calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){


    @Override
    public void onSelectedDayChange(CalendarView view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        String selectedDate = new StringBuilder().append(mMonth + 1).append("-").append(mDay).append("-")
                .append(mYear).append(" ").toString();

        Bundle b = new Bundle();
        b.putString("dateSelected", selectedDate);

        Intent intent = new Intent();
        intent.putExtras(b);
        setResult(RESULT_OK,intent);
        finish();
    }
});


    }


}
