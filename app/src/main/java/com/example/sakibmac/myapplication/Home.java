package com.example.sakibmac.myapplication;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

public class Home extends Fragment {
    ImageView parent, student, teacher;
    RequestQueue queue;
    TextView teach, par, stu;

    @SuppressLint("CutPasteId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        queue = Volley.newRequestQueue(getActivity());

        teacher = v.findViewById(R.id.teacher);
        student = v.findViewById(R.id.student);
        parent = v.findViewById(R.id.parent);


        par = v.findViewById(R.id.tparent);
        teach = v.findViewById(R.id.tteacher);
        stu = v.findViewById(R.id.tstudent);


        Glide.with(getActivity())
                .load("http://wbheaventech.com/SchoolManagementApp/image/teacher/parent.png")
                .placeholder(R.drawable.correct)
                .error(R.drawable.error)
                .into(parent);

        Glide.with(getActivity())
                .load("http://wbheaventech.com/SchoolManagementApp/image/teacher/teacher.png")
                .placeholder(R.drawable.correct)
                .error(R.drawable.error)
                .into(teacher);
        Glide.with(getActivity())
                .load("http://wbheaventech.com/SchoolManagementApp/image/teacher/student.png")
                .placeholder(R.drawable.correct)
                .error(R.drawable.error)
                .into(student);

        user_count();
        return v;
    }

    private void user_count() {
        String urls = "http://wbheaventech.com/SchoolManagementApp/image/user_count.php";

        StringRequest request = new StringRequest(urls,
                new Response.Listener<String>() {


                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            teach.setText(object.getString("teacher"));
                            stu.setText(object.getString("student"));
                            par.setText(object.getString("parent"));
                            //}

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


    }
}