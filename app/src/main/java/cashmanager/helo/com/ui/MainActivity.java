package cashmanager.helo.com.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cashmanager.helo.com.R;
import cashmanager.helo.com.ui.menu.BudgetFragment;
import cashmanager.helo.com.ui.menu.AddRecordFragment;
import cashmanager.helo.com.ui.menu.RecordListFragment;


public class MainActivity extends FragmentActivity {

    private FragmentManager mFragmentManager;

    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();
        initFragments(savedInstanceState);
        initUI();
    }

    private void initUI() {
        mMenuTitles = getResources().getStringArray(R.array.menu);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMenuTitles));
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, new RecordListFragment())
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_record:
                setFragment(new AddRecordFragment(), AddRecordFragment.class.getSimpleName());
                break;
            case R.id.action_add_money:
                setFragment(new BudgetFragment(), BudgetFragment.class.getSimpleName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mFragmentManager.findFragmentByTag(tag) != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
