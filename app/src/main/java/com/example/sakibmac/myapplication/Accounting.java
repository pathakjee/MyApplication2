package com.example.sakibmac.myapplication;


import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;


public class Accounting extends Fragment {
    TextView name, contact, email, birthday, religion, clas, blood, fname, mname, gender, address, roll;
    ImageView image;
    Realm myreal;
    String ltype;
    RelativeLayout rfname, rmname, rclass, rroll, rblood, rrel, rbir;
    ProgressDialog loading1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accounting, container, false);
        myreal = Realm.getInstance(getActivity());
        rfname = v.findViewById(R.id.rfname);
        rblood = v.findViewById(R.id.rblood);
        rmname = v.findViewById(R.id.rmname);
        rrel = v.findViewById(R.id.rrel);
        rroll = v.findViewById(R.id.rroll);
        rclass = v.findViewById(R.id.rclass);
        rbir = v.findViewById(R.id.rbir);

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


        loading1 = ProgressDialog.show(getActivity(), "Please wait...", "Fetching...", false, false);

        RealmResults<Sign> sig = myreal.where(Sign.class).findAll();

//for each loop
        for (Sign sign : sig) {
            roll.setText(sign.getRollno());
            address.setText(sign.getAddress());
            gender.setText(sign.getGender());
            mname.setText(sign.getMothername());
            fname.setText(sign.getFathername());
            blood.setText(sign.getBlood());
            clas.setText(sign.getClas());
            religion.setText(sign.getReligion());
            birthday.setText(sign.getBirthday());
            name.setText(sign.getName());
            contact.setText(sign.getContact());
            email.setText(sign.getEmail());
            address.setText(sign.getAddress());
            ltype = sign.getType();
            Glide.with(getActivity())
                    .load(sign.getImage())
                    .placeholder(R.drawable.correct)
                    .error(R.drawable.error)
                    .into(image);
        }
        loading1.dismiss();


        if (ltype.equals("Teacher")) {
            rfname.setVisibility(View.INVISIBLE);
            rmname.setVisibility(View.INVISIBLE);
            rclass.setVisibility(View.INVISIBLE);
            rroll.setVisibility(View.INVISIBLE);
            rrel.setVisibility(View.INVISIBLE);
            rblood.setVisibility(View.INVISIBLE);

        } else if (ltype.equals("Parent")) {
            rfname.setVisibility(View.INVISIBLE);
            rmname.setVisibility(View.INVISIBLE);
            rclass.setVisibility(View.INVISIBLE);
            rroll.setVisibility(View.INVISIBLE);
            rrel.setVisibility(View.INVISIBLE);
            rblood.setVisibility(View.INVISIBLE);
            rbir.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);

        }
        return v;
    }

}