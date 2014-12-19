package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.R;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.db.data.CategoryData;
import cashmanager.helo.com.db.data.RecordsData;
import cashmanager.helo.com.model.bd.Category;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.MainActivity;
import cashmanager.helo.com.utils.ImageRetriever;
import cashmanager.helo.com.view.DateTimePicker;
import cashmanager.helo.com.view.Utils;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class AddRecordFragment extends Fragment {

    public static String BUNDLE_REPORT = "report";

    public static AddRecordFragment newInstance(Record record) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REPORT, record);
        AddRecordFragment addRecordFragment = new AddRecordFragment();
        addRecordFragment.setArguments(bundle);
        return addRecordFragment;
    }

    private EditText mDatePicker;
    private EditText mCost;
    private AutoCompleteTextView mDescription;
    private TextView mChooseImage;
    private CheckBox mPrivate;

    private String mBitmapPath;

    private AutoCompleteTextView mCategory;

    private ImageView mAttachmentImage;

    private Bitmap mAttachmentBitmap;

    private Button mAddBnt;
    private Button mCancelBnt;

    private ImageRetriever mImageRetriever;

    private Date mDate;

    private SharedPreferences mSettings;

    private RecordsData mRecordsDataSource;
    private BudgetData mBudgetData;
    private CategoryData mCategoryData;

    private int mBudgetValue;

    private Record mReportToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mImageRetriever = new ImageRetriever(getActivity());
        DBHelper dbHelper = DBHelper.get();
        mRecordsDataSource = dbHelper.getRecordsDataSource();
        mBudgetData = dbHelper.getBudgetDataSource();
        mCategoryData = dbHelper.getCategoryData();
        mDate = new Date();
        initUI(view);
        setUpListeners();
        initData();
        if (getArguments() != null) {
            mReportToEdit = (Record) getArguments().getSerializable(BUNDLE_REPORT);
        }
        if (mReportToEdit != null) {
            initEditRecord(mReportToEdit);
        }
        return view;
    }

    private void initUI(View view) {

        mPrivate = (CheckBox) view.findViewById(R.id.privateCheckBox);
        mDatePicker = (EditText) view.findViewById(R.id.txt_time);
        mDescription = (AutoCompleteTextView) view.findViewById(R.id.etxt_description);
        ArrayAdapter<String> titleAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mRecordsDataSource.getRecordsTitle());
        mDescription.setAdapter(titleAdapter);
        mDescription.setThreshold(2);
        mDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDescription.showDropDown();
                return false;
            }
        });
        mCategory = (AutoCompleteTextView) view.findViewById(R.id.etxt_category);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mCategoryData.getCategoryTitleList());
        mCategory.setAdapter(categoryAdapter);
        mCategory.setThreshold(2);
        mCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mCategory.showDropDown();
                return false;
            }
        });
        mChooseImage = (TextView) view.findViewById(R.id.img_choose_attach);
        mAttachmentImage = (ImageView) view.findViewById(R.id.img_attach);
        mCost = (EditText) view.findViewById(R.id.etxt_cost);

        mAddBnt = (Button) view.findViewById(R.id.btn_add);
        mCancelBnt = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void setUpListeners() {
        OnCancelConfirmAddListener onCancelConfirmAddListener = new OnCancelConfirmAddListener();
        mAddBnt.setOnClickListener(onCancelConfirmAddListener);
        mDatePicker.setOnClickListener(onCancelConfirmAddListener);
        mCancelBnt.setOnClickListener(onCancelConfirmAddListener);
        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(mImageRetriever.getImageFromCameraIntent(), ImageRetriever.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    private class OnCancelConfirmAddListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_add:
                    addRecord();
                    break;
                case R.id.btn_cancel:
                    getActivity().onBackPressed();
                    break;
                case R.id.txt_time:
                    showTimeDialog();
                    break;
            }
        }
    }

    private void addRecord() {

        String description = mDescription.getText().toString();

        String date = mDatePicker.getText().toString();

        String costString = mCost.getText().toString();

        if (TextUtils.isEmpty(costString)) {
            mCost.setError("Please set cost");
            return;
        }

        Integer cost = Integer.parseInt(costString);

        Record record = new Record();
        record.description = description;
        record.date = mDate;
        record.cost = cost;
        record.isPrivate = mPrivate.isChecked();
        record.categoryTitle = mCategory.getText().toString();
        record.filePath = mBitmapPath;
        if(mReportToEdit!=null){
            record.id = mReportToEdit.id;
        }
        mBudgetData.updateBudget(mBudgetValue - record.cost);
        mRecordsDataSource.addRecord(record);
        if(record.categoryTitle.length()>0){
            mCategoryData.addCategory(record.categoryTitle);
        }
        checkLimitations();
    }


    private void checkLimitations() {

        Boolean isNotificationAvailable = mSettings.getBoolean(getString(R.string.settings_notification), false);
        if (isNotificationAvailable) {
            int maxCostPerDay = mSettings.getInt(getString(R.string.settings_limitations_per_day), 0);
            if (maxCostPerDay > 0) {
                int dayCost = mRecordsDataSource.getRecordsPrice(RecordsData.TimeSearchType.DAY);
                if (dayCost > maxCostPerDay) {
                    sendNotification(dayCost, RecordsData.TimeSearchType.DAY);
                }
            }
            int maxCostPerWeek = mSettings.getInt(getString(R.string.settings_limitations_per_week), 0);
            if (maxCostPerWeek > 0) {
                int weekCost = mRecordsDataSource.getRecordsPrice(RecordsData.TimeSearchType.WEEK);
                if (weekCost > maxCostPerWeek) {
                    sendNotification(weekCost, RecordsData.TimeSearchType.WEEK);
                }
            }
            int maxCostPerMonth = mSettings.getInt(getString(R.string.settings_limitations_per_week), 0);
            if (maxCostPerMonth > 0) {
                int monthCost = mRecordsDataSource.getRecordsPrice(RecordsData.TimeSearchType.WEEK);
                if (monthCost > maxCostPerMonth) {
                    sendNotification(monthCost, RecordsData.TimeSearchType.MONTH);
                }
            }
        }

        Toast.makeText(getActivity(), getActivity().getString(R.string.record_added), Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    private void sendNotification(int spendCost, RecordsData.TimeSearchType type) {
        String title = "";
        String message = "";
        switch (type) {
            case DAY:
                title = "Limit per day is exceeded";
                message = "Today you've already spend %d";
                break;
            case WEEK:
                title = "Limit per week is exceeded";
                message = "This week you've already spend %d";
                break;
            case MONTH:
                title = "Limit per month is exceeded";
                message = "This month you've already spend %d";
                break;
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(String.format(message, spendCost));
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void initData() {
        mBudgetValue = mBudgetData.getCurrentBudget();
        mDatePicker.setText(Utils.getDateAndTime(mDate));
    }

    private void initEditRecord(Record record) {
        mPrivate.setChecked(record.isPrivate);
        mDatePicker.setText(record.date.toString());
        mDescription.setText(record.description);
        mCategory.setText(record.categoryTitle);
        ImageLoader.getInstance().displayImage("file:/" + record.filePath, mAttachmentImage);
        mCost.setText(record.cost+"");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBitmapPath = mImageRetriever.getImagePath(requestCode, resultCode, data);
        if (mBitmapPath.length() > 0) {
            mAttachmentBitmap = mImageRetriever.getBitmapFromPath(mBitmapPath);
            if (mAttachmentBitmap != null) {
                mAttachmentImage.setImageBitmap(mAttachmentBitmap);
                mChooseImage.setVisibility(View.GONE);
            }
        }
    }

    private void showTimeDialog() {
        DateTimePicker dateTimePicker = DateTimePicker.newInstance(new Date(), getString(R.string.select_date));
        dateTimePicker.show(getFragmentManager(), DateTimePicker.class.getName());
        DateTimePicker.OnDateTimeSetListener myCallBack = new DateTimePicker.OnDateTimeSetListener() {

            @Override
            public void DateTimeSet(Date date) {
                mDate = date;
                mDatePicker.setText(Utils.getDateAndTime(mDate));
            }
        };
        dateTimePicker.setOnDateTimeSetListener(myCallBack);
    }
}
