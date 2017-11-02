package com.example.sakibmac.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONObject;

import java.util.List;


public class Teacherlist extends Fragment {
    private RecyclerView recyclerView;
    private AQuery aQuery;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_teacherlist, container, false);

        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.teacherlist);

        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.BLACK)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                        .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loadMalls();
        return v;
    }


    private void loadMalls() {


        String url = "http://wbheaventech.com/SchoolManagementApp/image/teacherlist.php";

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
                    .inflate(R.layout.teacher_list, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);

            viewHolder.name.setText(tutorListBeans.getName());
            viewHolder.email.setText(tutorListBeans.getEmail());
            Glide.with(getActivity())
                    .load(tutorListBeans.getImage())
                    .placeholder(R.drawable.correct)
                    .error(R.drawable.error)
                    .into(viewHolder.image);

        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView name;
            protected TextView email;
            protected ImageView image;

            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                name = (TextView) itemLayoutView.findViewById(R.id.name);
                email = (TextView) itemLayoutView.findViewById(R.id.email);
                image = itemLayoutView.findViewById(R.id.image);
            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }

}
