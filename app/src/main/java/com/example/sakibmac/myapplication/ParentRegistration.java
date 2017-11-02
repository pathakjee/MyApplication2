package com.example.sakibmac.myapplication;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class ParentRegistration extends Fragment implements View.OnClickListener {

    EditText name, address, password, confirmPass, email, mobile, profession;
    ImageView imageView, getImage;
    private boolean checkImage = false;
    LinearLayout lLr;
    Button register;
    private int PICK_IMAGE_REQUEST = 1;
    File imagepath;
    RequestQueue queue;
    CheckBox checkBox;
    private Bitmap bitmap;

    private int radioCheckedId;
    String url = "http://wbheaventech.com/SchoolManagementApp/image/Registration.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parent_registration, container, false);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        lLr = (LinearLayout) view.findViewById(R.id.camara_gallery_button);
        register = (Button) view.findViewById(R.id.register);
        name = (EditText) view.findViewById(R.id.name);
        mobile = (EditText) view.findViewById(R.id.mobile);
        email = (EditText) view.findViewById(R.id.email);
        profession = (EditText) view.findViewById(R.id.profession);
        address = (EditText) view.findViewById(R.id.address);
        password = (EditText) view.findViewById(R.id.password);
        confirmPass = (EditText) view.findViewById(R.id.confirmpassword);
        imageView = (ImageView) view.findViewById(R.id.Im_forImage);
        getImage = (ImageView) view.findViewById(R.id.chooseFromGallery);
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        register.setOnClickListener(this);
        queue = Volley.newRequestQueue(getActivity());

        return view;
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

    boolean checkrecord() {
        boolean res = true;
        String names = name.getText().toString().trim();
        String addresss = address.getText().toString().trim();
        String mobiles = mobile.getText().toString().trim();
        String profes = profession.getText().toString().trim();
        String Pass = password.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String cPass = confirmPass.getText().toString().trim();
        final int l = Pass.length();

        if (names.equals("")) {
            name.setError("enter name");
            res = false;
        } else if (addresss.equals("")) {
            address.setError("enter address");
            res = false;
        } else if (profes.equals("")) {
            profession.setError("enter profession");
            res = false;
        } else if (emails.equals("")) {
            email.setError("enter email");
            res = false;
        } else if (emails.contains("@") == false) {
            email.setError("should contain @");
            res = false;
        } else if (mobiles.equals("")) {
            mobile.setError("enter contact");
            res = false;
        } else if (Pass.equals("")) {
            password.setError("enter password");
            res = false;
        } else if (cPass.equals("")) {
            confirmPass.setError("enter confirm password");
            res = false;
        } else if (l < 8) {
            password.setError("password more than 8 ");
            res = false;
        } else if (cPass.equals(Pass)) {
            res = true;
        }

        return res;
    }

    @Override
    public void onClick(View view) {

        //  if (isConnection()) {
        if (!checkImage) {
            Snackbar.make(view, "Please upload Image", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else if (!checkrecord()) {
            Snackbar.make(view, "Please enter all fields", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
            final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            loading.dismiss();
                            if (s.equals("Successfully Uploaded")) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }

                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            loading.dismiss();
                            Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {


                    //Converting Bitmap to String
                    String image = getStringImage(bitmap);
                    String names = name.getText().toString().trim();
                    String addresss = address.getText().toString().trim();
                    String profes = profession.getText().toString().trim();
                    String passwords = password.getText().toString().trim();
                    String mobiles = mobile.getText().toString().trim();//Creating parameters
                    String emails = email.getText().toString().trim();
                    Map<String, String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put("image", image);
                    params.put("name", names);
                    params.put("address", addresss);
                    params.put("profession", profes);
                    params.put("type", "teacher");
                    params.put("email", emails);
                    params.put("phone", mobiles);
                    params.put("password", passwords);
                    params.put("signup", "signup");
                    //returning parameters
                    return params;
                }

            };
            queue.add(stringRequest);
        }
        //    } else {
        //      showConnectionErrorDialog();
        //}

        Toast.makeText(getActivity(), "Signup", Toast.LENGTH_SHORT).show();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                lLr.setVisibility(View.INVISIBLE);
                checkImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
