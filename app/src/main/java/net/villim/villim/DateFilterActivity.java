package net.villim.villim;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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

    private final static int STATE_SELECT_NONE = 0;
    private final static int STATE_SELECT_START = 1;
    private final static int STATE_SELECT_END = 2;

    private TimeZone timeZone;
    private Date startDate;
    private Date endDate;
    int selectState;

    private TextView startDateTextView;
    private TextView endDateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_filter);

        timeZone = TimeZone.getDefault();
        selectState = STATE_SELECT_NONE;

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
                switch (selectState) {
                    case STATE_SELECT_NONE:
                        startDate = date;
                        changeState(STATE_SELECT_NONE);
                        break;
                    case STATE_SELECT_END:
                        endDate = date;
                        changeState(STATE_SELECT_NONE);
                        break;
                    default:
                        startDate = date;
                        changeState(STATE_SELECT_END);
                        break;
                }
                calendar.selectDate(startDate);
                calendar.selectDate(endDate);
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
                changeState(STATE_SELECT_START);
            }
        });

        endDateTextView = (TextView) findViewById(R.id.end_date_text);
        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(STATE_SELECT_END);
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
                    + "\n" + getWeekday(startDate.getDay());
            startDateTextView.setText(startDateText);
            String endDateText = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonth(), endDate.getDate())
                    + "\n" + getWeekday(endDate.getDay());;
            endDateTextView.setText(endDateText);
        }
    }

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

    private void changeState(int state) {
        switch (state) {
            case STATE_SELECT_NONE:
                selectState = STATE_SELECT_NONE;
                startDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                endDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                break;
            case STATE_SELECT_START:
                selectState = STATE_SELECT_START;
                startDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_highlighted));
                endDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                break;
            case STATE_SELECT_END:
                selectState = STATE_SELECT_END;
                startDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                endDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_highlighted));
                break;
            default:
                selectState = STATE_SELECT_NONE;
                startDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                endDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                break;
        }
    }
}
