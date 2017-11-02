package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String type;
    EditText email, pass;
    Spinner sp_cate;
    ArrayAdapter dataAdapter;
    RequestQueue requestQueue;
    String url = "http://wbheaventech.com/SchoolManagementApp/image/login.php", res;
    Toolbar toolbar;
    Realm myreal;
    Sign sign;
    String lemail, lpass, ltype;
    ImageView loginnn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loginnn = (ImageView) findViewById(R.id.login);

        Glide.with(this)
                .load("http://wbheaventech.com/SchoolManagementApp/image/teacher/login.png")
                .placeholder(R.drawable.correct)
                .error(R.drawable.error)
                .into(loginnn);

        sp_cate = (Spinner) findViewById(R.id.choose);
        requestQueue = Volley.newRequestQueue(this);
        myreal = Realm.getInstance(this);

        dataAdapter = ArrayAdapter.createFromResource(this,
                R.array.profile, R.layout.support_simple_spinner_dropdown_item);

        sp_cate.setAdapter(dataAdapter);
        sp_cate.setOnItemSelectedListener(this);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        type = String.valueOf(adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
        for (Sign sign : sig) {
            lemail = sign.getEmail();
            lpass = sign.getPassword();
            ltype = sign.getType();
        }
        Toast.makeText(this, lemail, Toast.LENGTH_SHORT).show();
        if (lemail == null || lemail.equals("")) {
            Toast.makeText(this, "please login", Toast.LENGTH_SHORT).show();
        } else if (isConnection()) {

            final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Please wait...", "Fetching...", false, false);


            final StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {


                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);

                                res = object.getString("code");
                                if (res.equals("login_success")) {
                                    loading.dismiss();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.putExtra("login", ltype);
                                    startActivity(i);
                                    LoginActivity.this.finish();

                                } else {
                                    loading.dismiss();
                                    Toast.makeText(LoginActivity.this, "please login", Toast.LENGTH_SHORT).show();
                                }
                            } catch (
                                    JSONException e
                                    )

                            {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<String, String>();

                    param.put("email", lemail);
                    param.put("password", lpass);
                    param.put("type", ltype);
                    param.put("login", "login");


                    return param;
                }
            };
            requestQueue.add(request);

        } else {
            showConnectionErrorDialog();
        }
    }


    public void login(View view) {
   /*     if (type.equals("Select")) {
            Toast.makeText(this, "Please select", Toast.LENGTH_SHORT).show();
        } else {
            Intent login = new Intent(LoginActivity.this, MainActivity.class);
            login.putExtra("login", type);
            startActivity(login);
            finish();
        }
*/
        final String emails = email.getText().toString();
        final String passs = pass.getText().toString();
        // if (isConnection()) {
        if (emails.equals("")) {
            email.setError("enter email");
        } else if (passs.equals("")) {
            pass.setError("enter password");
        } else if (type.equals("Select")) {
            Toast.makeText(LoginActivity.this, "select user type", Toast.LENGTH_SHORT).show();
        } else if (isConnection()) {
            final ProgressDialog loading = ProgressDialog.show(LoginActivity.this, "Please wait...", "Fetching...", false, false);

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {


                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject object = jsonArray.getJSONObject(0);

                                res = object.getString("code");
                                Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();

                                loading.dismiss();

                                if (res.equals("login_success")) {

                                    myreal.beginTransaction();
                                    sign = myreal.createObject(Sign.class);
                                    sign.setType(type);
                                    sign.setPassword(passs);

                                    if (type.equals("Teacher")) {
                                        sign.setId(object.getString("user_id"));
                                        sign.setImage(object.getString("image"));
                                        sign.setName(object.getString("name"));
                                        sign.setEmail(emails);
                                        sign.setContact(object.getString("mobile"));
                                        sign.setAddress(object.getString("address"));
                                        sign.setBirthday(object.getString("birthday"));
                                        sign.setGender(object.getString("gender"));

                                    } else if (type.equals("Student")) {
                                        sign.setId(object.getString("user_id"));
                                        sign.setImage(object.getString("image"));
                                        sign.setName(object.getString("name"));
                                        sign.setEmail(emails);
                                        sign.setContact(object.getString("mobile"));
                                        sign.setAddress(object.getString("address"));
                                        sign.setBirthday(object.getString("birthday"));
                                        sign.setGender(object.getString("gender"));
                                        sign.setFathername(object.getString("father_name"));
                                        sign.setMothername(object.getString("mother_name"));
                                        sign.setClas(object.getString("class"));
                                        sign.setParentid(object.getString("parent_id"));
                                        sign.setRollno(object.getString("roll"));
                                        sign.setBlood(object.getString("blood_group"));
                                        sign.setReligion(object.getString("religion"));
                                    } else if (type.equals("Parent")) {
                                        sign.setId(object.getString("user_id"));
                                        sign.setName(object.getString("name"));
                                        sign.setEmail(emails);
                                        sign.setGender(object.getString("gender"));
                                        sign.setContact(object.getString("mobile"));
                                        sign.setAddress(object.getString("address"));
                                        sign.setProfesssion(object.getString("profession"));
                                    }
                                    myreal.commitTransaction();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("login", type);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "USer not found", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LoginActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })

            {
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> param = new HashMap<String, String>();
                    param.put("email", emails);
                    param.put("password", passs);
                    param.put("type", type);
                    param.put("login", "login");
                    return param;
                }
            };
            requestQueue.add(request);

            email.setText("");
            pass.setText("");
        } else {
            showConnectionErrorDialog();
        }

    }

    public void showConnectionErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet !!");
        builder.setMessage("Not connected to the network right now. Please turn it on and try again later");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                LoginActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }
    }

}