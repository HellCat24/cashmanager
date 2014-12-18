package cashmanager.helo.com.ui.menu;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.R;
import cashmanager.helo.com.db.data.RecordsData;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.adapter.RecordsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordListFragment extends Fragment {

    private ListView mRecordsList;
    private RecordsAdapter mRecordsAdapter;

    private RecordsData mRecordsDataSource;

    private List<Record> mRecords;
    private SharedPreferences mSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_record_list, container, false);
        mRecordsDataSource = DBHelper.get().getRecordsDataSource();
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        initUI(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mRecordsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initUI(View view) {
        mRecordsList = (ListView) view.findViewById(R.id.records_list);
        mRecordsList.setEmptyView(view.findViewById(android.R.id.empty));
        mRecordsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setPositiveButton("Delete dialog", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mRecordsAdapter.deleteItem(position);
                        mRecordsDataSource.deleteRecord(mRecordsAdapter.getItem(position).id);
                    }
                });
                b.show();
                return false;
            }
        });
    }

    private void initData() {
        List<Record> records = mRecordsDataSource.getRecordList(mSettings.getBoolean(getString(R.string.settings_private), false));
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

        public onEditRecord(Record record) {
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