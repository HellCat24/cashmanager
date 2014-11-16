package cashmanager.helo.com.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TabHost;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import cashmanager.helo.com.R;
import cashmanager.helo.com.model.DateTime;

public class DateTimePicker extends DialogFragment {

    public static final String TAG_FRAG_DATE_TIME = "fragDateTime";
    private static final String KEY_DIALOG_TITLE = "dialogTitle";
    private static final String KEY_INIT_DATE = "initDate";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private Context mContext;
    private ButtonClickListener mButtonClickListener;
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private Bundle mArgument;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Calendar c = Calendar.getInstance();
        mCurrentYear = c.get(Calendar.YEAR);
        mCurrentMonth = c.get(Calendar.MONTH);
        mCurrentDay = c.get(Calendar.DAY_OF_MONTH);
        mContext = activity;
        mButtonClickListener = new ButtonClickListener();
    }

    public static DateTimePicker newInstance(Date initDate) {
        DateTimePicker mDateTimePicker = new DateTimePicker();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(KEY_INIT_DATE, initDate);
        mDateTimePicker.setArguments(mBundle);
        return mDateTimePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgument = getArguments();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setTitle(mArgument.getCharSequence(KEY_DIALOG_TITLE));
        mBuilder.setNegativeButton(android.R.string.no, mButtonClickListener);
        mBuilder.setPositiveButton(android.R.string.yes, mButtonClickListener);
        AlertDialog mDialog = mBuilder.create();
        mDialog.setView(
                createDateTimeView(mDialog.getLayoutInflater())
        );
        return mDialog;
    }


    private View createDateTimeView(LayoutInflater layoutInflater) {
        View mView = layoutInflater.inflate(R.layout.custom_time_date, null);
        TabHost mTabHost = (TabHost) mView.findViewById(R.id.tab_host);
        mTabHost.setup();
        TabHost.TabSpec mDateTab = mTabHost.newTabSpec(TAG_DATE);
        mDateTab.setIndicator(getString(R.string.tab_date));
        mDateTab.setContent(R.id.date_content);
        mTabHost.addTab(mDateTab);
        TabHost.TabSpec mTimeTab = mTabHost.newTabSpec(TAG_TIME);
        mTimeTab.setIndicator(getString(R.string.tab_time));
        mTimeTab.setContent(R.id.time_content);
        mTabHost.addTab(mTimeTab);
        DateTime mDateTime = new DateTime((Date) mArgument.getSerializable(KEY_INIT_DATE));
        mDatePicker = (DatePicker) mView.findViewById(R.id.date_picker);
        mTimePicker = (TimePicker) mView.findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);
        mDatePicker.init(mDateTime.getYear(), mDateTime.getMonthOfYear(), mDateTime.getDayOfMonth(), new OnDataChanged());
        mTimePicker.setCurrentHour(mDateTime.getHourOfDay());
        mTimePicker.setCurrentMinute(mDateTime.getMinuteOfHour());
        return mView;
    }


private class OnDataChanged implements DatePicker.OnDateChangedListener{

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        if (year < mCurrentYear)
            datePicker.updateDate(mCurrentYear, mCurrentMonth, mCurrentDay);

        if (month < mCurrentMonth && year == mCurrentYear)
            datePicker.updateDate(mCurrentYear, mCurrentMonth, mCurrentDay);


        if (day < mCurrentDay && month == mCurrentMonth && mCurrentYear == mCurrentYear)
            datePicker.updateDate(mCurrentYear, mCurrentMonth, mCurrentDay);
    }
}

    public void setOnDateTimeSetListener(OnDateTimeSetListener onDateTimeSetListener) {
        mOnDateTimeSetListener = onDateTimeSetListener;
    }

    private class ButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int result) {
            if (DialogInterface.BUTTON_POSITIVE == result) {
                DateTime mDateTime = new DateTime(
                        mDatePicker.getYear(),
                        mDatePicker.getMonth(),
                        mDatePicker.getDayOfMonth(),
                        mTimePicker.getCurrentHour(),
                        mTimePicker.getCurrentMinute()
                );
                mOnDateTimeSetListener.DateTimeSet(mDateTime.getDate());
            }
        }
    }


    public interface OnDateTimeSetListener {
        public void DateTimeSet(Date date);
    }
}