package com.example.sakibmac.myapplication;

import android.support.v4.app.Fragment;
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
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sakibmac.myapplication.MainActivity;
import com.example.sakibmac.myapplication.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class StudentForm extends Fragment implements View.OnClickListener {

    EditText fname, mname, birthday, classs, email, section, roll, blood;
    ImageView imageView, getImage;
    private boolean checkImage = false;
    LinearLayout lLr;
    Button register;
    private int PICK_IMAGE_REQUEST = 1;
    File imagepath;
    RequestQueue queue;
    CheckBox checkBox;
    private Bitmap bitmap;
    //String type="Student";
    String genders, religions;
    private int radioCheckedId, radioCheckedId1;
    String url = "http://wbheaventech.com/SchoolManagementApp/image/Registration.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_form, container, false);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioCheckedId = checkedId;
                if (radioCheckedId == 1) {
                    genders = "Male";
                    Toast.makeText(getActivity(), genders, Toast.LENGTH_SHORT).show();
                }
                if (radioCheckedId == 2) {
                    genders = "Female";
                    Toast.makeText(getActivity(), genders, Toast.LENGTH_SHORT).show();
                }

            }
        });
        RadioGroup rel = (RadioGroup) view.findViewById(R.id.religion);
        rel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioCheckedId1 = i;
                if (radioCheckedId1 == 1) {
                    religions = "Hindu";
                    Toast.makeText(getActivity(), "religin:" + religions, Toast.LENGTH_SHORT).show();
                }
                if (radioCheckedId1 == 2) {
                    religions = "Muslim";
                    Toast.makeText(getActivity(), "religin:" + religions, Toast.LENGTH_SHORT).show();
                }
                if (radioCheckedId1 == 3) {
                    religions = "Sikh";
                    Toast.makeText(getActivity(), "religin:" + religions, Toast.LENGTH_SHORT).show();
                }
                if (radioCheckedId1 == 4) {
                    religions = "Other";
                    Toast.makeText(getActivity(), "religin:" + religions, Toast.LENGTH_SHORT).show();
                }
            }
        });


        lLr = (LinearLayout) view.findViewById(R.id.camara_gallery_button);
        register = (Button) view.findViewById(R.id.register);
        fname = (EditText) view.findViewById(R.id.fname);
        mname = (EditText) view.findViewById(R.id.mname);
        section = (EditText) view.findViewById(R.id.section);
        roll = (EditText) view.findViewById(R.id.rollno);
        blood = (EditText) view.findViewById(R.id.blood);

        email = (EditText) view.findViewById(R.id.email);
        birthday = (EditText) view.findViewById(R.id.date);
        classs = (EditText) view.findViewById(R.id.classs);
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
        String fnames = fname.getText().toString().trim();
        String mnames = mname.getText().toString().trim();
        String date = birthday.getText().toString().trim();
        String clas = classs.getText().toString().trim();
        String emails = email.getText().toString().trim();
        String sections = section.getText().toString().trim();
        String rolls = roll.getText().toString().trim();
        String bloods = blood.getText().toString().trim();

        if (fnames.equals("")) {
            fname.setError("enter name");
            res = false;
        } else if (mnames.equals("")) {
            fname.setError("enter name");
            res = false;
        } else if (date.equals("")) {
            birthday.setError("enter birthday");
            res = false;
        } else if (clas.equals("")) {
            classs.setError("enter class");
            res = false;
        } else if (emails.equals("")) {
            email.setError("enter email");
            res = false;
        } else if (emails.contains("@") == false) {
            email.setError("should contain @");
            res = false;
        } else if (sections.equals("")) {
            section.setError("enter section");
            res = false;
        } else if (bloods.equals("")) {
            blood.setError("enter Blood group");
            res = false;
        } else if (rolls.equals("")) {
            roll.setError("enter roll no");
            res = false;
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
                    String fnames = fname.getText().toString().trim();
                    String mnames = mname.getText().toString().trim();
                    String date = birthday.getText().toString().trim();
                    String clas = classs.getText().toString().trim();
                    String emails = email.getText().toString().trim();
                    String sections = section.getText().toString().trim();
                    String rolls = roll.getText().toString().trim();
                    String bloods = blood.getText().toString().trim();

                    Map<String, String> params = new Hashtable<String, String>();

                    //Adding parameters
                    params.put("image", image);
                    params.put("father_name", fnames);
                    params.put("mother_name", mnames);
                    params.put("birthday", date);
                    params.put("class", clas);
                    params.put("type", "student");
                    params.put("gender", genders);
                    params.put("email", emails);
                    params.put("section_id", sections);
                    params.put("roll", rolls);
                    params.put("religion", religions);

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
