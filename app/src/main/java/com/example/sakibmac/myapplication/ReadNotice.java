package com.example.sakibmac.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sakib Mac on 10/31/2017.
 */

public class ReadNotice extends Fragment {
    String title, notice;
    TextView tit, notic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.read_notice, container, false);

       // title = getArguments().getString("title");
        notice = getArguments().getString("notice");

        tit = v.findViewById(R.id.title);
        notic = v.findViewById(R.id.notice);

       // tit.setText(title);
        notic.setText(notice);
        return v;
    }
}
