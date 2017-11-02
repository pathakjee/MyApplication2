package com.example.sakibmac.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.List;

import static com.example.sakibmac.myapplication.R.string.ok_text;

public class NoticeBoard extends Fragment {

    private RecyclerView recyclerView;
    private AQuery aQuery;
    private List<Compliment_Bean> mallsList;
    private MyAdapter myAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notice_board, container, false);

        aQuery = new AQuery(getActivity());
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadMalls();
        return v;
    }


    private void loadMalls() {

/*
        if (isConnection()) {
*/
        String url = "http://wbheaventech.com/SchoolManagementApp/image/noticeboard.php?";

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(" Please wait...");
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
/*
        } else {
            showConnectionErrorDialog();
        }
*/
    }

    private void showConnectionErrorDialog() {
        Builder builder = new Builder(getActivity());
        builder.setTitle("No Internet !!");
        builder.setMessage("Not connected to the network right now. Please turn it on and try again later");
        builder.setPositiveButton("Ok", new OnClickListener() {
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
                    .inflate(R.layout.notice, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // MallsListBean storeBean = mallsList.get(position);
            final Compliment_Bean tutorListBeans = mallsList.get(position);
            viewHolder.title.setText(tutorListBeans.getTitle());
            viewHolder.notice.setText(tutorListBeans.getNotice());
            viewHolder.notice.setMaxLines(2);
            viewHolder.viewnotice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ReadNotice pf = new ReadNotice();
                    Bundle notice = new Bundle();
                    //Bundle title = new Bundle();
                    // title.putString("title", tutorListBeans.getTitle());
                    notice.putString("notice", tutorListBeans.getNotice());
                    pf.setArguments(notice);
                    //pf.setArguments(title);

//Inflate the fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, pf);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected TextView notice;
            protected Button viewnotice;

            public ViewHolder(final View itemLayoutView) {
                super(itemLayoutView);
                title = itemLayoutView.findViewById(R.id.tittle);
                notice = itemLayoutView.findViewById(R.id.notice);
                viewnotice = itemLayoutView.findViewById(R.id.viewnotice);
            }
        }

        @Override
        public int getItemCount() {
            return mallsList.size();
        }

    }

}
