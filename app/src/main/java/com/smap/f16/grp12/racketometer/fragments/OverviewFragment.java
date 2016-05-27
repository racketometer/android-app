package com.smap.f16.grp12.racketometer.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.models.Attributes;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.OverviewChart;
import com.smap.f16.grp12.racketometer.utils.PerformanceFormatter;
import com.smap.f16.grp12.racketometer.utils.ToStringFormatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Overview fragment to present averaged data on a users performances.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    private static final String LOG = "OverviewFragment";
    private static final String ARG_SESSIONS
            = "com.smap.f16.grp12.racketometer.fragments.OverviewFragment.SESSIONS";

    private List<Session> sessions;

    private TextView txtSpeed;
    private TextView txtAgility;
    private TextView txtPower;
    private BarChart chtHistory;

    private OverviewChart overviewChart;

    public OverviewFragment() { }

    /**
     * Create instance of Overview Fragment.
     *
     * @return A new instance of fragment OverviewFragment.
     * @param sessions The sessions.
     */
    public static OverviewFragment newInstance(List<Session> sessions) {
        OverviewFragment fragment = new OverviewFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_SESSIONS, (Serializable) sessions);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();
        if(arguments != null) {
            sessions = (List<Session>) arguments.getSerializable(ARG_SESSIONS);
            if(sessions == null) {
                sessions = new ArrayList<>();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        initUiReferences(view);

        Resources resources = getResources();
        overviewChart = new OverviewChart(chtHistory, resources);
        updateView();
        return view;
    }

    /**
     * Set list of {@link Session}.
     * @param sessions The Sessions.
     */
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
        updateView();
    }

    /**
     * Update all view elements.
     */
    private void updateView() {
        Attributes attributes = PerformanceFormatter.getAveragedAttributes(this.sessions);

        if(attributes == null || this.sessions == null || this.sessions.size() == 0) {
            return;
        }

        setSpeed(ToStringFormatter.FromDouble(attributes.getSpeed()));
        setAgility(ToStringFormatter.FromDouble(attributes.getAgility()));
        setPower(ToStringFormatter.FromDouble(attributes.getPower()));
        overviewChart.setData(sessions);
    }

    private void setPower(String power) {
        txtPower.setText(power);
    }

    private void setAgility(String agility) {
        txtAgility.setText(agility);
    }

    private void setSpeed(String speed) {
        txtSpeed.setText(speed);
    }

    private void initUiReferences(View view) {
        txtSpeed = (TextView) view.findViewById(R.id.txt_speed);
        txtAgility = (TextView) view.findViewById(R.id.txt_agility);
        txtPower = (TextView) view.findViewById(R.id.txt_power);
        chtHistory = (BarChart) view.findViewById(R.id.cht_history);
    }
}
