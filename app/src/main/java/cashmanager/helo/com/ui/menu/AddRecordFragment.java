package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import cashmanager.helo.com.DBHelper;
import cashmanager.helo.com.R;
import cashmanager.helo.com.RecordsDataSource;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.utils.ImageRetriever;
import cashmanager.helo.com.view.DateTimePicker;
import cashmanager.helo.com.view.Utils;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class AddRecordFragment extends Fragment {

    private EditText mDatePicker;
    private EditText mCost;
    private EditText mDescription;
    private TextView mChooseImage;

    private String mBitmapPath;

    private MultiAutoCompleteTextView mCategory;

    private ImageView mAttachmentImage;

    private Bitmap mAttachmentBitmap;

    private Button mAddBnt;
    private Button mCancelBnt;

    private ImageRetriever mImageRetriever;

    private Date mDate;

    private RecordsDataSource mRecordsDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);
        mImageRetriever = new ImageRetriever(getActivity());
        mRecordsDataSource = DBHelper.get().getRecordsDataSource();
        mDate = new Date();
        initUI(view);
        setUpListeners();
        initData();
        return view;
    }

    private void initUI(View view) {

        mDatePicker = (EditText) view.findViewById(R.id.txt_time);
        mDescription = (EditText) view.findViewById(R.id.etxt_description);
        mCategory = (MultiAutoCompleteTextView) view.findViewById(R.id.etxt_category);
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
        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        if(TextUtils.isEmpty(costString)){
            mCost.setError("Please set cost");
            return;
        }

        Integer cost = Integer.parseInt(costString);

        Record record= new Record();
        record.description = description;
        record.date = mDate;
        record.cost = cost;
        /*if(mBitmapPath!=null){
            record.attachment = new Attachment();
            record.attachment.file = mBitmapPath;
        }*/
        mRecordsDataSource.addRecord(record);

        Toast.makeText(getActivity(), getActivity().getString(R.string.record_added), Toast.LENGTH_SHORT).show();

        getActivity().onBackPressed();
    }


    private void initData() {
        mDatePicker.setText(Utils.getDateAndTime(mDate));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBitmapPath = mImageRetriever.getImagePath(requestCode, resultCode, data);
        if(mBitmapPath.length()>0){
            mAttachmentBitmap = mImageRetriever.getBitmapFromPath(mBitmapPath);
            if (mAttachmentBitmap != null){
                mAttachmentImage.setImageBitmap(mAttachmentBitmap);
                mChooseImage.setVisibility(View.GONE);
            }
        }
    }

    private void showTimeDialog() {
        DateTimePicker dateTimePicker = DateTimePicker.newInstance(new Date());
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
