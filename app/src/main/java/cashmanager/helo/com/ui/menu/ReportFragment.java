package cashmanager.helo.com.ui.menu;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cashmanager.helo.com.R;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.db.data.CategoryData;
import cashmanager.helo.com.db.data.RecordsData;
import cashmanager.helo.com.model.DateTime;
import cashmanager.helo.com.model.ReportItem;
import cashmanager.helo.com.model.bd.Category;
import cashmanager.helo.com.model.bd.Record;
import cashmanager.helo.com.ui.adapter.ReportAdapter;
import cashmanager.helo.com.view.CustomProgressBar;
import cashmanager.helo.com.view.DateTimePicker;

/**
 * Created by Mazhukin Oleh on 10.11.2014.
 */
public class ReportFragment extends Fragment implements View.OnClickListener {

    private CustomProgressBar mDiagram;

    private View mReportMainView;

    private RecordsData mRecordsData;
    private BudgetData mBudgetData;
    private CategoryData mCategoryData;

    private ListView mReportListView;
    private ReportAdapter mReportAdapter;

    private TextView mMaxPrice;
    private TextView mMinPrice;
    private TextView mWeekOverAll;
    private TextView mMonthOverAll;
    private TextView mBudgetLeft;

    private Date mStartDate;
    private Date mEndDate;

    private int mBudgetValue;

    private boolean isPrivate;

    private ActionBar mActionBar;

    private enum ReportType {Day, Week, Month, Dates, Category, Costs}

    private DateTimePicker.OnDateTimeSetListener myCallBack = new DateTimePicker.OnDateTimeSetListener() {

        private boolean isEndDate;

        @Override
        public void DateTimeSet(Date date) {
            if (!isEndDate) {
                isEndDate = true;
                mStartDate = date;
                showTimeDialog("Select end date");
            } else {
                isEndDate = false;
                mEndDate = date;
                initDiagram(getRecordsInDate(new Date[]{mStartDate, mEndDate}));
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        DBHelper dbHelper = DBHelper.get();
        mActionBar = getActivity().getActionBar();
        isPrivate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(getString(R.string.settings_private), false);
        mRecordsData = dbHelper.getRecordsDataSource();
        mBudgetData = dbHelper.getBudgetDataSource();
        mCategoryData = dbHelper.getCategoryData();
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
        initActionBar();
    }

    private void initActionBar() {
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.Report));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActionBar.setListNavigationCallbacks(spinnerArrayAdapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                switch (ReportType.values()[itemPosition]) {
                    case Day:
                        initDiagram(getRecordsWithinFilter(RecordsData.TimeSearchType.DAY));
                        break;
                    case Week:
                        initDiagram(getRecordsWithinFilter(RecordsData.TimeSearchType.WEEK));
                        break;
                    case Month:
                        initDiagram(getRecordsWithinFilter(RecordsData.TimeSearchType.MONTH));
                        break;
                    case Dates:
                        showTimeDialog(getString(R.string.select_start_date));
                        break;
                    case Category:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(getString(R.string.choose_category))
                                .setItems(mCategoryData.getCategoryTitleList(), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        initDiagram(getRecordsWithCategory(which + 1));
                                    }
                                });
                        builder.create().show();
                        break;
                    case Costs:
                        initDiagram(getSortRecords());
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        mMaxPrice.setText(getString(R.string.max_cost) + " " + mRecordsData.getMaxPrice());
        mMinPrice.setText(getString(R.string.min_cost) + " " + mRecordsData.getMinPrice());
        mWeekOverAll.setText(getString(R.string.week_overall) + " " + mRecordsData.getRecordsPrice(RecordsData.TimeSearchType.WEEK));
        mMonthOverAll.setText(getString(R.string.month_overall) + " " + mRecordsData.getRecordsPrice(RecordsData.TimeSearchType.MONTH));
    }

    private List<Record> getRecordsWithinFilter(RecordsData.TimeSearchType timeSearchType) {
        return mRecordsData.getRecordListWithinDates(timeSearchType, isPrivate);
    }

    private List<Record> getRecordsInDate(Date[] dates) {
        return mRecordsData.getRecordsInDates(dates, isPrivate);
    }

    private List<Record> getRecordsWithCategory(int categoryId) {
        return mRecordsData.getRecordsWithCategory(categoryId, isPrivate);
    }

    private List<Record> getSortRecords() {
        return mRecordsData.getSortRecords(isPrivate);
    }

    private void initDiagram(final List<Record> records) {
        final List<Float> floats = new ArrayList<Float>();
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
                mBudgetLeft.setText("Budget left: " + (mBudgetData.getCurrentBudget() - sum));
                mReportListView.setAdapter(new ReportAdapter(getActivity(), reports));
            }
        });
        mDiagram.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_empty_budget:

        }
    }

    private void showTimeDialog(String title) {
        DateTimePicker dateTimePicker = DateTimePicker.newInstance(new Date(), title);
        dateTimePicker.show(getFragmentManager(), DateTimePicker.class.getName());
        dateTimePicker.setOnDateTimeSetListener(myCallBack);
    }

    @Override
    public void onDetach() {
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDetach();
    }
}