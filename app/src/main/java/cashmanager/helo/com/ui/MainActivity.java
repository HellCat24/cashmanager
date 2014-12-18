package cashmanager.helo.com.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Date;

import cashmanager.helo.com.R;
import cashmanager.helo.com.data.MyMenu;
import cashmanager.helo.com.db.DBHelper;
import cashmanager.helo.com.db.data.BudgetData;
import cashmanager.helo.com.db.data.CategoryData;
import cashmanager.helo.com.model.bd.Budget;
import cashmanager.helo.com.ui.menu.BudgetFragment;
import cashmanager.helo.com.ui.menu.AddRecordFragment;
import cashmanager.helo.com.ui.menu.RecordListFragment;
import cashmanager.helo.com.ui.menu.ReportFragment;


public class MainActivity extends FragmentActivity {

    public static String ACTION_REPORT = "action_report";
    public static String ACTION_ADD = "action_add";

    private static String IS_FIRST_LAUNCH = "is_first_launch";

    private FragmentManager mFragmentManager;

    private String[] mMenuTitles;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getFragmentManager();
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSettings.getBoolean(IS_FIRST_LAUNCH, true)) {
            mSettings.edit().putBoolean(IS_FIRST_LAUNCH, false).apply();
            CategoryData categoryData  = DBHelper.get().getCategoryData();
            for (String categoryTitle : getResources().getStringArray(R.array.Category)) {
                categoryData.addCategory(categoryTitle);
            }
        }
        initData();
        initUI();
    }

    private void initUI() {
        mMenuTitles = getResources().getStringArray(R.array.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMenuTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu item = MyMenu.values()[position];
                switch (item) {
                    case RECORDS:
                        setFragment(new RecordListFragment(), RecordListFragment.class.getName());
                        break;
                    case REPORT:
                        setFragment(new ReportFragment(), ReportFragment.class.getName());
                        break;
                    case SETTINGS:
                        setFragment(new SettingsFragment(), SettingsFragment.class.getName());
                        break;
                }
                mDrawerLayout.closeDrawers();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                0,  /* "open drawer" description */
                0  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(getString(R.string.app_name));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Menu");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initData() {
        BudgetData data = DBHelper.get().getBudgetDataSource();
        if (data.getBudgetList().size() == 0) {
            data.addBudgetRecord(new Budget(new Date(), BudgetData.EMPTY_BUDGET));
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initFragments() {
        setFragment(new RecordListFragment(), RecordListFragment.class.getName());
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if(action.equals(ACTION_REPORT)){
            setFragment(new ReportFragment(), ReportFragment.class.getName());
        } else if(action.equals(ACTION_ADD)){
            setFragment(new AddRecordFragment(), AddRecordFragment.class.getName());
        } else {
            initFragments();
        }
        super.onResume();
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
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
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
