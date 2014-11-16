package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import cashmanager.helo.com.DBHelper;
import cashmanager.helo.com.R;
import cashmanager.helo.com.RecordsDataSource;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.adapter.RecordsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordListFragment extends Fragment {

    private ListView mRecordsList;
    private RecordsAdapter mRecordsAdapter;

    private RecordsDataSource mRecordsDataSource;

    private List<Record> mRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_list, container, false);
        mRecordsDataSource = DBHelper.get().getRecordsDataSource();
        initUI(rootView);
        return rootView;
    }

    private void initUI(View view) {
        mRecordsList = (ListView) view.findViewById(R.id.records_list);
        mRecordsList.setEmptyView(view.findViewById(android.R.id.empty));
    }

    private void initData() {
        List<Record> records = mRecordsDataSource.getRecordList();
        if (records != null) {
            if (mRecords == null) {
                initAdapter(records);
            } else {
                if (!mRecords.equals(records)) {
                    initAdapter(records);
                } else {
                    mRecordsList.setAdapter(mRecordsAdapter);
                }
            }
        }
    }

    private class onEditRecord implements View.OnClickListener {

        private Record mRecord;

        public  onEditRecord(Record record){
            mRecord = record;
        }

        @Override
        public void onClick(View v) {

        }
    }

    private void initAdapter(List<Record> records) {
        mRecords = records;
        mRecordsAdapter = new RecordsAdapter(getActivity(), records);
        mRecordsList.setAdapter(mRecordsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}