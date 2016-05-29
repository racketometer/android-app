package com.smap.f16.grp12.racketometer.utils;

import android.content.res.Resources;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.models.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Overview chart for presenting performance statistics.
 */
public class OverviewChart {
    private final BarChart chart;
    private final Resources resources;

    public OverviewChart(BarChart chart, Resources resources) {
        this.chart = chart;
        this.resources = resources;

        chart.setDrawValueAboveBar(true);
        chart.setTouchEnabled(false);
        chart.setDescription("");

        setXAxis();
        setYAxis();
        setLegend();
    }

    private void setLegend() {
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
    }

    private void setYAxis() {
        YAxis right = chart.getAxisRight();
        right.setEnabled(false);

        YAxis left = chart.getAxisLeft();
        left.setEnabled(true);
        left.setAxisMinValue(0);
    }

    private void setXAxis() {
        XAxis axis = chart.getXAxis();
        axis.setEnabled(false);
        axis.setDrawGridLines(false);
    }

    /**
     * Set chart data based on sessions.
     * @param sessions The sessions.
     */
    public void setData(List<Session> sessions) {
        ArrayList<String> xValues = new ArrayList<>();
        ArrayList<BarEntry> performanceValues = new ArrayList<>();

        Collections.sort(sessions, new DateComparator());

        for (int i = 0; i < sessions.size(); i++) {
            Session session = sessions.get(i);

            xValues.add(DateFormatter.Date(session.getDate()));
            double performance = PerformanceFormatter.PerformanceAsDouble(session);
            performanceValues.add(new BarEntry((float)performance, i));
        }

        BarDataSet agilitySet = new BarDataSet(performanceValues, resources.getString(R.string.overview_agility));

        agilitySet.setColor(resources.getColor(R.color.color_accent, null));

        BarData barData = new BarData(xValues, agilitySet);
        barData.setDrawValues(false);
        chart.setData(barData);
        chart.animateY(resources.getInteger(R.integer.chart_animation));
    }
}
