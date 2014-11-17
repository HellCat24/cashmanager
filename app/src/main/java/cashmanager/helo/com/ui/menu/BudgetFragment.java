package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import cashmanager.helo.com.R;
import cashmanager.helo.com.view.CustomProgressBar;

/**
 * Created by Mazhukin Oleh on 15.11.2014.
 */
public class BudgetFragment extends Fragment implements View.OnClickListener {

    private EditText mBudgetText;
    private TextView mEmptyBudgetMessage;
    private CustomProgressBar mBudgetBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mBudgetText = (EditText) view.findViewById(R.id.etxt_budget);
        mBudgetBar = (CustomProgressBar) view.findViewById(R.id.budget_bar);
        mEmptyBudgetMessage = (TextView) view.findViewById(R.id.txt_empty_budget);
        mEmptyBudgetMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mBudgetText.getVisibility()==View.INVISIBLE){
            Animation fadeIn = new AlphaAnimation(0,1);
            fadeIn.setDuration(getResources().getInteger(R.integer.mediumAnimationTime));
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mBudgetText.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mBudgetText.startAnimation(fadeIn);
        } else {

            mBudgetBar.updatePrjgressTo(Integer.parseInt(mBudgetText.getText().toString()));
        }
    }
}

