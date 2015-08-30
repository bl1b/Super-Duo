package it.jaschke.alexandria;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class About extends Fragment {

    public About() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Edit JG: local rootView variable unused => removed
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override public void onResume() {
        super.onResume();

        // Edit JG: set the title in onResume instead of onAttach
        if (getActivity() != null) {
            getActivity().setTitle(R.string.about);
        }
    }
}
