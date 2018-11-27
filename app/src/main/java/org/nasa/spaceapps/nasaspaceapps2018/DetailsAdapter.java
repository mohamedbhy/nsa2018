package org.nasa.spaceapps.nasaspaceapps2018;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String YOUTUBE_API_KEY = "AIzaSyARoxTFxTDJ7HrQwAfdNDWXNRFZiHU9OCE";
    private Context context;
    private FragmentManager manager;
    private DetailsItem data;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public DetailsAdapter(Context context,int id,FragmentManager manager){
        this.context = context;
        this.manager = manager;
        try {
            this.data = DataFactory.getInstance().getItemDetails(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public static class EssentialViewHolder extends RecyclerView.ViewHolder{
        protected TextView missionName,launchDate,rocketName;
        protected ImageView missionImage;
        protected ImageButton star;
        public EssentialViewHolder(View itemView) {
            super(itemView);
            this.missionName = itemView.findViewById(R.id.missionname);
            this.launchDate = itemView.findViewById(R.id.launchdate);
            this.rocketName = itemView.findViewById(R.id.rocket_name);
            this.missionImage = itemView.findViewById(R.id.missionimage);
            this.star = itemView.findViewById(R.id.star);
        }
    }
    public static class StatViewHoler extends RecyclerView.ViewHolder{
        protected TextView numberOfCores,cnw,payloadsNumber,pnw,staticFireDate,launchSuccess;
        public StatViewHoler(View itemView) {
            super(itemView);
            this.numberOfCores = itemView.findViewById(R.id.number_of_cores);
            this.cnw = itemView.findViewById(R.id.cnw);
            this.payloadsNumber = itemView.findViewById(R.id.payloads_number);
            this.pnw = itemView.findViewById(R.id.pnw);
            this.staticFireDate = itemView.findViewById(R.id.static_fire_date);
            this.launchSuccess = itemView.findViewById(R.id.launch_success);
        }
    }
    public static class DetailsViewHolder extends RecyclerView.ViewHolder{
        protected TextView details,moreDetails;
        public DetailsViewHolder(View itemView) {
            super(itemView);
            this.details = itemView.findViewById(R.id.details);
            this.moreDetails = itemView.findViewById(R.id.more_details);
        }
    }
    public class YoutubeViewHolder extends RecyclerView.ViewHolder{
        protected  YouTubePlayerFragment fragment;
        protected YouTubePlayer.OnInitializedListener onInitializedListener;
        @SuppressLint("ResourceType")
        public YoutubeViewHolder(View itemView) {
            super(itemView);
            this.fragment = YouTubePlayerFragment.newInstance();
            FragmentManager manager = DetailsAdapter.this.manager;
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.fadein,R.anim.fadeout)
                    .replace(R.id.youtube_player,fragment)
                    .addToBackStack(null)
                    .commit();

        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(this.context==null) this.context=parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        if(viewType==0){
            view = layoutInflater.inflate(R.layout.essential,parent,false);
            return new EssentialViewHolder(view);
        }
        else if(viewType==1){
            view = layoutInflater.inflate(R.layout.stat,parent,false);
            return new StatViewHoler(view);
        }
        else if(viewType==2){
            view = layoutInflater.inflate(R.layout.fdetails,parent,false);
            return new DetailsViewHolder(view);
        }
        else{
            view = layoutInflater.inflate(R.layout.youtubedetails,parent,false);
            view.animate().alpha(1);
            return new YoutubeViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            ((EssentialViewHolder)holder).missionName.setText(this.data.getMissionName());
            ((EssentialViewHolder)holder).launchDate.setText(format.format(this.data.getLaunchDate()));
            ((EssentialViewHolder)holder).rocketName.setText(this.data.getRocketName());
            Picasso.get().load(this.data.getImage()).fit().into(((EssentialViewHolder)holder).missionImage);
            ((EssentialViewHolder)holder).star.setSelected(this.data.isSaved());
            ((EssentialViewHolder)holder).star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                    if(v.isSelected()) Toast.makeText(context, "Event Added To Saved List", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(context, "Event Removed From Saved List", Toast.LENGTH_SHORT).show();
                    SQLiteOpenHelper helper = new DBHelper(DetailsAdapter.this.context);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("saved",true);
                    db.update(SQLItems.TABLE_LAUNCHES,values,"flight_number = "+DetailsAdapter.this.data.getId(),null);
                    db.close();
                }
            });
        }
        else if(position==1){
            ((StatViewHoler)holder).numberOfCores.setText(Integer.toString(this.data.getCoresCount()));
            ((StatViewHoler)holder).cnw.setText(context.getResources().getQuantityString(R.plurals.cores_serials,this.data.getCoresCount()));
            ((StatViewHoler)holder).payloadsNumber.setText(Integer.toString(this.data.getPayloadsCount()));
            ((StatViewHoler)holder).pnw.setText(context.getResources().getQuantityString(R.plurals.payloads_ids,this.data.getPayloadsCount()));
            if(this.data.getStaticFireDate()!=null)((StatViewHoler)holder).staticFireDate.setText(format.format(this.data.getStaticFireDate()));
            ((StatViewHoler)holder).launchSuccess.setText(this.data.isLaunchSuccess()?"Yes":"No");
        }
        else if(position==2){
            if(this.data.getDetails()!=null)
            ((DetailsViewHolder)holder).details.setText(this.data.getDetails());
            ((DetailsViewHolder)holder).moreDetails.setText(Html.fromHtml("<a href=\""+this.data.getMoreDetails()+"\">here</a>"));
        }
        else {
            ((YoutubeViewHolder)holder).onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.cueVideo(DetailsAdapter.this.data.getYoutubeId());
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            };
            ((YoutubeViewHolder)holder).fragment.initialize(YOUTUBE_API_KEY,((YoutubeViewHolder)holder).onInitializedListener);
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
