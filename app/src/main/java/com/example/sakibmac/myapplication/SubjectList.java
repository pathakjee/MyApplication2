package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.realm.Realm;

public class SubjectList extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private AQuery aQuery;
    Realm myreal;
    Sign sign;

    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;
    RequestQueue queue;
    ArrayAdapter<String> dataAdapter;
    Spinner spinner;
    String cls;
    public int clsid, classid[];

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subject_list, container, false);
        myreal = Realm.getInstance(getActivity());

        queue = Volley.newRequestQueue(getActivity());
        spinner = v.findViewById(R.id.subject);
        dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return v;
    }

    @Override
    public void onStart() {
        dataAdapter.clear();

        String urls = "http://wbheaventech.com/SchoolManagementApp/image/class.php";

        JsonArrayRequest request = new JsonArrayRequest(urls, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    classid = new int[response.length()];

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);

                        classid[i] = object.getInt("class_id");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        cls = adapterView.getItemAtPosition(i).toString();
        clsid = classid[i];
        Toast.makeText(getActivity(), "classid[" + i + "]=" + classid[i], Toast.LENGTH_SHORT).show();

        // Toast.makeText(getActivity(), "" + i + classid[i] + " " + clsid, Toast.LENGTH_SHORT).show();
        loadTeacher();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void loadTeacher() {

        //  if (isConnection()) {
        String url = "http://wbheaventech.com/SchoolManagementApp/image/subjectlist.php?class_id=" + clsid;
        //Toast.makeText(getActivity(), clsid, Toast.LENGTH_SHORT).show();
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


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.subject_list, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);

            viewHolder.subject.setText(tutorListBeans.getSubject());
            viewHolder.teacher.setText(tutorListBeans.getTeacher());

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView subject;
            protected TextView teacher;


            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                subject = itemLayoutView.findViewById(R.id.subject);
                teacher = itemLayoutView.findViewById(R.id.teacher);

            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }
    }
}
