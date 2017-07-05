package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class CalendarActivity extends VillimActivity {

    public final static String START_DATE = "start_date";
    public final static String END_DATE = "end_date";
    public final static String INVALID_DATES = "invalid_dates";

    private final static int STATE_SELECT_NONE = 0;
    private final static int STATE_SELECT_START = 1;
    private final static int STATE_SELECT_END = 2;

    private TimeZone timeZone;
    private DateTime startDate;
    private DateTime endDate;
    int selectState;

    Date[] invalidDates;

    private Toolbar toolbar;
    private TextView startDateTextView;
    private TextView endDateTextView;

    private Button saveSelectionButton;

    private boolean highlightStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        timeZone = TimeZone.getDefault();
        startDate = (DateTime) getIntent().getSerializableExtra(START_DATE);
        endDate = (DateTime) getIntent().getSerializableExtra(END_DATE);
        boolean hasPresetDate = (startDate != null && endDate != null);

        long[] invalidDateArray = getIntent().getLongArrayExtra(INVALID_DATES);
        invalidDates = net.villim.villim.VillimUtils.longArrayToDateArray(invalidDateArray);
        System.out.println(invalidDates.length);

        for (Date reservedDate : invalidDates) {
            System.out.println(reservedDate.getMonth());
            System.out.println(reservedDate.getDay());
            System.out.println(reservedDate.getYear());
        }


        highlightStartDate = true;

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Set up start date / end date select texts. */
        startDateTextView = (TextView) findViewById(R.id.start_date_text);
        endDateTextView = (TextView) findViewById(R.id.end_date_text);


        changeState(STATE_SELECT_START);

        /* Bottom button */
        saveSelectionButton = (Button) findViewById(R.id.save_selection_button);
        saveSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar nextMonth = Calendar.getInstance();
                nextMonth.set(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth());
                nextMonth.add(Calendar.MONTH, 1);
                nextMonth.add(Calendar.DATE, -1);
                DateTime nextMonthDate = new DateTime()
                        .withYear(nextMonth.get(Calendar.YEAR))
                        .withMonthOfYear(nextMonth.get(Calendar.MONTH))
                        .withDayOfMonth(nextMonth.get(Calendar.DATE));

                if (startDate.equals(endDate)){
                    Toast toast =  Toast.makeText(getApplicationContext(), R.string.select_different_dates, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else if (endDate.isBefore(nextMonthDate)) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.book_at_least_a_month, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(START_DATE, startDate);
                    returnIntent.putExtra(END_DATE, endDate);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

            }
        });

        /* Set up calendar. */
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        final CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        calendar.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
            @Override
            public boolean isDateSelectable(Date date) {
                for (Date reservedDate : invalidDates) {
                    if (date.equals(reservedDate)) {
                        return false;
                    }
                }
                return true;
            }
        });
        Date tomorrow = new Date(System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS);
        calendar.init(tomorrow, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                switch (selectState) {
                    case STATE_SELECT_START:
                        startDate = new DateTime(date);
                        changeState(STATE_SELECT_END);
                        endDate = null;
                        deactivateBototmButton();
                        break;
                    case STATE_SELECT_END:
                        if (date.before(startDate.toDate())) {
                            startDate = new DateTime(date);
                        } else {
                            endDate = new DateTime(date);
                            changeState(STATE_SELECT_START);
                        }
                        break;
                    default:
                        break;
                }

                setStartAndEndDateText(startDate, endDate);

                /* Set button clickable if both start date and end dates are set.
                   Highlight dates from startDate to endDate */
                if (startDate != null && endDate != null) {
                    activateBottomButton();
                }

            }

            @Override
            public void onDateUnselected(Date date) {
                //updateSelectedDates(calendar.getSelectedDates());
            }
        });

        calendar.setOnInvalidDateSelectedListener(new CalendarPickerView.OnInvalidDateSelectedListener() {
            @Override
            public void onInvalidDateSelected(Date date) {
                CharSequence text = getString(R.string.invalid_date_selected);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });

        if (hasPresetDate) {
            calendar.clearHighlightedDates();
            calendar.selectDate(startDate.toDate());
            calendar.selectDate(endDate.toDate());
            setStartAndEndDateText(startDate, endDate);
            activateBottomButton();
        } else {
            deactivateBototmButton();
        }
    }

    private void updateSelectedDates(List<DateTime> dates) {
        if (dates.size() > 0) {
            startDate = dates.get(0);
            endDate = dates.get(dates.size() - 1);
        } else {
            startDate = null;
            endDate = null;
        }
        setStartAndEndDateText(startDate, endDate);
    }

    private void setStartAndEndDateText(DateTime startDate, DateTime endDate) {
        /* Set start date text */
        if (startDate != null) {
            String startDateText = String.format(getString(R.string.date_filter_date_text_format), startDate.getMonthOfYear(), startDate.getDayOfMonth())
                    + "\n" + net.villim.villim.VillimUtils.getWeekday(this, startDate.getDayOfMonth());
            startDateTextView.setText(startDateText);
        } else {
            startDateTextView.setText(getString(R.string.date_filter_start_date));
        }

        /* Set end date text. */
        if (endDate != null) {
            String endDateText = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonthOfYear(), endDate.getDayOfMonth())
                    + "\n" + net.villim.villim.VillimUtils.getWeekday(this, endDate.getDayOfMonth());
            endDateTextView.setText(endDateText);
        } else {
            endDateTextView.setText(getString(R.string.date_filter_end_date));
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

                int colorId = highlightStartDate ?
                        getResources().getColor(R.color.date_filter_state_highlighted) :
                        getResources().getColor(R.color.date_filter_state_normal) ;

                startDateTextView.setTextColor(colorId);
                endDateTextView.setTextColor(getResources().getColor(R.color.date_filter_state_normal));
                break;
            case STATE_SELECT_END:
                highlightStartDate = false;
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

    private void deactivateBototmButton() {
        saveSelectionButton.setEnabled(false);
        saveSelectionButton.setBackgroundColor(getResources().getColor(R.color.dark_button_disabled));
        saveSelectionButton.setTextColor(Color.WHITE);
    }

    private void activateBottomButton() {
        saveSelectionButton.setEnabled(true);
        saveSelectionButton.setBackground(getResources().getDrawable(R.drawable.send_again_button));
        saveSelectionButton.setTextColor(getResources().getColorStateList(R.color.dark_button_text));
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
