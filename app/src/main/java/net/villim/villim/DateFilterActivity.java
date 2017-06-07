package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateFilterActivity extends AppCompatActivity {

    private final static int START_DATE = 0;
    private final static int END_DATE = 1;

    private TimeZone timeZone;
    private Date startDate;
    private Date endDate;
    int selectMode;

    private TextView startDateTextView;
    private TextView endDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_filter);

        timeZone = TimeZone.getDefault();
        selectMode = START_DATE;

        /* Set up calendar. */
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                switch (selectMode) {
                    case START_DATE:
                        ;
                    case END_DATE:
                        ;
                    default:

                }
                updateSelectedDates(calendar.getSelectedDates());
            }

            @Override
            public void onDateUnselected(Date date) {
                updateSelectedDates(calendar.getSelectedDates());
            }
        });

        /* Set up start date / end date select texts. */
        startDateTextView = (TextView) findViewById(R.id.start_date_text);
        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMode = START_DATE;
            }
        });

        endDateTextView = (TextView) findViewById(R.id.end_date_text);
        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMode = END_DATE;
            }
        });
    }

    private void updateSelectedDates(List<Date> dates) {
        if (dates.size() > 1) {
            startDate = dates.get(0);
            endDate = dates.get(dates.size() - 1);
        } else if (dates.size() == 1) {
            startDate = dates.get(0);
            endDate = dates.get(0);
        } else {
            startDate = null;
            endDate = null;
        }
        setStartAndEndDateText(startDate, endDate);
    }

    private void setStartAndEndDateText(Date startDate, Date endDate) {
        if (startDate == null && endDate == null) {
            startDateTextView.setText(getString(R.string.date_filter_start_date));
            endDateTextView.setText(getString(R.string.date_filter_end_date));
        } else {
            String startDateText = String.format(getString(R.string.date_filter_date_text_format), startDate.getMonth(), startDate.getDate())
                    + getWeekday(startDate.getDay());
            startDateTextView.setText(startDateText);
            String endDateText = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonth(), endDate.getDate())
                    + getWeekday(endDate.getDay());;
            endDateTextView.setText(endDateText);
        }
    }

    ;


    private String getWeekday(int weekday) {
        /* Java date은 0부터 6, Calendar 클래스 constant는 1부터 7. */
        switch (weekday + 1) {
            case Calendar.SUNDAY:
                return getString(R.string.sunday);
            case Calendar.MONDAY:
                return getString(R.string.monday);
            case Calendar.TUESDAY:
                return getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return getString(R.string.thursday);
            case Calendar.FRIDAY:
                return getString(R.string.friday);
            case Calendar.SATURDAY:
                return getString(R.string.saturday);
            default:
                return getString(R.string.sunday);
        }
    }
}
