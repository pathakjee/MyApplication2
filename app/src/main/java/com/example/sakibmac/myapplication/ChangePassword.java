package com.example.sakibmac.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ChangePassword extends Fragment implements View.OnClickListener {

    Button change;
    EditText oldp, newp, conp;
    RequestQueue queue;
    String url = "http://wbheaventech.com/SchoolManagementApp/image/change_pass.php", res;
    CheckBox mCbShowPwd;
    Realm myreal;
    Sign sign;
    String id, email, type;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_change_password, container, false);
        queue = Volley.newRequestQueue(getActivity());
        myreal = Realm.getInstance(getActivity());

        oldp = v.findViewById(R.id.currentpas);
        newp = v.findViewById(R.id.newpas);
        conp = v.findViewById(R.id.cnewpas);
        change = v.findViewById(R.id.submit);
        change.setOnClickListener(this);

        mCbShowPwd = (CheckBox) v.findViewById(R.id.checkbox);

        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    oldp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newp.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    oldp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newp.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });


        RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
        for (Sign sign : sig) {
            email = sign.getEmail();
            id = sign.getId();
            type = sign.getType();
        }
        return v;

    }

    @Override
    public void onClick(View view) {
        final String cpasse = conp.getText().toString();
        final String passs = oldp.getText().toString();
        final String newPa = newp.getText().toString();

        if (passs.equals("")) {
            oldp.setError("enter password");
        } else if (newPa.equals("")) {
            oldp.setError("enter new password");
        } else if (cpasse.equals("")) {
            conp.setError("enter confirm password");
        } else if (cpasse.equals(newPa)) {

            final ProgressDialog loading = ProgressDialog.show(getActivity(),"Updating", "Please wait...", false, false);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {


                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);

                                res = object.getString("code");
                                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();

                                loading.dismiss();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                    param.put("email_id", email);
                    param.put("old_pass", passs);
                    param.put("new_pass", passs);
                    param.put("user_type", type);
                    param.put("submit", "submit");
                    return param;
                }
            };
            queue.add(request);

            newp.setText("");
            oldp.setText("");
            conp.setText("");
        }
    }
}

