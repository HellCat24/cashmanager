package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cashmanager.helo.com.R;
import cashmanager.helo.com.data.DataManager;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.MainActivity;

/**
 * Created by Oleg on 19.12.2014.
 */
public class RecordFragment extends Fragment {

    public static String BUNDLE_REPORT = "report";

    public static RecordFragment newInstance(Record record) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_REPORT, record);
        RecordFragment addRecordFragment = new RecordFragment();
        addRecordFragment.setArguments(bundle);
        return addRecordFragment;
    }

    private TextView mDatePicker;
    private TextView mCost;
    private TextView mDescription;
    private CheckBox mPrivate;

    private TextView mCategory;

    private ImageView mAttachmentImage;

    private Bitmap mAttachmentBitmap;

    private Button mEditBnt;
    private Button mCancelBnt;

    private Record mReport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_record, container, false);
        mReport = (Record) getArguments().getSerializable(BUNDLE_REPORT);
        initUI(view);
        setListeners();
        return view;
    }

    @Override
    public void onResume() {
        initEditRecord(DBHelper.get().getRecordsDataSource().getRecordById(mReport.id));
        super.onResume();
    }

    private void initUI(View view) {

        mPrivate = (CheckBox) view.findViewById(R.id.privateCheckBox);
        mDatePicker = (TextView) view.findViewById(R.id.txt_time);
        mDescription = (TextView) view.findViewById(R.id.etxt_description);
        mCategory = (TextView) view.findViewById(R.id.etxt_category);
        mAttachmentImage = (ImageView) view.findViewById(R.id.img_attach);
        mCost = (TextView) view.findViewById(R.id.etxt_cost);

        mEditBnt = (Button) view.findViewById(R.id.btn_edit);
        mCancelBnt = (Button) view.findViewById(R.id.btn_cancel);
    }

    private void setListeners() {
        OnCancelConfirmAddListener addListener = new OnCancelConfirmAddListener();
        mEditBnt.setOnClickListener(addListener);
        mCancelBnt.setOnClickListener(addListener);
        mAttachmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file:/" + mReport.filePath), "image/*");
                startActivity(intent);
            }
        });
    }

    private class OnCancelConfirmAddListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_edit:
                    ((MainActivity) getActivity()).setFragment(AddRecordFragment.newInstance(mReport), AddRecordFragment.class.getName());
                    break;
                case R.id.btn_cancel:
                    getActivity().onBackPressed();
                    break;
            }
        }
    }


    private void initEditRecord(Record record) {
        mPrivate.setChecked(record.isPrivate);
        mDatePicker.setText("Date:"+" "+record.date.toString());
        if(record.description.length()>0){
            mDescription.setText("Description:"+" "+record.description);
        } else {
            mDescription.setText("Description: empty");
        }

        mCost.setText("Cost:"+" "+record.cost);
        ImageLoader.getInstance().displayImage("file:/" + record.filePath, mAttachmentImage);
    }
}
