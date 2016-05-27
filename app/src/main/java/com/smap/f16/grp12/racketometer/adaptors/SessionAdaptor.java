package com.smap.f16.grp12.racketometer.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.DateFormatter;
import com.smap.f16.grp12.racketometer.utils.PerformanceFormatter;

import java.util.List;

/**
 * Adaptor for displaying the Session model
 */
public class SessionAdaptor extends BaseAdapter {

    private final Context context;
    private final List<Session> sessions;

    public SessionAdaptor(Context context , List<Session> items) {
        this.context = context;
        this.sessions = items;
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Object getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater sessionInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = sessionInflater.inflate(R.layout.fragment_history_session_adaptor, null);
        }

        Session session = sessions.get(position);

        if(session == null) {
            return convertView;
        }

        TextView txtDesc = (TextView) convertView.findViewById(R.id.adaptorDescription);
        txtDesc.setText(session.getDescription());

        TextView txtPerf = (TextView) convertView.findViewById(R.id.adaptorPerformance);
        txtPerf.setText("Performance: " + PerformanceFormatter.Performance(session));

        TextView txtDate = (TextView) convertView.findViewById(R.id.adaptorDate);
        txtDate.setText(DateFormatter.Date(session.getDate()));

        return convertView;
    }
}
