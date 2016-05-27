package com.smap.f16.grp12.racketometer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smap.f16.grp12.racketometer.R;

/**
 * No Data Fragment to present a user for a simple UI in case of no available data.
 * Use the {@link NoDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoDataFragment extends Fragment {
    public NoDataFragment() { }

    /**
     * Create instance of No Data fragment.
     *
     * @return A new instance of fragment NoDataFragment.
     */
    public static NoDataFragment newInstance() {
        return new NoDataFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_data, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }
}
