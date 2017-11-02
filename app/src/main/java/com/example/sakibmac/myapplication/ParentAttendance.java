package com.example.sakibmac.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.toolbox.Volley;

import io.realm.Realm;


public class ParentAttendance extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner sp_cate, year;
    ArrayAdapter dataAdapter, yearAdapter;
    String id, months, years;
    Button views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_parent_attendance, container, false);

        id = getArguments().toString();
        views = v.findViewById(R.id.view);
        sp_cate = v.findViewById(R.id.month);

        dataAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.month, R.layout.support_simple_spinner_dropdown_item);

        sp_cate.setAdapter(dataAdapter);
        sp_cate.setOnItemSelectedListener(this);

        year = v.findViewById(R.id.year);

        yearAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.year, R.layout.support_simple_spinner_dropdown_item);

        year.setAdapter(yearAdapter);
        year.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        months = String.valueOf(adapterView.getItemAtPosition(i));
        years = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
