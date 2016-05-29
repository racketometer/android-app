package com.smap.f16.grp12.racketometer.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.DateFormatter;
import com.smap.f16.grp12.racketometer.utils.PerformanceFormatter;
import com.smap.f16.grp12.racketometer.utils.ToStringFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * How to setup the Horizontal-BarChart is inspired by this StackOverflow:
 * http://stackoverflow.com/a/32225844/5324369
 */
public class DetailsFragment extends Fragment implements OnMapReadyCallback {
    private Session session;
    private static final String SESSION =
            "com.smap.f16.grp12.racketometer.fragments.DetailsFragment.SESSION";

    private HorizontalBarChart barChart;
    private TextView dateInput;
    private TextView descriptionInput;
    private TextView hitsInput;
    private TextView performanceInput;
    private ShareButton shareButton;
    private MapView mapView;

    private Resources resources;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(Session session) {
        DetailsFragment fragment = new DetailsFragment();

        if (session != null) {
            Bundle args = new Bundle();

            args.putSerializable(SESSION, session);
            fragment.setArguments(args);
        }
        return fragment;
    }

    /**
     * The MapView fragment initialization was found on the below link
     * https://www.quora.com/How-can-I-open-map-in-map-fragment-inside-another-fragment-in-Android
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        resources = getResources();
        getSession();
        initUiReferences(rootView);
        initFields();
        initChart();
        return rootView;
    }

    private void initUiReferences(View view) {
        barChart = (HorizontalBarChart) view.findViewById(R.id.detailsBarChart);
        dateInput = (TextView) view.findViewById(R.id.date_input);
        descriptionInput = (TextView) view.findViewById(R.id.description_input);
        hitsInput = (TextView) view.findViewById(R.id.hits_input);
        performanceInput = (TextView) view.findViewById(R.id.performance_input);
        shareButton = (ShareButton) view.findViewById(R.id.fb_share_button);
    }

    private void initFields() {
        dateInput.setText(DateFormatter.Date(session.getDate()));
        descriptionInput.setText(session.getDescription());
        hitsInput.setText(String.valueOf(session.getHits()));
        performanceInput.setText(ToStringFormatter.FromDouble(PerformanceFormatter.PerformanceAsDouble(session)));

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://caveofcode.com"))
                .setImageUrl(Uri.parse("http://i.imgur.com/5zlG909.png"))
                .setContentTitle("Look at me skillz!")
                .build();

        shareButton.setShareContent(content);
    }

    /**
     * Gets sessions from fragment arguments.
     */
    private void getSession() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        session = (Session) args.getSerializable(SESSION);
    }

    /**
     * Initializes chart
     */
    private void initChart() {
        BarData data = new BarData(getXAxisValues(), getDataSet());
        barChart.setData(data);
        barChart.setDescription("");

        barChart.getLegend().setEnabled(false);


        XAxis verticalAxis = barChart.getXAxis();
        verticalAxis.setDrawGridLines(false);

        YAxis horizontalAxis = barChart.getAxisRight();
        horizontalAxis.setEnabled(false);
        horizontalAxis.setDrawGridLines(false);
        horizontalAxis.setDrawAxisLine(false);
        horizontalAxis.setAxisMinValue(0);

        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisLeft().setAxisMaxValue((float) getMaxValue() + 3);

        int textSize = resources.getDimensionPixelSize(R.dimen.chart_text_size);
        verticalAxis.setTextSize(textSize);
        horizontalAxis.setTextSize(textSize);
        data.setValueTextSize(resources.getDimension(R.dimen.chart_text_size));

        barChart.animateY(resources.getInteger(R.integer.chart_animation));
    }

    /**
     * Find maximum value of attributes.
     * @return The maximum value.
     */
    private double getMaxValue() {
        double maxValue = session.getPower();

        if(maxValue < session.getSpeed()) {
            maxValue = session.getSpeed();
        }

        if(maxValue < session.getAgility()) {
            maxValue = session.getAgility();
        }

        return maxValue;
    }

    /**
     * Get data sets.
     *
     * @return The data sets.
     */
    private IBarDataSet getDataSet() {
        ArrayList<BarEntry> dataSets = new ArrayList<>();
        BarEntry agilitySet = new BarEntry((float) session.getAgility(), 0);
        dataSets.add(agilitySet);

        BarEntry powerSet = new BarEntry((float) session.getPower(), 1);
        dataSets.add(powerSet);

        BarEntry speedSet = new BarEntry((float) session.getSpeed(), 2);
        dataSets.add(speedSet);

        BarDataSet barDataSet = new BarDataSet(dataSets, "");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        return barDataSet;
    }

    /**
     * Initializes XAxis Values of HorizontalBarChart
     *
     * @return List of axis string values.
     */
    private List<String> getXAxisValues() {
        List<String> xAxis = new ArrayList<>();
        xAxis.add(resources.getString(R.string.overview_agility));
        xAxis.add(resources.getString(R.string.overview_power));
        xAxis.add(resources.getString(R.string.overview_speed));
        return xAxis;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(56.193846, 10.247143);
        googleMap.addMarker(new MarkerOptions().position(location).title("You where here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 11));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
