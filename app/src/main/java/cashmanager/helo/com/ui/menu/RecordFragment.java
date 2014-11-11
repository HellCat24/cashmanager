package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import cashmanager.helo.com.R;
import cashmanager.helo.com.model.Record;
import cashmanager.helo.com.utils.ImageRetriever;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class RecordFragment extends Fragment {

    //Add auto
    private EditText mTitle;
    private TimePicker mTimePicker;
    private EditText mDescription;
    private Spinner mCategory;
    private ImageView mAttachmentImage;

    private Bitmap mAttachmentBitmap;

    private Button mAddBnt;
    private Button mCancelBnt;

    private ImageRetriever mImageRetriever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);
        mImageRetriever = new ImageRetriever(getActivity());
        initUI(view);
        setUpListeners();
        initData();
        return view;
    }

    private void initUI(View view) {

        mTitle = (EditText) view.findViewById(R.id.etxt_title);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        mDescription = (EditText) view.findViewById(R.id.etxt_description);
        mCategory = (Spinner) view.findViewById(R.id.category_spinner);
        mAttachmentImage = (ImageView) view.findViewById(R.id.img_attach);

        mAddBnt = (Button) view.findViewById(R.id.btn_add);
        mCancelBnt = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void setUpListeners() {
        OnCancelConfirmAddListener onCancelConfirmAddListener = new OnCancelConfirmAddListener();
        mAddBnt.setOnClickListener(onCancelConfirmAddListener);
        mCancelBnt.setOnClickListener(onCancelConfirmAddListener);
        mAttachmentImage.setOnClickListener(new View.OnClickListener() {
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
            }
        }
    }

    private void addRecord() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();

    }

    private void addRecord(Record record){

    }

    private void initData() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minutes = rightNow.get(Calendar.MINUTE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String bitmapPath = mImageRetriever.getImagePath(requestCode, resultCode, data);
        mAttachmentBitmap = mImageRetriever.getBitmapFromPath(bitmapPath);
        if (mAttachmentBitmap != null) mAttachmentImage.setImageBitmap(mAttachmentBitmap);
    }
}
