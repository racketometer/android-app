package com.smap.f16.grp12.racketometer.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
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
 * Stuff.
 * How to setup the Horizontal-BarChart is inspired by the answer from Amir in this stackoverflow:
 * http://stackoverflow.com/questions/28850411/mpandroidchart-barchart-horizontal-vertical
 */
public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    // viewmodel
    private Session session;
    // const string
    private static final String SESSION =
            "com.smap.f16.grp12.racketometer.fragments.HistoryFragment.SESSION";
    // View related
    private HorizontalBarChart barChart;
    private TextView dateInput;
    private TextView descriptionInput;
    private TextView hitsInput;
    private TextView performanceInput;
    private ShareButton shareButton;
    private GoogleMap mMap;
    private MapView mapView;
    private View rootView;

    public DetailsFragment() {}

    public static DetailsFragment newInstance(Session session) {
        DetailsFragment fragment = new DetailsFragment();

        if(session != null) {
            Bundle args = new Bundle();

            args.putSerializable(SESSION, session);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * The MapView fragment initialization was found on the below link
     * https://www.quora.com/How-can-I-open-map-in-map-fragment-inside-another-fragment-in-Android
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        MapsInitializer.initialize(this.getActivity());
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        getSession();
        initUiReferences(rootView);
        initFields();
        setBarData();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initUiReferences(View view) {
        barChart = (HorizontalBarChart) view.findViewById(R.id.detailsBarChart);
        dateInput = (TextView) view.findViewById(R.id.date_input);
        descriptionInput = (TextView) view.findViewById(R.id.description_input);
        hitsInput = (TextView) view.findViewById(R.id.hits_input);
        performanceInput = (TextView) view.findViewById(R.id.performance_input);
        shareButton = (ShareButton)view.findViewById(R.id.fb_share_button);
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
        if(args == null) {
            return;
        }
        session = (Session) args.getSerializable(SESSION);
    }

    /**
     * Initializes the barData
     */
    private void setBarData() {
        BarData t = new BarData();
        BarData data = new BarData(getXAxisValues(), getDataSet());
        barChart.setData(data);
        barChart.setDescription("My Chart");
        barChart.animateY(2500, Easing.EasingOption.EaseOutSine);
        barChart.invalidate();
    }

    /**
     * Initializes the barDataSet
     * @return
     */
    private IBarDataSet getDataSet() {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        float[] test = new float[] {0,(float)session.getAgility() };
        BarEntry v1e1 = new BarEntry((float)session.getAgility(), 0); // Agility
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry((float)session.getPower(), 1); // Power
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry((float)session.getSpeed(), 2); // Speed
        valueSet1.add(v1e3);

        BarDataSet barDataSet = new BarDataSet(valueSet1, "");

        // Set min - max
        barChart.getAxisLeft().setAxisMinValue(0);
        barChart.getAxisLeft().setAxisMaxValue(15);

        // Set colors to colorful
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        return barDataSet;
    }

    /**
     * Initializes XAxis Values of HorizontalBarChart
     * @return
     */
    private List<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Agility");
        xAxis.add("Power");
        xAxis.add("Speed");
        return xAxis;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(56.193846, 10.247143);
        mMap.addMarker(new MarkerOptions().position(location).title("You where here"));
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
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
