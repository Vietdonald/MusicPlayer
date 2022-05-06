package hqv.app.music.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hqv.app.music.R;

public class NewsFragment extends Fragment {
    private RecyclerView rcvListNew;

    public NewsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        rcvListNew = view.findViewById(R.id.rcv_listNews);
        rcvListNew.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
}