package com.smap.f16.grp12.racketometer.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.smap.f16.grp12.racketometer.R;
import com.smap.f16.grp12.racketometer.adaptors.SessionAdaptor;
import com.smap.f16.grp12.racketometer.models.Session;
import com.smap.f16.grp12.racketometer.utils.DateComparator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment {

    private ListView lstView;
    private SwipeRefreshLayout refreshLayout;
    private List<Session> sessions;
    private OnListFragmentInteractionListener mListener;

    private static final String SESSIONS =
            "com.smap.f16.grp12.racketometer.fragments.HistoryFragment.SESSIONS";

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance(List<Session> list) {
        HistoryFragment fragment = new HistoryFragment();

        Bundle args = new Bundle();

        args.putSerializable(SESSIONS, (Serializable) list);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initUiReferences(view);
        initRefreshHandler();
        getSessions();

        initListView();
        return view;
    }

    private void initRefreshHandler() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.onRefresh(refreshLayout);
            }
        });
    }

    /**
     * Sets the adaptor on the listView and adds an event listener
     */
    private void initListView() {
        Collections.sort(sessions, new DateComparator());
        Collections.reverse(sessions);

        SessionAdaptor sessionAdaptor = new SessionAdaptor(this.getContext(), sessions);
        lstView.setAdapter(sessionAdaptor);

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onSessionSelected(sessions.get(position));
            }
        });
    }

    /**
     * Finds and sets the listView
     *
     * @param view The view.
     */
    private void initUiReferences(View view) {
        lstView = (ListView) view.findViewById(R.id.history);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Gets fragment arguments
     */
    private void getSessions() {
        sessions = (List<Session>) getArguments().getSerializable(SESSIONS);
    }

    /**
     * Interface for users of the fragment to implement if
     * they want the fragment to communicate with them.
     */
    public interface OnListFragmentInteractionListener {
        void onSessionSelected(Session item);
        void onRefresh(SwipeRefreshLayout refreshLayout);
    }
}
