package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


public class ClassRoutine extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner clasc, scec, day;
    ArrayAdapter classAdapter, sectionAdapter, dayAdapter;
    private RecyclerView recyclerView;
    private AQuery aQuery;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;
    String clas, sec;
    Button views;
    RequestQueue queue;
    String classid[], secid[], cls, days;
    private String clsid, seid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_class_routine, container, false);

        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.studentlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        queue = Volley.newRequestQueue(getActivity());
        views = v.findViewById(R.id.view);
        views.setOnClickListener(this);


        clasc = v.findViewById(R.id.claschoose);
        scec = v.findViewById(R.id.section);
        classAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clasc.setAdapter(classAdapter);
        clasc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cls = adapterView.getItemAtPosition(i).toString();
                clsid = classid[i];
                sectionAdapter.clear();
                Toast.makeText(getActivity(), clsid, Toast.LENGTH_SHORT).show();
                String url = "http://wbheaventech.com/SchoolManagementApp/image/class.php?class_id=" + clsid;

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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        day = v.findViewById(R.id.day);
        dayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.day,
                android.R.layout.simple_dropdown_item_1line);
        day.setAdapter(dayAdapter);
        day.setOnItemSelectedListener(this);

        return v;
    }


    private void loadMalls() {

        //  if (isConnection()) {
        //  String url = "http://wbheaventech.com/SchoolManagementApp/image/studentlist.php?class=9&section_id=4";

        String url = "http://wbheaventech.com/SchoolManagementApp/image/class_routine.php?class_id=" + clsid + "&section_id=" + seid + "&day=" + days;
        Toast.makeText(getActivity(), clsid + seid + days, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(aQuery.getContext(), "request_couldnt_be_completed", Toast.LENGTH_LONG).show();
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

    @Override
    public void onStart() {
        classAdapter.clear();
        sectionAdapter.clear();
        String urls = "http://wbheaventech.com/SchoolManagementApp/image/class.php";

        JsonArrayRequest request = new JsonArrayRequest(urls, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    classid = new String[response.length()];

                    //dataAdapter.add("class");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);
                        classid[i] = object.getString("class_id");
                        classAdapter.add(object.getString("class"));
                        classAdapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View view) {
        loadMalls();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        days = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.class_routine, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);


            viewHolder.subject.setText(tutorListBeans.getSubject());
            viewHolder.start.setText(tutorListBeans.getStart());
            viewHolder.end.setText(tutorListBeans.getEnd());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            protected TextView subject;
            protected TextView start;
            protected TextView end;


            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                subject = itemLayoutView.findViewById(R.id.subject);
                start = itemLayoutView.findViewById(R.id.start);
                end = itemLayoutView.findViewById(R.id.end);
            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }

}
