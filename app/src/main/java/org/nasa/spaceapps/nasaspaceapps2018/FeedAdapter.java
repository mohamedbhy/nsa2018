package org.nasa.spaceapps.nasaspaceapps2018;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private NSAFragment nsaFragment;
    private int val;
    private String search;
    private List<ListItem> list = new ArrayList<>();
    public FeedAdapter(NSAFragment nsaFragment,Context context,int val,String search) throws ParseException {
        this.nsaFragment = nsaFragment;
        this.context = nsaFragment == null? context : nsaFragment.getContext();
        this.val = nsaFragment == null ? val : nsaFragment.val;
        if(this.val == 0)this.list = DataFactory.getInstance().getUpcoming();
        else if(this.val == 1)this.list = DataFactory.getInstance().getPrevious();
        else if (this.val == 2)this.list = DataFactory.getInstance().getAll();
        else if(this.val == 3)this.list = DataFactory.getInstance().getSaved();
        else if(this.val == 4 && search !=null) this.list = DataFactory.getInstance().getSearch(search);
    }
    public void updateSearchList(String search){
        this.search = search;
        try {
            this.list = DataFactory.getInstance().getSearch(search);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }
    public void setList(List<ListItem> list) {
        this.list = list;
    }

    private View.OnClickListener getLaunchDetails(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedAdapter.this.context,DetailsActivity.class);
                intent.putExtra("id",FeedAdapter.this.list.get(position).getId());
                FeedAdapter.this.context.startActivity(intent);
            }
        };
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView title,company,base,date,rocket;
        protected ImageButton star;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.company = (TextView)itemView.findViewById(R.id.company);
            this.base = (TextView)itemView.findViewById(R.id.base);
            this.date = (TextView)itemView.findViewById(R.id.date);
            this.rocket = (TextView)itemView.findViewById(R.id.rocket);
            this.star = (ImageButton)itemView.findViewById(R.id.star);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.launch_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ((ViewHolder)holder).title.setText(this.list.get(position).getMissionName());
            ((ViewHolder)holder).title.setOnClickListener(getLaunchDetails(position));
            ((ViewHolder)holder).company.setText("SpaceX");
            ((ViewHolder)holder).company.setOnClickListener(getLaunchDetails(position));
            ((ViewHolder)holder).base.setText(this.list.get(position).getLaunchSite());
            ((ViewHolder)holder).base.setOnClickListener(getLaunchDetails(position));
            ((ViewHolder)holder).date.setText(format.format(this.list.get(position).getLaunchDate()));
            ((ViewHolder)holder).date.setOnClickListener(getLaunchDetails(position));
            ((ViewHolder)holder).rocket.setText(this.list.get(position).getRocketName());
            ((ViewHolder)holder).rocket.setOnClickListener(getLaunchDetails(position));
            if(this.list.get(position).isSaved()) ((ViewHolder)holder).star.setSelected(true);
            ((ViewHolder)holder).star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    if(v.isSelected()) Toast.makeText(context, "Event Added To Saved List", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(context, "Event Removed From Saved List", Toast.LENGTH_SHORT).show();
                    int id=0;
                    try{
                        if( FeedAdapter.this.val == 0 )id=DataFactory.getInstance().getUpcoming().get(position).getId();
                        else if(FeedAdapter.this.val == 1)id=DataFactory.getInstance().getPrevious().get(position).getId();
                        else if(FeedAdapter.this.val ==2)id=DataFactory.getInstance().getAll().get(position).getId();
                        else if (FeedAdapter.this.val ==3)id=DataFactory.getInstance().getSaved().get(position).getId();
                        else id=DataFactory.getInstance().getSearch(FeedAdapter.this.search).get(position).getId();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if(v.isSelected()){
                        NSAEventIntentService.CreateAlarm(id,FeedAdapter.this.context);
                        SQLiteOpenHelper helper = new DBHelper(FeedAdapter.this.context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("saved",true);
                        db.update(SQLItems.TABLE_LAUNCHES,values,"flight_number = "+id,null);
                        db.close();
                    }
                    else {
                        SQLiteOpenHelper helper = new DBHelper(FeedAdapter.this.context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("saved",false);
                        db.update(SQLItems.TABLE_LAUNCHES,values,"flight_number = "+id,null);
                        db.close();
                        NSAEventIntentService.CancelAlarm(id,FeedAdapter.this.context);
                    }
                    try {
                        DataFactory.getInstance().update();
                        FeedAdapter.this.notifyDataSetChanged();
                        if(DataFactory.getInstance().getSaved().size() == 0 && FeedAdapter.this.val == 3){
                            FeedAdapter.this.nsaFragment.setNoSaves();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
}
