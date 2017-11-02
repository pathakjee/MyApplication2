package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    TextView name, contact, email, birthday, religion, clas, blood, fname, mname, gender, address, roll;
    TextView Profession;
    ImageView image;
    RequestQueue queue;
    String id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        queue = Volley.newRequestQueue(getActivity());


        name = v.findViewById(R.id.name);
        contact = v.findViewById(R.id.mobile);
        email = v.findViewById(R.id.email);
        birthday = v.findViewById(R.id.birthday);
        religion = v.findViewById(R.id.religion);
        blood = v.findViewById(R.id.blood);
        fname = v.findViewById(R.id.fname);
        mname = v.findViewById(R.id.mname);
        image = v.findViewById(R.id.image);
        address = v.findViewById(R.id.address);
        gender = v.findViewById(R.id.gender);
        roll = v.findViewById(R.id.rollno);
        clas = v.findViewById(R.id.classs);
        id = getArguments().getString("id");

        loadProfile();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadProfile();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProfile();
    }

    private void loadProfile() {
        Toast.makeText(getActivity(), "id=" + id, Toast.LENGTH_SHORT).show();

        String urd = "http://wbheaventech.com/SchoolManagementApp/image/studentprofile.php?student_id=" + id;
        final ProgressDialog loading1 = ProgressDialog.show(getActivity(), "Profile...", "Please wait...", false, false);
        StringRequest request = new StringRequest(urd,
                new Response.Listener<String>() {


                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject object = jsonArray.getJSONObject(0);
                            Glide.with(getActivity())
                                    .load(object.getString("image"))
                                    .placeholder(R.drawable.correct)
                                    .error(R.drawable.error)
                                    .into(image);

                            name.setText(object.getString("student_name"));
                            email.setText(object.getString("email"));
                            birthday.setText(object.getString("birthday"));
                            contact.setText(object.getString("mobile"));
                            blood.setText(object.getString("blood_group"));
                            religion.setText(object.getString("religion"));
                            fname.setText(object.getString("father_name"));
                            mname.setText(object.getString("mother_name"));
                            address.setText(object.getString("address"));
                            gender.setText(object.getString("gender"));
                            roll.setText(object.getString("roll"));
                            clas.setText(object.getString("class"));
                            loading1.dismiss();
                        } catch (
                                JSONException e
                                )

                        {
                            e.printStackTrace();
                        }

                    }
                }
                ,
                new Response.ErrorListener()

                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        queue.add(request);
    }
}