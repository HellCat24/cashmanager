package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import cashmanager.helo.com.R;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.db.data.RecordsData;
import cashmanager.helo.com.model.ReportItem;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.adapter.ReportAdapter;
import cashmanager.helo.com.view.CustomProgressBar;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    private CustomProgressBar mDiagram;

    private View mReportMainView;

    private RecordsData mRecordsData;
    private BudgetData mBudgetData;

    private ListView mReportListView;
    private ReportAdapter mReportAdapter;

    private TextView mMaxPrice;
    private TextView mMinPrice;
    private TextView mWeekOverAll;
    private TextView mMonthOverAll;
    private TextView mBudgetLeft;

    private SharedPreferences mSettings;

    private int mBudgetValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        DBHelper dbHelper = DBHelper.get();
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mRecordsData = dbHelper.getRecordsDataSource();
        mBudgetData = dbHelper.getBudgetDataSource();
        initUI(view);
        initData();
        return view;
    }

    private void initUI(View view) {
        mReportMainView = view.findViewById(R.id.report_main_view);
        mDiagram = (CustomProgressBar) view.findViewById(R.id.report_diagram);
        mReportListView = (ListView) view.findViewById(R.id.report_list);
        mReportListView.setEmptyView(view.findViewById(android.R.id.empty));
        mMaxPrice = (TextView) view.findViewById(R.id.txt_max_price);
        mMinPrice = (TextView) view.findViewById(R.id.txt_min_price);
        mWeekOverAll = (TextView) view.findViewById(R.id.txt_week);
        mMonthOverAll = (TextView) view.findViewById(R.id.txt_month);
        mBudgetLeft = (TextView) view.findViewById(R.id.txt_bughet_left);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(getResources().getInteger(R.integer.mediumAnimationTime));
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mReportMainView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mReportMainView.startAnimation(fadeIn);
    }

    private void initData() {
        mMaxPrice.setText("Max cost: "+mRecordsData.getMaxPrice());
        mMinPrice.setText("Min cost: "+mRecordsData.getMinPrice());
        mWeekOverAll.setText("Week overall: "+ mRecordsData.getRecordsPrice(RecordsData.TimeSearchType.WEEK) + "");
        mMonthOverAll.setText("Month overall: " +mRecordsData.getRecordsPrice(RecordsData.TimeSearchType.MONTH) + "");
        if (mRecordsData.getRecordList(mSettings.getBoolean(getString(R.string.settings_private), false)).size() > 0) {
            initDiagram();
        }
    }

    private void initDiagram() {
        final List<Float> floats = new ArrayList<Float>();
        final List<Record> records = mRecordsData.getRecordList(mSettings.getBoolean(getString(R.string.settings_private), false));
        final List<ReportItem> reports = new ArrayList<ReportItem>();
        for (Record record : records) {
            reports.add(new ReportItem(record));
            floats.add(Float.valueOf(record.cost));
        }
        mDiagram.setDiagramParams(floats, new CustomProgressBar.OnDiagramComplete() {
            @Override
            public void onDiagramComplete(float sum, int[] colors) {
                for (int i = 0; i < records.size(); i++) {
                    reports.get(i).color = colors[i];
                }
                mBudgetLeft.setText("Budget left: "+(mBudgetData.getCurrentBudget() - sum));
                mReportListView.setAdapter(new ReportAdapter(getActivity(), reports));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_empty_budget:

        }
    }

}