package com.example.sakibmac.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;


public class DailyAttendance extends Fragment implements View.OnClickListener, OnItemSelectedListener {
    EditText datepicker;
    Spinner clasc, scec;
    Button views;
    ArrayAdapter classAdapter, sectionAdapter;
    private RecyclerView recyclerView;
    private AQuery aQuery;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;
    String clas, sec, date;
    RequestQueue queue;
    String classid[], secid[], cls, days;
    Realm myreal;

    private String clsid, seid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily_attendance, container, false);
        myreal = Realm.getInstance(getActivity());

        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.Attendance);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        queue = Volley.newRequestQueue(getActivity());
        views = v.findViewById(R.id.view);
        views.setOnClickListener(this);
        SimpleDateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        String thisdate = currentdate.format(today);
        datepicker = v.findViewById(R.id.datepicker);
        datepicker.setText(thisdate);
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                java.util.Calendar c = java.util.Calendar.getInstance();
                @SuppressLint("WrongConstant") int dd = c.get(Calendar.DATE);
                @SuppressLint("WrongConstant") int mm = c.get(Calendar.MONTH);
                @SuppressLint("WrongConstant") int yy = c.get(Calendar.YEAR);

                date = dd + "/" + mm + "/" + yy;

                DatePickerDialog dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int mo = month + 1;
                        String day = null;
                        String mm = null;
                        if (dayOfMonth < 10) {

                            day = "0" + dayOfMonth;
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }
                        if (mo < 10) {

                            mm = "0" + mo;
                        } else {
                            mm = String.valueOf(mo);
                        }
                        Toast.makeText(getActivity(), mo + month + mm, Toast.LENGTH_SHORT).show();
                        String str = day + "/" + mm + "/" + year;
                        datepicker.setText(str);
                    }
                }, yy, mm, dd);
                dp.show();

            }
        });
        clasc = v.findViewById(R.id.claschoose);
        scec = v.findViewById(R.id.section);
        classAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clasc.setAdapter(classAdapter);
        clasc.setOnItemSelectedListener(new OnItemSelectedListener() {
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
        scec.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sec = adapterView.getItemAtPosition(i).toString();
                seid = secid[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }


    private void loadMalls() {

        String date1 = datepicker.getText().toString();

        String url = "http://wbheaventech.com/SchoolManagementApp/image/attendence.php?class_id=" + clsid + "&section_id=" + seid + "&date=" + date1;

        Toast.makeText(getActivity(), clsid + seid + date1, Toast.LENGTH_SHORT).show();

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
        clas = adapterView.getItemAtPosition(i).toString();
        sec = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.attendance, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);
            final String[] ss = {tutorListBeans.getStatus()};
            final int s_id = tutorListBeans.getStudentid();
            String type = null;
            RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
            for (Sign sign : sig) {
                type = sign.getType();

            }


            viewHolder.rollno.setText(tutorListBeans.getRoll());
            viewHolder.name.setText(tutorListBeans.getName());
            viewHolder.statusAdapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.status, R.layout.support_simple_spinner_dropdown_item);

            viewHolder.sstatus.setAdapter(viewHolder.statusAdapter);
            if (type.equals("Teacher")) {

                viewHolder.sstatus.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String s = adapterView.getItemAtPosition(i).toString();
                        SimpleDateFormat currentdate = new SimpleDateFormat("dd/MM/yyyy");
                        Date today = new Date();
                        String thisdate = currentdate.format(today);
                        //       Toast.makeText(getActivity(), thisdate, Toast.LENGTH_SHORT).show();
                        String date = datepicker.getText().toString();


                        if (ss[0].equals("0")) {
                            s = "Undefined";
                            viewHolder.status.setText(s);
                            ss[0] = "";

                        } else if (ss[0].equals("1")) {
                            s = "Present";
                            viewHolder.status.setText(s);
                            ss[0] = "";
                        } else if (ss[0].equals("2")) {
                            s = "Absent";
                            viewHolder.status.setText(s);
                            ss[0] = "";
                        } else if (date != thisdate) {

                            viewHolder.status.setText(s);
                            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating", "Please wait...", false, false);
                            final int newStatus = i;
                            String t = null;
                            RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
                            for (Sign sign : sig) {
                                t = sign.getId();

                            }
                            final String t_id = t;
                            String ur = "http://wbheaventech.com/SchoolManagementApp/image/attendence.php";
                            StringRequest request = new StringRequest(Request.Method.POST, ur,
                                    new Response.Listener<String>() {


                                        public void onResponse(String response) {
                                            if (response.equals("true")) {
                                                Toast.makeText(getActivity(), "update", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "not update", Toast.LENGTH_SHORT).show();

                                            }
                                            loading.dismiss();

                                        }
                                    }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })

                            {
                                protected Map<String, String> getParams() throws AuthFailureError {

                                    Map<String, String> param = new HashMap<String, String>();
                                    param.put("student_id", String.valueOf(s_id));
                                    param.put("teacher_id", t_id);
                                    param.put("status", String.valueOf(newStatus));
                                    return param;
                                }
                            };
                            queue.add(request);

                        } else {
                            Toast.makeText(getActivity(), "select current date", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

        }


        public class ViewHolder extends RecyclerView.ViewHolder {


            protected TextView rollno;
            protected TextView name;
            public TextView status;
            protected Spinner sstatus;
            protected ArrayAdapter statusAdapter;


            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                rollno = itemLayoutView.findViewById(R.id.rollno);
                name = itemLayoutView.findViewById(R.id.name);
                status = itemLayoutView.findViewById(R.id.status);
                sstatus = itemLayoutView.findViewById(R.id.sstatus);
            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }

}
