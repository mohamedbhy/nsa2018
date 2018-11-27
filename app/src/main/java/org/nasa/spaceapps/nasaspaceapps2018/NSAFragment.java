package org.nasa.spaceapps.nasaspaceapps2018;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class NSAFragment extends Fragment{
    private Handler handler;
    private Runnable runnable;
    public static final String ARG_OBJECT = "object";
    protected int val;
    private View rootView = null;
    private FeedAdapter feedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public NSAFragment(){}
    @SuppressLint("ValidFragment")
    public NSAFragment(int val){
        this.val=val;
        this.handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.getData().getBoolean("error"))
                    Toast.makeText(NSAFragment.this.getContext(), "Could Not Update Data", Toast.LENGTH_SHORT).show();
                NSAFragment.this.swipeRefreshLayout.setRefreshing(false);
                NSAFragment.this.feedAdapter.notifyDataSetChanged();
            }
        };
        this.runnable = new Runnable() {
            @Override
            public void run() {
                Map<String,Boolean> res = new HashMap<>();
                boolean error = false;
                try {
                    res = new DataUpdater(getContext()).execute().get();
                } catch (Exception e) {
                    error = true;
                }
                if(res.containsKey("UpdateSuccess") || res.containsKey("NetworkErr")) error = true;
                try {
                    DataFactory.getInstance().update();
                    if(NSAFragment.this.val == 0)NSAFragment.this.feedAdapter.setList(DataFactory.getInstance().getUpcoming());
                    else if(NSAFragment.this.val == 1)NSAFragment.this.feedAdapter.setList(DataFactory.getInstance().getPrevious());
                    else if (NSAFragment.this.val == 2)NSAFragment.this.feedAdapter.setList(DataFactory.getInstance().getAll());
                    else NSAFragment.this.feedAdapter.setList(DataFactory.getInstance().getSaved());
                } catch (Exception e) {
                    error = true;
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putBoolean("error",error);
                message.setData(bundle);
                NSAFragment.this.handler.sendMessage(message);
            }
        };
    }
    public void setNoSaves(){
        ((ImageView)rootView.findViewById(R.id.star)).setVisibility(View.VISIBLE);
        ((TextView)rootView.findViewById(R.id.start)).setVisibility(View.VISIBLE);
        ((RecyclerView)rootView.findViewById(R.id.eventrv)).setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment,container,false);
        RecyclerView recyclerView = this.rootView.findViewById(R.id.eventrv);
        try{
            if(this.val == 3 && DataFactory.getInstance().update().getSaved().size() ==0){
                ((ImageView)this.rootView.findViewById(R.id.star)).setVisibility(View.VISIBLE);
                ((TextView)this.rootView.findViewById(R.id.start)).setVisibility(View.VISIBLE);
                ((RecyclerView)this.rootView.findViewById(R.id.eventrv)).setVisibility(View.GONE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            this.feedAdapter = new FeedAdapter(this,null,this.val,null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(this.feedAdapter);
        this.swipeRefreshLayout =rootView.findViewById(R.id.swipe_refresh);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(NSAFragment.this.runnable).start();
            }
        });
        return rootView;
    }
}