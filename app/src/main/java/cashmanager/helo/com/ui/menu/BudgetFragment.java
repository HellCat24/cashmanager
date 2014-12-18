package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import cashmanager.helo.com.R;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.model.bd.Budget;
import cashmanager.helo.com.ui.adapter.BudgetAdapter;
import cashmanager.helo.com.view.CustomProgressBar;

/**
 * Created by Mazhukin Oleh on 15.11.2014.
 */
public class BudgetFragment extends Fragment implements View.OnClickListener {

    private TextView mEmptyBudgetMessage;
    private TextView mIncomesTitle;

    private EditText mAddBudgetEditText;

    private ListView mBudgetListView;
    private BudgetAdapter mBudgetAdapter;

    private CustomProgressBar mBudgetBar;

    private Button mAddBudgetBtn;

    private BudgetData mBudgetData;

    private int mBudgetValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        mBudgetData = DBHelper.get().getBudgetDataSource();
        initUI(view);
        initData();
        return view;
    }

    private void initUI(View view) {
        mIncomesTitle = (TextView) view.findViewById(R.id.txt_incomes_title);
        mBudgetListView = (ListView) view.findViewById(R.id.budget_list_view);
        mAddBudgetEditText = (EditText) view.findViewById(R.id.etxt_budget);
        mBudgetBar = (CustomProgressBar) view.findViewById(R.id.budget_bar);
        mEmptyBudgetMessage = (TextView) view.findViewById(R.id.txt_empty_budget);
        mAddBudgetBtn = (Button) view.findViewById(R.id.btn_add_budget);
        mEmptyBudgetMessage.setOnClickListener(this);
        mAddBudgetBtn.setOnClickListener(this);
    }

    private void initData() {
        mBudgetValue = mBudgetData.getCurrentBudget();
        if (mBudgetValue != BudgetData.EMPTY_BUDGET) {
            mBudgetListView.setAdapter(new BudgetAdapter(getActivity(), mBudgetData.getBudgetList()));
            mBudgetBar.updateProgressTo(mBudgetValue);
            mAddBudgetBtn.setVisibility(View.VISIBLE);
            mAddBudgetEditText.setVisibility(View.VISIBLE);
            mIncomesTitle.setVisibility(View.VISIBLE);
            mEmptyBudgetMessage.setVisibility(View.GONE);
            mBudgetBar.setText(mBudgetValue + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_empty_budget:

                if (mAddBudgetEditText.getVisibility() == View.INVISIBLE) {
                    Animation fadeIn = new AlphaAnimation(0, 1);
                    Animation fadeOut = new AlphaAnimation(1, 0);
                    fadeIn.setDuration(getResources().getInteger(R.integer.mediumAnimationTime));
                    fadeOut.setDuration(getResources().getInteger(R.integer.mediumAnimationTime));
                    fadeIn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mAddBudgetBtn.setVisibility(View.VISIBLE);
                            mAddBudgetEditText.setVisibility(View.VISIBLE);
                            mEmptyBudgetMessage.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mEmptyBudgetMessage.startAnimation(fadeOut);
                    mAddBudgetEditText.startAnimation(fadeIn);
                    mAddBudgetBtn.startAnimation(fadeIn);
                }
                break;

            case R.id.btn_add_budget:

                int addedBudget = Integer.parseInt(mAddBudgetEditText.getText().toString());

                mBudgetValue = mBudgetValue+addedBudget;

                Budget budget = new Budget(new Date(), addedBudget);

                mBudgetData.addBudgetRecord(budget);
                mBudgetData.updateBudget(mBudgetValue);

                if(mBudgetAdapter==null){
                    mBudgetAdapter = new BudgetAdapter(getActivity(), new ArrayList<Budget>());
                    mBudgetListView.setAdapter(mBudgetAdapter);
                }
                mBudgetAdapter.add(budget);

                mBudgetBar.updateProgressTo(mBudgetValue);
                mBudgetBar.setText(Integer.toString(mBudgetValue));

                break;
        }
    }
}

