package cashmanager.helo.com.ui.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cashmanager.helo.com.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecordListFragment extends Fragment {

    public RecordListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }
}