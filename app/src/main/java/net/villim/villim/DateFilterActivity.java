package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateFilterActivity extends AppCompatActivity {

    public final static String START_DATE = "start_date";
    public final static String END_DATE = "end_date";

    private final static int STATE_SELECT_NONE = 0;
    private final static int STATE_SELECT_START = 1;
    private final static int STATE_SELECT_END = 2;

    private TimeZone timeZone;
    private Date startDate;
    private Date endDate;
    int selectState;

    private Toolbar toolbar;
    private TextView startDateTextView;
    private TextView endDateTextView;

    private Button saveSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_filter);

        timeZone = TimeZone.getDefault();
        startDate = (Date) getIntent().getSerializableExtra(START_DATE);
        endDate = (Date) getIntent().getSerializableExtra(END_DATE);
        boolean hasPresetDate = (startDate != null && endDate != null);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        changeState(STATE_SELECT_START);

        /* Bottom button */
        saveSelectionButton = (Button) findViewById(R.id.save_selection_button);
        saveSelectionButton.setEnabled(hasPresetDate);
        saveSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(START_DATE, startDate);
                returnIntent.putExtra(END_DATE, endDate);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

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
//                        startDate = date;
//                        if (endDate == null) { // First ever selection
//                            endDate = date;
//                        }
//                        changeState(STATE_SELECT_END);
                        break;
                    case STATE_SELECT_START:
                        startDate = date;
                        changeState(STATE_SELECT_END);
                        break;
                    case STATE_SELECT_END:
                        endDate = date;
                        changeState(STATE_SELECT_NONE);
                        break;
                    default:
//                        startDate = date;
//                        if (endDate == null) { // First ever selection
//                            endDate = date;
//                        }
//                        changeState(STATE_SELECT_END);
                        break;
                }

                calendar.clearSelectedDates();
                calendar.clearHighlightedDates();
                if (startDate != null) {
                    calendar.selectDate(startDate);
                }
                if (endDate != null) {
                    calendar.selectDate(endDate);
                }
                setStartAndEndDateText(startDate, endDate);

                /* Set button clickable if both start date and end dates are set.
                   Highlight dates from startDate to endDate */
                if (startDate != null && endDate != null) {
                    saveSelectionButton.setEnabled(true);
                    highlightDatesBetween(calendar, startDate, endDate);
                    System.out.println("Both nonnull.");

                }

            }

            @Override
            public void onDateUnselected(Date date) {
                //updateSelectedDates(calendar.getSelectedDates());
            }
        });

        if (hasPresetDate) {
            calendar.clearHighlightedDates();
            calendar.selectDate(startDate);
            calendar.selectDate(endDate);
            setStartAndEndDateText(startDate, endDate);
        }
    }

    private void updateSelectedDates(List<Date> dates) {
        if (dates.size() > 0) {
            startDate = dates.get(0);
            endDate = dates.get(dates.size() - 1);
        } else {
            startDate = null;
            endDate = null;
        }
        setStartAndEndDateText(startDate, endDate);
    }

    private void setStartAndEndDateText(Date startDate, Date endDate) {
        /* Set start date text */
        if (startDate != null) {
            String startDateText = String.format(getString(R.string.date_filter_date_text_format), startDate.getMonth(), startDate.getDate())
                    + "\n" + getWeekday(startDate.getDay());
            startDateTextView.setText(startDateText);
        } else {
            startDateTextView.setText(getString(R.string.date_filter_start_date));
        }

        /* Set end date text. */
        if (endDate != null) {
            String endDateText = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonth(), endDate.getDate())
                    + "\n" + getWeekday(endDate.getDay());
            ;
            endDateTextView.setText(endDateText);
        } else {
            endDateTextView.setText(getString(R.string.date_filter_end_date));
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

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }

    public void highlightDatesBetween(CalendarPickerView cal, Date startDate, Date endDate) {
        ArrayList<Date> dates = new ArrayList<Date>();
        Calendar dayAfterStartDate = Calendar.getInstance();
        dayAfterStartDate.setTime(startDate);
        dayAfterStartDate.add(Calendar.DATE, 1);

        Calendar dayBeforeEndDate = Calendar.getInstance();
        dayBeforeEndDate.setTime(endDate);
        dayBeforeEndDate.add(Calendar.DATE, -1);

        while (dayAfterStartDate.before(dayBeforeEndDate) || dayAfterStartDate.equals(dayBeforeEndDate)) {
            dates.add(dayAfterStartDate.getTime());
            dayAfterStartDate.add(Calendar.DATE, 1);
        }

        cal.highlightDates(dates);
    }
}
