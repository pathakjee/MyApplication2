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
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MarksFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Button manage;
    Spinner clasc, scec, exam, subject;
    ArrayAdapter classAdapter, sectionAdapter, examAdapter, subjectAdapter;
    private RecyclerView recyclerView;
    private AQuery aQuery;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;
    String clas, sec, sub, exams;
    String classid[], secid[], subid[], examid[], cls;
    String clsid, seid, suid, exid;
    Realm myreal;
    String type = null;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_marks, container, false);
        myreal = Realm.getInstance(getActivity());
        queue = Volley.newRequestQueue(getActivity());
        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
        for (Sign sign : sig) {
            type = sign.getType();

        }

        manage = v.findViewById(R.id.view);
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadExam();
            }
        });
        exam = v.findViewById(R.id.exampaterm);
        examAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        examAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        exam.setAdapter(examAdapter);
        exam.setOnItemSelectedListener(this);


        subject = v.findViewById(R.id.subject);
        subjectAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        subject.setAdapter(subjectAdapter);
        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sub = adapterView.getItemAtPosition(i).toString();
                suid = subid[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                subjectAdapter.clear();
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

                String urd = "http://wbheaventech.com/SchoolManagementApp/image/subject.php?class_id=" + clsid;

                JsonArrayRequest requ = new JsonArrayRequest(urd, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            subid = new String[response.length()];

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = (JSONObject) response.get(i);
                                subid[i] = object.getString("subject_id");

                                subjectAdapter.add(object.getString("subject_name"));
                                subjectAdapter.notifyDataSetChanged();

                            }

                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), "exp" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(requ);

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

        return v;
    }

    @Override
    public void onStart() {
        classAdapter.clear();
        sectionAdapter.clear();
        subjectAdapter.clear();
        examAdapter.clear();
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


        String exam = "http://wbheaventech.com/SchoolManagementApp/image/exam.php";

        JsonArrayRequest requs = new JsonArrayRequest(exam, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    examid = new String[response.length()];

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = (JSONObject) response.get(i);
                        examid[i] = object.getString("exam_id");

                        examAdapter.add(object.getString("exam_name"));
                        examAdapter.notifyDataSetChanged();

                    }

                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "exp" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(requs);


        super.onStart();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        exams = adapterView.getItemAtPosition(i).toString();
        exid = examid[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void loadExam() {

        //if (isConnection()) {
        String url = "http://wbheaventech.com/SchoolManagementApp/image/get_student_marks.php?class_id=" + clsid + "&section_id=" + seid + "&exam_id=" + exid + "&subject_id=" + suid;
        Toast.makeText(getActivity(), clsid + seid + exid + suid, Toast.LENGTH_SHORT).show();
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
        //} else {
        //  showConnectionErrorDialog();
        //}
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
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.marks, null);
            MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {

            final Compliment_Bean tutorListBeans = mallsList.get(position);
            final int s_id = tutorListBeans.getStudentid();
            if (type.equals("Parent")) {

                viewHolder.marks.setEnabled(false);
                viewHolder.comments.setEnabled(false);
                viewHolder.m_total.setEnabled(false);
                viewHolder.ok.setVisibility(View.INVISIBLE);
            } else if (type.equals("Student")) {
                viewHolder.marks.setEnabled(false);
                viewHolder.comments.setEnabled(false);
                viewHolder.m_total.setEnabled(false);
                viewHolder.ok.setVisibility(View.INVISIBLE);
            }


            viewHolder.rollno.setText(tutorListBeans.getRoll());
            viewHolder.name.setText(tutorListBeans.getName());
            viewHolder.marks.setText(tutorListBeans.getMarks());
            viewHolder.m_total.setText(tutorListBeans.getMarksTotal());
            viewHolder.comments.setText(tutorListBeans.getComments());
            viewHolder.ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final ProgressDialog loading = ProgressDialog.show(getActivity(), "Updating", "Please wait...", false, false);
                    String t = null;
                    RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
                    for (Sign sign : sig) {
                        t = sign.getId();

                    }
                    final String t_id = t;

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://wbheaventech.com/SchoolManagementApp/image/get_student_marks.php",
                            new Response.Listener<String>() {
                                @Override

                                public void onResponse(String response) {
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                    loading.dismiss();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            String mar = viewHolder.marks.getText().toString().trim();
                            String comme = viewHolder.comments.getText().toString().trim();
                            String mark_total = viewHolder.m_total.getText().toString().trim();

                            String roll = viewHolder.rollno.toString();
                            Map<String, String> params = new Hashtable<String, String>();

                            params.put("exam_id", exid);
                            params.put("student_id", String.valueOf(s_id));
                            params.put("section_id", seid);
                            params.put("subject_id", suid);
                            params.put("class_id", clsid);
                            params.put("teacher_id", t_id);
                            params.put("mark_obtained", mar);
                            params.put("mark_total", mark_total);
                            params.put("comment", comme);
                            return params;
                        }
                    };
                    queue.add(stringRequest);

                }
            });

        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView rollno;
            protected TextView name;
            protected EditText marks;
            protected EditText comments;
            protected EditText m_total;
            protected Button ok;

            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                rollno = itemLayoutView.findViewById(R.id.rno);
                name = itemLayoutView.findViewById(R.id.name);
                marks = itemLayoutView.findViewById(R.id.marks);
                comments = itemLayoutView.findViewById(R.id.comment);
                m_total = itemLayoutView.findViewById(R.id.mark_total);
                ok = itemLayoutView.findViewById(R.id.ok);

            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }

}

