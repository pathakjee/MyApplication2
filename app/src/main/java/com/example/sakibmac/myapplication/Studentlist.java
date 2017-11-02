package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Studentlist extends Fragment  {
    private RecyclerView recyclerView;
    private AQuery aQuery;
    Spinner spinner, scec;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;
    RequestQueue queue;
    String cls, sec;
    Realm myreal;
    Sign sign;
    TextView nodata;
    ArrayAdapter<String> dataAdapter, sectionAdapter;
    String id, classid[], secid[];
    private String clsid, seid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentlist, container, false);


        myreal = Realm.getInstance(getActivity());
        RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
        for (Sign sign : sig) {
            id = sign.getId();
        }
        spinner = v.findViewById(R.id.classs);
        scec = v.findViewById(R.id.section);
        dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nodata = v.findViewById(R.id.empty);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cls = adapterView.getItemAtPosition(i).toString();
                clsid = classid[i];
                sectionAdapter.clear();
                Toast.makeText(getActivity(), clsid, Toast.LENGTH_SHORT).show();
                String url = "http://wbheaventech.com/SchoolManagementApp/image/class.php?class_id=" + clsid + "&teacher_id=" + id;

                JsonArrayRequest request1 = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            secid = new String[response.length()];

                            // sectionAdapter.add("section");
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = (JSONObject) response.get(i);
                                secid[i] = object.getString("section_id");
                                sectionAdapter.add(object.getString("section"));
                                sectionAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "exp" + ex.getMessage(), Toast.LENGTH_SHORT).
                                    show();
                        }


                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(request1);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sectionAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        scec.setAdapter(sectionAdapter);
        scec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sec = adapterView.getItemAtPosition(i).toString();
                seid = secid[i];
                loadStudent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        queue = Volley.newRequestQueue(getActivity());


        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.studentlist);
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.RED)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //loadTeacher();
        return v;
    }

    @Override
    public void onStart() {
        dataAdapter.clear();
        sectionAdapter.clear();
        Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
        String urls = "http://wbheaventech.com/SchoolManagementApp/image/class.php?teacher_id=" + id;

        JsonArrayRequest request = new JsonArrayRequest(urls, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    classid = new String[response.length()];

                    //dataAdapter.add("class");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);
                        classid[i] = object.getString("class_id");
                        dataAdapter.add(object.getString("class"));
                        dataAdapter.notifyDataSetChanged();

                    }

                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "exp" + ex.getMessage(), Toast.LENGTH_SHORT).
                            show();
                }


            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);


        super.onStart();
    }


    private void loadStudent() {

        //  if (isConnection()) {
        String url = "http://wbheaventech.com/SchoolManagementApp/image/studentlist.php?class=" + cls + "&section_id=" + seid;

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        aQuery.progress(progressDialog).ajax(url, null, JSONObject.class, new AjaxCallback<JSONObject>() {


            public void callback(String url, JSONObject json, AjaxStatus status) {
                if (json != null) {
                    mallsList = ConversionHelper.getcomment(json);
                    myAdapter = new MyAdapter();
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(aQuery.getContext(), "request_couldnot_be_completed", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*} else {
            showConnectionErrorDialog();
        }*/
    }

    private void showConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("No Internet !!");
        builder.setMessage("Not connected to the network right now. Please turn it on and try again later");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Compliment_Bean> mlist;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.teacher_list, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);

            if (tutorListBeans.getCode().equals("found")) {

                nodata.setVisibility(View.INVISIBLE);
                viewHolder.name.setText(tutorListBeans.getName());
                viewHolder.email.setText(tutorListBeans.getEmail());
                Glide.with(getActivity())
                        .load(tutorListBeans.getImage())
                        .placeholder(R.drawable.correct)
                        .error(R.drawable.error)
                        .into(viewHolder.image);

                viewHolder.show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ProfileFragment pf = new ProfileFragment();
                        Bundle args = new Bundle();
                        args.putString("id", String.valueOf(tutorListBeans.getStudentid()));
                        pf.setArguments(args);

//Inflate the fragment
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, pf);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            } else {

                nodata.setVisibility(View.VISIBLE);
            }
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            protected ImageView image;
            protected TextView name;
            protected TextView email;
            protected RelativeLayout show;

            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                name = itemLayoutView.findViewById(R.id.name);
                email = itemLayoutView.findViewById(R.id.email);
                image = itemLayoutView.findViewById(R.id.image);
                show = itemLayoutView.findViewById(R.id.show);
            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }
}
