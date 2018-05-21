package com.example.manikandanvnair.halftune;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.DownloadProgress;
import Models.Track;

/**
 * Created by manikandanvnair on 15/05/18.
 */

public class TrackListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //to reference the Activity
    private final Activity context;
    private final RecyclerViewClicks recyclerViewClicks;
    private final ArrayList<Track> trackList;



    public TrackListAdapter(Activity context, ArrayList<Track> trackList, RecyclerViewClicks recyclerViewClicks){

        this.context = context;
        this.recyclerViewClicks = recyclerViewClicks;
        this.trackList = trackList;


    }


    public ArrayList<Track> getData() {
        return this.trackList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tracklist_row,null);
        TrackListView rowView = new TrackListView(view);

        return rowView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Track currentTrack = this.trackList.get(position);

        TrackListView trackListView = (TrackListView) holder;
        trackListView.title.setText(currentTrack.getName());
        trackListView.track.setText(currentTrack.getArtist());
        Picasso.get().load(currentTrack.getArtUrl())
                .resize(100,100)
                .into(trackListView.imageView);

//        if(currentTrack.isDownloaded())
//        {
//            trackListView.download.setVisibility(View.INVISIBLE);
//        }
//        else
//        {
//            trackListView.download.setVisibility(View.INVISIBLE);
//        }

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    class TrackListView extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {

        TextView title, track;
        ImageView imageView;
        Button download;
        ProgressBar progressBar;
        TextView progressText;
      //  DownloadProgress downloadProgress;

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public TrackListView(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.name);
            track = itemView.findViewById(R.id.track);
            imageView = itemView.findViewById(R.id.imageView);
            download = itemView.findViewById(R.id.download);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressText = itemView.findViewById(R.id.downloadProgress);



            itemView.setOnClickListener(this);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClicks.onButtonClicked(getAdapterPosition(),view,(Button) view);
                }
            });



        }


        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }



}

